package yos.clash.material

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.color.DynamicColors
import yos.clash.material.common.compat.isAllowForceDarkCompat
import yos.clash.material.common.compat.isLightNavigationBarCompat
import yos.clash.material.common.compat.isLightStatusBarsCompat
import yos.clash.material.common.compat.isSystemBarsTranslucentCompat
import com.github.kr328.clash.core.bridge.ClashException
import yos.clash.material.design.Design
import yos.clash.material.design.model.DarkMode
import yos.clash.material.design.store.UiStore
import yos.clash.material.design.ui.DayNight
import yos.clash.material.design.util.resolveThemedBoolean
import yos.clash.material.design.util.resolveThemedColor
import yos.clash.material.design.util.showExceptionToast
import yos.clash.material.remote.Broadcasts
import yos.clash.material.remote.Remote
import yos.clash.material.util.ActivityResultLifecycle
import yos.clash.material.util.ApplicationObserver
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

abstract class BaseActivity<D : Design<*>> :
    AppCompatActivity(),
    CoroutineScope by MainScope(),
    Broadcasts.Observer {
    enum class Event {
        ServiceRecreated,
        ActivityStart,
        ActivityStop,
        ClashStop,
        ClashStart,
        ProfileLoaded,
        ProfileChanged
    }


    protected val uiStore by lazy { UiStore(this) }
    protected val events = Channel<Event>(Channel.UNLIMITED)
    protected var activityStarted: Boolean = false
    protected val clashRunning: Boolean
        get() = Remote.broadcasts.clashRunning
    protected var design: D? = null
        private set(value) {
            field = value

            if (value != null) {
                setContentView(value.root)
            } else {
                setContentView(View(this))
            }
        }

    private var defer: suspend () -> Unit = {}
    private var deferRunning = false
    private val nextRequestKey = AtomicInteger(0)
    private var dayNight: DayNight = DayNight.Day

    protected abstract suspend fun main()

    fun defer(operation: suspend () -> Unit) {
        this.defer = operation
    }

    suspend fun <I, O> startActivityForResult(
        contracts: ActivityResultContract<I, O>,
        input: I
    ): O = withContext(Dispatchers.Main) {
        val requestKey = nextRequestKey.getAndIncrement().toString()

        ActivityResultLifecycle().use { lifecycle, start ->
            suspendCoroutine { c ->
                activityResultRegistry.register(requestKey, lifecycle, contracts) {
                    c.resumeWith(Result.success(it))
                }.apply { start() }.launch(input)
            }
        }
    }

    suspend fun setContentDesign(design: D) {
        suspendCoroutine<Unit> {
            window.decorView.post {
                this.design = design

                it.resume(Unit)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DynamicColors.applyToActivitiesIfAvailable(this.application)

        applyDayNight()

        launch {
            main()

            finish()
        }
    }

    override fun onStart() {
        super.onStart()

        activityStarted = true

        Remote.broadcasts.addObserver(this)

        events.trySend(Event.ActivityStart)
    }

    override fun onStop() {
        super.onStop()

        activityStarted = false

        Remote.broadcasts.removeObserver(this)

        events.trySend(Event.ActivityStop)
    }

    override fun onDestroy() {
        design?.cancel()

        cancel()

        super.onDestroy()
    }

    override fun finish() {
        if (deferRunning) {
            return
        }

        deferRunning = true

        launch {
            try {
                defer()
            } finally {
                withContext(NonCancellable) {
                    super.finish()
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (queryDayNight(newConfig) != dayNight) {
            ApplicationObserver.createdActivities.forEach {
                it.recreate()
            }
        }
    }

    open fun shouldDisplayHomeAsUpEnabled(): Boolean {
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        this.onBackPressed()

        return true
    }

    override fun onProfileChanged() {
        events.trySend(Event.ProfileChanged)
    }

    override fun onProfileLoaded() {
        events.trySend(Event.ProfileLoaded)
    }

    override fun onServiceRecreated() {
        events.trySend(Event.ServiceRecreated)
    }

    override fun onStarted() {
        events.trySend(Event.ClashStart)
    }

    override fun onStopped(cause: String?) {
        events.trySend(Event.ClashStop)

        if (cause != null && activityStarted) {
            launch {
                design?.showExceptionToast(ClashException(cause))
            }
        }
    }

    private fun queryDayNight(config: Configuration = resources.configuration): DayNight {
        return when (uiStore.darkMode) {
            DarkMode.Auto -> {
                if (config.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
                    DayNight.Night
                else
                    DayNight.Day
            }
            DarkMode.ForceLight -> {
                DayNight.Day
            }
            DarkMode.ForceDark -> {
                DayNight.Night
            }
        }
    }

    private fun applyDayNight(config: Configuration = resources.configuration) {
        val dayNight = queryDayNight(config)

        when (dayNight) {
            DayNight.Night -> {
                theme.applyStyle(R.style.AppThemeDark, true)
            }
            DayNight.Day -> {
                theme.applyStyle(R.style.AppThemeLight, true)
            }
        }

        window.isAllowForceDarkCompat = false
        window.isSystemBarsTranslucentCompat = true

        window.statusBarColor = resolveThemedColor(android.R.attr.statusBarColor)
        window.navigationBarColor = resolveThemedColor(android.R.attr.navigationBarColor)

        if (Build.VERSION.SDK_INT >= 23) {
            window.isLightStatusBarsCompat =
                resolveThemedBoolean(android.R.attr.windowLightStatusBar)
        }

        if (Build.VERSION.SDK_INT >= 27) {
            window.isLightNavigationBarCompat =
                resolveThemedBoolean(android.R.attr.windowLightNavigationBar)
        }

        this.dayNight = dayNight
    }
}
