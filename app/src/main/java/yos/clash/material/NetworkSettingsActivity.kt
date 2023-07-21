package yos.clash.material

import yos.clash.material.common.util.intent
import yos.clash.material.design.NetworkSettingsDesign
import yos.clash.material.service.store.ServiceStore
import kotlinx.coroutines.isActive
import kotlinx.coroutines.selects.select

class NetworkSettingsActivity : BaseActivity<NetworkSettingsDesign>() {
    override suspend fun main() {
        val design = NetworkSettingsDesign(
            this,
            uiStore,
            ServiceStore(this),
            clashRunning,
        )

        setContentDesign(design)

        while (isActive) {
            select<Unit> {
                events.onReceive {
                    when (it) {
                        Event.ClashStart, Event.ClashStop, Event.ServiceRecreated ->
                            recreate()
                        else -> Unit
                    }
                }
                design.requests.onReceive {
                    when (it) {
                        NetworkSettingsDesign.Request.StartAccessControlList ->
                            startActivity(AccessControlActivity::class.intent)
                    }
                }
            }
        }
    }

}
