package yos.clash.material.design

import android.app.ProgressDialog.show
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.github.kr328.clash.core.model.TunnelState
import com.github.kr328.clash.core.util.trafficTotal
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hjq.permissions.XXPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import yos.clash.material.design.databinding.DesignAboutBinding
import yos.clash.material.design.databinding.DesignMainBinding
import yos.clash.material.design.util.layoutInflater
import yos.clash.material.design.util.resolveThemedColor
import yos.clash.material.design.util.root
import java.security.Permission


class MainDesign(context: Context) : Design<MainDesign.Request>(context) {
    enum class Request {
        ToggleStatus,
        OpenProxy,
        OpenProfiles,
        OpenProviders,
        OpenLogs,
        OpenSettings,
        OpenHelp,
        OpenAbout,
    }

    private val binding = DesignMainBinding
        .inflate(context.layoutInflater, context.root, false)

    override val root: View
        get() = binding.root

    suspend fun setProfileName(name: String?) {
        withContext(Dispatchers.Main) {
            binding.profileName = name
        }
    }

    suspend fun setClashRunning(running: Boolean) {
        withContext(Dispatchers.Main) {
            binding.clashRunning = running
        }
    }

    suspend fun setForwarded(value: Long) {
        withContext(Dispatchers.Main) {
            binding.forwarded = value.trafficTotal()
        }
    }

    suspend fun setMode(mode: TunnelState.Mode) {
        withContext(Dispatchers.Main) {
            binding.mode = when (mode) {
                TunnelState.Mode.Direct -> context.getString(R.string.direct_mode)
                TunnelState.Mode.Global -> context.getString(R.string.global_mode)
                TunnelState.Mode.Rule -> context.getString(R.string.rule_mode)
                TunnelState.Mode.Script -> context.getString(R.string.script_mode)
            }
        }
    }

    suspend fun setHasProviders(has: Boolean) {
        withContext(Dispatchers.Main) {
            binding.hasProviders = has
        }
    }

    suspend fun showAbout(versionName: String) {
        withContext(Dispatchers.Main) {
            val binding = DesignAboutBinding.inflate(context.layoutInflater).apply {
                this.versionName = versionName
            }

            val alertDialog = AlertDialog.Builder(context).create()
            val window = alertDialog.window
            window!!.setBackgroundDrawable(BitmapDrawable())
            window.decorView.setPadding(25, 0, 25, 0);
            alertDialog.setView(binding.root)
            alertDialog.show()
        }
    }

    suspend fun showUpdatedTips() {
        withContext(Dispatchers.Main) {
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.version_updated)
                .setMessage(R.string.version_updated_tips)
                .setPositiveButton(R.string.ok) { _, _ -> }
                .show()
        }
    }

    suspend fun showPermissionRequest() {
        withContext(Dispatchers.Main) {
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.permission_request_title)
                .setMessage(R.string.permission_notification_desc)
                .setPositiveButton(R.string.permission_request_positive) { _, _ ->
                    XXPermissions.startPermissionActivity(
                        context,
                        com.hjq.permissions.Permission.POST_NOTIFICATIONS
                    )
                }
                .setNegativeButton(R.string.permission_request_negative) { _, _ -> }
                .show()
        }
    }

    init {
        binding.self = this

        binding.colorClashStarted = context.resolveThemedColor(R.attr.colorPrimary)
        binding.colorClashStopped = context.resolveThemedColor(R.attr.colorClashStopped)
    }

    fun request(request: Request) {
        requests.trySend(request)
    }
}