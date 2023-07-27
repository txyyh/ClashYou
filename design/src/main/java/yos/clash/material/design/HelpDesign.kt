package yos.clash.material.design

import android.content.Context
import android.net.Uri
import android.view.View
import yos.clash.material.common.compat.preferredLocale
import yos.clash.material.design.databinding.DesignSettingsCommonBinding
import yos.clash.material.design.preference.category
import yos.clash.material.design.preference.clickable
import yos.clash.material.design.preference.preferenceScreen
import yos.clash.material.design.preference.tips
import yos.clash.material.design.util.applyFrom
import yos.clash.material.design.util.bindAppBarElevation
import yos.clash.material.design.util.layoutInflater
import yos.clash.material.design.util.root

class HelpDesign(
    context: Context,
    openLink: (Uri) -> Unit,
) : Design<Unit>(context) {
    private val binding = DesignSettingsCommonBinding
        .inflate(context.layoutInflater, context.root, false)

    override val root: View
        get() = binding.root

    init {
        binding.surface = surface

        binding.activityBarLayout.applyFrom(context)

        binding.scrollRoot.bindAppBarElevation(binding.activityBarLayout)

        val screen = preferenceScreen(context) {
            tips(R.string.tips_help)

            clickable(
                title = R.string.application_name,
                summary = R.string.github_url_now
            ) {
                clicked {
                    openLink(Uri.parse(context.getString(R.string.github_url_now)))
                }
            }
            category(R.string.document)

            clickable(
                title = R.string.clash_wiki,
                summary = R.string.clash_wiki_url
            ) {
                clicked {
                    openLink(Uri.parse(context.getString(R.string.clash_wiki_url)))
                }
            }

            category(R.string.feedback)

            /*if (YosConfigAchieve.getIfPremium()) {
                clickable(
                    title = R.string.google_play,
                    summary = R.string.google_play_url
                ) {
                    clicked {
                        openLink(Uri.parse(context.getString(R.string.google_play_url)))
                    }
                }
            }*/

            clickable(
                title = R.string.github_issues,
                summary = R.string.github_issues_url
            ) {
                clicked {
                    openLink(Uri.parse(context.getString(R.string.github_issues_url)))
                }
            }

                category(R.string.sources)

                clickable(
                    title = R.string.clash_for_android_show,
                    summary = R.string.github_url
                ) {
                    clicked {
                        openLink(Uri.parse(context.getString(R.string.github_url)))
                    }
                }

            clickable(
                title = R.string.clash_core,
                summary = R.string.clash_core_url
            ) {
                clicked {
                    openLink(Uri.parse(context.getString(R.string.clash_core_url)))
                }
            }
            category(R.string.donate)
            clickable(
                title = R.string.developer_now,
                summary = R.string.donate_url_now
            ) {
                clicked {
                    openLink(Uri.parse(context.getString(R.string.donate_url_now)))
                }
            }
            if (context.resources.configuration.preferredLocale.language == "zh") {
                clickable(
                    title = R.string.developer_before,
                    summary = R.string.donate_url
                ) {
                    clicked {
                        openLink(Uri.parse(context.getString(R.string.donate_url)))
                    }
                }
            }
        }

        binding.content.addView(screen.root)
    }
}