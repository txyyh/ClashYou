package yos.clash.material.service.util

import android.net.ConnectivityManager
import android.net.Network

fun ConnectivityManager.resolvePrimaryDns(network: Network?): String? {
    val properties = getLinkProperties(network) ?: return null

    return properties.dnsServers.firstOrNull()?.asSocketAddressText(53)
}
