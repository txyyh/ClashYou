package yos.clash.material.service.remote

import com.github.kr328.kaidl.BinderInterface

@BinderInterface
interface IRemoteService {
    fun clash(): IClashManager
    fun profile(): IProfileManager
}