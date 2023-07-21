package yos.clash.material

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import yos.clash.material.R
import yos.clash.material.common.util.intent
import yos.clash.material.common.util.setUUID
import yos.clash.material.service.model.Profile
import yos.clash.material.util.withProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

class ExternalImportActivity : Activity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.action != Intent.ACTION_VIEW)
            return finish()

        val uri = intent.data ?: return finish()
        val url = uri.getQueryParameter("url") ?: return finish()

        launch {
            val uuid = withProfile {
                val type = when (uri.getQueryParameter("type")?.lowercase(Locale.getDefault())) {
                    "url" -> Profile.Type.Url
                    "file" -> Profile.Type.File
                    else -> Profile.Type.Url
                }
                val name = uri.getQueryParameter("name") ?: getString(R.string.new_profile)

                create(type, name).also {
                    patch(it, name, url, 0)
                }
            }

            startActivity(PropertiesActivity::class.intent.setUUID(uuid))

            finish()
        }
    }
}