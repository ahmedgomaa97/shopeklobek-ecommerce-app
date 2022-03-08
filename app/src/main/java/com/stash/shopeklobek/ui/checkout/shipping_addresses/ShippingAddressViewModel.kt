package com.stash.shopeklobek.ui.checkout.shipping_addresses

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.stash.shopeklobek.model.shareprefrances.SettingsPreferences
import com.stash.shopeklobek.model.api.ShopifyApi
import com.stash.shopeklobek.model.entities.Customer
import com.stash.shopeklobek.model.repositories.ProductRepo
import com.stash.shopeklobek.model.utils.Either
import kotlinx.coroutines.launch

class ShippingAddressViewModel(application: Application, val productRepo: ProductRepo) : AndroidViewModel(application) {

    val addressesLiveData= MutableLiveData<Customer>()

    fun getCustomerShippingAddresses() = viewModelScope.launch {
        when(val productRepo = productRepo.getAddress()){
            is Either.Error -> {

            }
            is Either.Success -> addressesLiveData.postValue(productRepo.data!!)
        }
    }



    class Factory(private val application: Application,val productRepo: ProductRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ShippingAddressViewModel(application,productRepo) as T
        }
    }

    companion object{
        fun create(context: Fragment):ShippingAddressViewModel{
            return ViewModelProvider(
                context,
                Factory(
                    context.context?.applicationContext as Application,
                    ProductRepo(ShopifyApi.api, SettingsPreferences.getInstance(context.context?.applicationContext as Application)
                        ,context.context?.applicationContext as Application)
                )
            )[ShippingAddressViewModel::class.java]
        }
    }
}