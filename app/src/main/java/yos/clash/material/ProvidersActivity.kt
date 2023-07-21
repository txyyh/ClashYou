package yos.clash.material

import yos.clash.material.R
import yos.clash.material.common.util.intent
import yos.clash.material.common.util.ticker
import yos.clash.material.design.ProvidersDesign
import yos.clash.material.design.util.showExceptionToast
import yos.clash.material.util.withClash
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import java.util.concurrent.TimeUnit

class ProvidersActivity : BaseActivity<ProvidersDesign>() {
    override suspend fun main() {
        val providers = withClash { queryProviders().sorted() }
        val design = ProvidersDesign(this, providers)

        setContentDesign(design)

        val ticker = ticker(TimeUnit.MINUTES.toMillis(1))

        while (isActive) {
            select<Unit> {
                events.onReceive {
                    when (it) {
                        Event.ProfileLoaded -> {
                            val newList = withClash { queryProviders().sorted() }

                            if (newList != providers) {
                                startActivity(ProvidersActivity::class.intent)

                                finish()
                            }
                        }
                        else -> Unit
                    }
                }
                design.requests.onReceive {
                    when (it) {
                        is ProvidersDesign.Request.Update -> {
                            launch {
                                try {
                                    withClash {
                                        updateProvider(it.provider.type, it.provider.name)
                                    }

                                    design.notifyChanged(it.index)
                                } catch (e: Exception) {
                                    design.showExceptionToast(
                                        getString(
                                            R.string.format_update_provider_failure,
                                            it.provider.name,
                                            e.message
                                        )
                                    )

                                    design.notifyUpdated(it.index)
                                }
                            }
                        }
                    }
                }
                if (activityStarted) {
                    ticker.onReceive {
                        design.updateElapsed()
                    }
                }
            }
        }
    }
}