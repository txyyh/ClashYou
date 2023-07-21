package yos.clash.material

import android.content.Intent
import android.net.Uri
import yos.clash.material.design.ApkBrokenDesign
import kotlinx.coroutines.isActive

class ApkBrokenActivity : BaseActivity<ApkBrokenDesign>() {
    override suspend fun main() {
        val design = ApkBrokenDesign(this)

        setContentDesign(design)

        while (isActive) {
            val req = design.requests.receive()

            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(req.url)))
        }
    }
}