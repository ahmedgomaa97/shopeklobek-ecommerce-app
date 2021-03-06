package com.stash.shopeklobek.ui

import android.app.Activity
import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import com.stash.shopeklobek.BuildConfig
import com.stash.shopeklobek.model.shareprefrances.SettingsPreferences
import com.stash.shopeklobek.model.api.ShopifyApi
import com.stash.shopeklobek.model.utils.Either
import com.stash.shopeklobek.model.utils.RepoErrors
import com.stash.shopeklobek.model.entities.CustomerModel
import com.stash.shopeklobek.model.repositories.AuthenticationRepo
import com.stash.shopeklobek.model.repositories.ProductRepo
import com.stash.shopeklobek.utils.Constants
import com.stash.shopeklobek.utils.ViewHelpers
import kotlinx.coroutines.launch

class MainViewModel(val app: Application, val productRepo: ProductRepo) : AndroidViewModel(app) {

    val settingsLiveData = productRepo.getSettingsLiveData()


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
                        SettingsPreferences.getInstance(context.application),
                        context.application
                    )
                )
            )[MainViewModel::class.java]
        }
    }

}