package com.stash.shopeklobek.ui.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.stash.shopeklobek.model.api.ShopifyApi
import com.stash.shopeklobek.model.repositories.ProductRepo
import com.stash.shopeklobek.model.shareprefrances.SettingsPreferences
import kotlinx.coroutines.launch

class SplashViewModel(val app: Application, val productRepo: ProductRepo) : AndroidViewModel(app) {

    var initItStart = false

    suspend fun updateCurrency() {
        productRepo.updateCurrency()
    }

    fun initIt(ini: suspend () -> Unit) {
        if (initItStart) return

        initItStart = true
        viewModelScope.launch {
            ini()
        }
    }


    class Factory(private val application: Application, val productRepo: ProductRepo) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashViewModel(application, productRepo) as T
        }
    }

    companion object {
        fun create(context: LauncherSActivity): SplashViewModel {
            return ViewModelProvider(
                context,
                Factory(
                    context.application,
                    ProductRepo(
                        ShopifyApi.api,
                        SettingsPreferences.getInstance(context.application),
                        context.application
                    )
                )
            )[SplashViewModel::class.java]
        }
    }
}