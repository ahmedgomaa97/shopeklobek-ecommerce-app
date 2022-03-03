package com.stash.shopeklobek.ui.splash

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.stash.shopeklobek.model.shareprefrances.SettingsPreferences
import com.stash.shopeklobek.model.api.ApiService
import com.stash.shopeklobek.model.repositories.ProductRepo
import kotlinx.coroutines.launch

class SplashViewModel(application: Application,val productRepo: ProductRepo) : AndroidViewModel(application) {


    init {
        viewModelScope.launch {
            println(productRepo.updateCurrency())
        }
    }



    class Factory(private val application: Application,val productRepo: ProductRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashViewModel(application,productRepo) as T
        }
    }

    companion object{
        fun create(context: SplashFragment):SplashViewModel{
            return ViewModelProvider(
                context,
                Factory(
                    context.application ,
                    ProductRepo(ApiService.api, SettingsPreferences(context.application)
                        ,context.application)
                )
            )[SplashViewModel::class.java]
        }
    }
}