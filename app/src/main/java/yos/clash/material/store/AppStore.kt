package yos.clash.material.store

import android.content.Context
import yos.clash.material.common.store.Store
import yos.clash.material.common.store.asStoreProvider

class AppStore(context: Context) {
    private val store = Store(
        context
            .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
            .asStoreProvider()
    )

    var updatedAt: Long by store.long(
        key = "updated_at",
        defaultValue = -1,
    )

    companion object {
        private const val FILE_NAME = "app"
    }
}