package yos.clash.material.common.constants

import android.content.ComponentName
import yos.clash.material.common.util.packageName

object Components {
    private const val componentsPackageName = "yos.clash.material"

    val MAIN_ACTIVITY = ComponentName(packageName, "$componentsPackageName.MainActivity")
    val PROPERTIES_ACTIVITY = ComponentName(packageName, "$componentsPackageName.PropertiesActivity")
}