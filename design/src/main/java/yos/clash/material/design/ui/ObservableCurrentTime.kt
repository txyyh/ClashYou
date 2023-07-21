package yos.clash.material.design.ui

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR

class ObservableCurrentTime : BaseObservable() {
    var value: Long = System.currentTimeMillis()
        @Bindable get
        private set(value) {
            field = value

            notifyPropertyChanged(BR.value)
        }

    fun update() {
        value = System.currentTimeMillis()
    }
}