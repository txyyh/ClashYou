package yos.clash.material.service

import android.content.Context
import android.content.SharedPreferences
import yos.clash.material.common.constants.Authorities
import rikka.preference.MultiProcessPreference
import rikka.preference.PreferenceProvider

class PreferenceProvider : PreferenceProvider() {
    override fun onCreatePreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        private const val FILE_NAME = "service"

        fun createSharedPreferencesFromContext(context: Context): SharedPreferences {
            return when (context) {
                is BaseService, is TunService ->
                    context.getSharedPreferences(
                        FILE_NAME,
                        Context.MODE_PRIVATE
                    )
                else ->
                    MultiProcessPreference(
                        context,
                        Authorities.SETTINGS_PROVIDER
                    )
            }
        }
    }
}