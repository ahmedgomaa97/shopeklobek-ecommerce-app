package com.stash.shopeklobek.ui

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.stash.shopeklobek.model.shareprefrances.SettingsPreferences
import com.stash.shopeklobek.model.api.ShopifyApi
import com.stash.shopeklobek.model.utils.Either
import com.stash.shopeklobek.model.utils.RepoErrors
import com.stash.shopeklobek.model.entities.CustomerModel
import com.stash.shopeklobek.model.repositories.AuthenticationRepo
import com.stash.shopeklobek.model.repositories.ProductRepo
import com.stash.shopeklobek.utils.ViewHelpers
import kotlinx.coroutines.launch

class MainViewModel(val app: Application, val productRepo: ProductRepo) : AndroidViewModel(app) {

    fun updateLanguage() {
        ViewHelpers.setAppLocale(
            ViewHelpers.returnByLanguage(productRepo.getSettings().language,"ar","en"), app.resources
        )
    }

    class Factory(private val application: Application,val productRepo: ProductRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(application,productRepo) as T
        }
    }

    companion object{
        fun create(context: MainActivity):MainViewModel{
            return ViewModelProvider(
                context,
                Factory(
                    context.application,
                    ProductRepo(
                        ShopifyApi.api,
                        SettingsPreferences(context.application),
                        context.application
                    )
                )
            )[MainViewModel::class.java]
        }
    }

}