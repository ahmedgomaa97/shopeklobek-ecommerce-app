package com.stash.shopeklobek.ui.checkout

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.stash.shopeklobek.model.shareprefrances.SettingsPreferences
import com.stash.shopeklobek.model.api.ApiService
import com.stash.shopeklobek.model.repositories.ProductRepo

class CheckoutViewModel(application: Application, val productRepo: ProductRepo) : AndroidViewModel(application) {

    val pageLiveData = MutableLiveData<Int>(0);

    class Factory(private val application: Application,val productRepo: ProductRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CheckoutViewModel(application,productRepo) as T
        }
    }

    companion object{
        fun create(context: CheckoutActivity):CheckoutViewModel{
            return ViewModelProvider(
                context,
                Factory(
                    context.applicationContext as Application,
                    ProductRepo(ApiService.api, SettingsPreferences(context.applicationContext as Application)
                        ,context.applicationContext as Application)
                )
            )[CheckoutViewModel::class.java]
        }
    }
}