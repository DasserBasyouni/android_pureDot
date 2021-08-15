package com.g7.soft.pureDot

import android.content.Context
import androidx.multidex.MultiDex
import com.zeugmasolutions.localehelper.LocaleAwareApplication

// TODO: Migrate your existing splash screen implementation to Android 12
//  https://developer.android.com/about/versions/12/splash-screen-migration
class Application : LocaleAwareApplication() {

    companion object{
        const val isClientFlavour = BuildConfig.FLAVOR == "client"
        const val isShopOwnerFlavour = BuildConfig.FLAVOR == "shopOwner"
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}