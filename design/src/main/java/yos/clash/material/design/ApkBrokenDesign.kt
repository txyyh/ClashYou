package yos.clash.material.design

import android.content.Context
import android.view.View
import yos.clash.material.design.databinding.DesignSettingsCommonBinding
import yos.clash.material.design.preference.category
import yos.clash.material.design.preference.clickable
import yos.clash.material.design.preference.preferenceScreen
import yos.clash.material.design.preference.tips
import yos.clash.material.design.util.applyFrom
import yos.clash.material.design.util.bindAppBarElevation
import yos.clash.material.design.util.layoutInflater
import yos.clash.material.design.util.root

class ApkBrokenDesign(context: Context) : Design<ApkBrokenDesign.Request>(context) {
    data class Request(val url: String)

    private val binding = DesignSettingsCommonBinding
        .inflate(context.layoutInflater, context.root, false)

    override val root: View
        get() = binding.root

    init {
        binding.surface = surface

        binding.activityBarLayout.applyFrom(context)

        binding.scrollRoot.bindAppBarElevation(binding.activityBarLayout)

        val screen = preferenceScreen(context) {
            tips(R.string.application_broken_tips)

            category(R.string.reinstall)

            clickable(
                title = R.string.google_play,
                summary = R.string.google_play_url
            ) {
                clicked {
                    requests.trySend(Request(context.getString(R.string.google_play_url)))
                }
            }

            clickable(
                title = R.string.github_releases,
                summary = R.string.github_releases_url
            ) {
                clicked {
                    requests.trySend(Request(context.getString(R.string.github_releases_url)))
                }
            }
        }

        binding.content.addView(screen.root)
    }
}