package yos.clash.material.util

import android.content.Context
import android.content.Intent
import android.net.VpnService
import yos.clash.material.common.compat.startForegroundServiceCompat
import yos.clash.material.common.constants.Intents
import yos.clash.material.common.util.intent
import yos.clash.material.design.store.UiStore
import yos.clash.material.service.ClashService
import yos.clash.material.service.TunService
import yos.clash.material.service.util.sendBroadcastSelf

fun Context.startClashService(): Intent? {
    val startTun = UiStore(this).enableVpn

    if (startTun) {
        val vpnRequest = VpnService.prepare(this)
        if (vpnRequest != null)
            return vpnRequest

        startForegroundServiceCompat(TunService::class.intent)
    } else {
        startForegroundServiceCompat(ClashService::class.intent)
    }

    return null
}

fun Context.stopClashService() {
    sendBroadcastSelf(Intent(Intents.ACTION_CLASH_REQUEST_STOP))
}