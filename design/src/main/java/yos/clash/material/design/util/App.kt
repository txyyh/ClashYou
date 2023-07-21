package yos.clash.material.design.util

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import yos.clash.material.common.compat.foreground
import yos.clash.material.design.model.AppInfo

fun PackageInfo.toAppInfo(pm: PackageManager): AppInfo {
    return AppInfo(
        packageName = packageName,
        icon = applicationInfo.loadIcon(pm).foreground(),
        label = applicationInfo.loadLabel(pm).toString(),
        installTime = firstInstallTime,
        updateDate = lastUpdateTime,
    )
}
