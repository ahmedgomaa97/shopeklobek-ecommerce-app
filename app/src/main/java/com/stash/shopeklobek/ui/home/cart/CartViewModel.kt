package com.stash.shopeklobek.ui.home.cart

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.stash.shopeklobek.model.api.ShopifyApi
import com.stash.shopeklobek.model.entities.Customer
import com.stash.shopeklobek.model.entities.currencies.Currency
import com.stash.shopeklobek.model.entities.room.RoomCart
import com.stash.shopeklobek.model.repositories.ProductRepo
import com.stash.shopeklobek.model.shareprefrances.SettingsPreferences
import kotlinx.coroutines.launch

class CartViewModel(application: Application, val productRepo: ProductRepo) : AndroidViewModel(application) {


    val settingsLiveData by lazy {
        productRepo.getSettingsLiveData()
    }

    var oldCustomer:Customer? = null
    var oldCurrency:Currency? = null



    var cartLiveData:LiveData<List<RoomCart>> = MutableLiveData(emptyList())
    var liveData:LiveData<List<RoomCart>>?=null

    fun getCartProductsEither() {
        cartLiveData = productRepo.getCart()
    }



    fun isNeedToRefresh(customer: Customer?):Boolean{
        return ((oldCustomer==null||oldCustomer!=customer)).also {
            oldCustomer = customer
        }
    }

    fun isNeedToRebuild(currency: Currency?):Boolean{
        return ((oldCurrency==null||oldCurrency!=currency)).also {
            oldCurrency = currency
        }
    }

    fun deleteCartProduct(id: Long) = productRepo.deleteFromCart(id)

    fun addCartProduct(roomCart: RoomCart) = viewModelScope.launch {
        productRepo.addToCart(roomCart.product, roomCart.variantId)
    }

    suspend fun updateCartProduct(roomCart: RoomCart) = productRepo.updateProductCart(roomCart)


    class Factory(private val application: Application, val productRepo: ProductRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CartViewModel(application, productRepo) as T
        }
    }

    companion object {
        fun create(context: Fragment): CartViewModel {
            val application = context.context?.applicationContext as Application
            return ViewModelProvider(
                context,
                Factory(
                    application,
                    ProductRepo(
                        ShopifyApi.api,
                        SettingsPreferences.getInstance(application),
                        application
                    )
                )
            )[CartViewModel::class.java]
        }
    }
}