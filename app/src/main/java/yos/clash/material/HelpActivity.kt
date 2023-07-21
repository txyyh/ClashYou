package yos.clash.material

import android.content.Intent
import yos.clash.material.design.HelpDesign
import kotlinx.coroutines.isActive

class HelpActivity : BaseActivity<HelpDesign>() {
    override suspend fun main() {
        val design = HelpDesign(this) {
            startActivity(Intent(Intent.ACTION_VIEW).setData(it))
        }

        setContentDesign(design)

        while (isActive) {
            events.receive()
        }
    }
}