package yos.clash.material

import android.app.Application
import android.content.Context
import yos.clash.material.common.Global
import yos.clash.material.common.compat.currentProcessName
import yos.clash.material.common.log.Log
import yos.clash.material.remote.Remote
import yos.clash.material.service.util.sendServiceRecreated

@Suppress("unused")
class MainApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        Global.init(this)
    }

    override fun onCreate() {
        super.onCreate()

        val processName = currentProcessName

        Log.d("Process $processName started")

        if (processName == packageName) {
            Remote.launch()
        } else {
            sendServiceRecreated()
        }
    }

    fun finalize() {
        Global.destroy()
    }
}