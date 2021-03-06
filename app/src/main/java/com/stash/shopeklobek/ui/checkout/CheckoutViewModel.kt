package com.stash.shopeklobek.ui.checkout

import android.app.Application
import androidx.lifecycle.*
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.order.*
import com.stash.shopeklobek.model.api.ShopifyApi
import com.stash.shopeklobek.model.entities.Address
import com.stash.shopeklobek.model.entities.PriceRule
import com.stash.shopeklobek.model.entities.Transactions
import com.stash.shopeklobek.model.entities.retroOrder.FinancialStatus
import com.stash.shopeklobek.model.entities.retroOrder.SendedOrder
import com.stash.shopeklobek.model.entities.room.RoomCart
import com.stash.shopeklobek.model.repositories.ProductRepo
import com.stash.shopeklobek.model.shareprefrances.SettingsPreferences
import com.stash.shopeklobek.model.utils.Either
import com.stash.shopeklobek.model.utils.RepoErrors
import com.stash.shopeklobek.model.utils.RoomAddOrderErrors
import com.stash.shopeklobek.utils.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import com.paypal.checkout.order.Order as paypalOrder

enum class PaymentMethodsEnum {
    Cash, Paypal
}

class CheckoutViewModel(val app: Application, val productRepo: ProductRepo) :
    AndroidViewModel(app) {

    var cartProducts: List<RoomCart> = emptyList()
    var discount: PriceRule? = null
    var selectedAddress: Address? = null
    var shipping = 5.0
    var selectedPaymentMethods: PaymentMethodsEnum = PaymentMethodsEnum.Cash
    var fixedDiscountLiveData = MutableLiveData<Either<PriceRule, RepoErrors>?>(null)


    fun addDiscount(discountCode: String) = viewModelScope.launch {
        fixedDiscountLiveData.value = productRepo.getDiscount(discountCode).also {
            discount = when (it) {
                is Either.Error -> null
                is Either.Success -> it.data
            }
        }
    }

    fun removeDiscount() {
        discount = null
        fixedDiscountLiveData.postValue(null)
    }

    suspend fun confirm(): Either<Unit, RepoErrors> {
        val order = SendedOrder(
            transactions = listOf(
                Transactions(
                    kind = "sale",
                    status = "success",
                    amount = getTotalPrice()
                )
            ),

            billingAddress = selectedAddress?.toBillingShippingAddress(),
            shippingAddress = selectedAddress?.toBillingShippingAddress(),
            discountCodes = discount?.toDiscountCodes() ?: listOf(),
            lineItems = cartProducts.toLineItem(),
        )


        return when (selectedPaymentMethods) {
            PaymentMethodsEnum.Cash -> {
                productRepo.addOrder(order.copy(financialStatus = FinancialStatus.Voided.toString()))
            }
            PaymentMethodsEnum.Paypal -> {
                productRepo.addOrder(order.copy(financialStatus = FinancialStatus.Paid.toString()))
            }
        }
    }



    fun getTotalPrice() =
        ((discount?.value?.toDouble()) ?: 0.0) + cartProducts.getPrice() + shipping


    fun startCheck() {
        PayPalCheckout.startCheckout(
            CreateOrder { createOrderActions ->
                val order = paypalOrder(
                    intent = OrderIntent.CAPTURE,
                    appContext = AppContext(
                        userAction = UserAction.PAY_NOW
                    ),
                    purchaseUnitList = listOf(
                        PurchaseUnit(
                            amount = Amount(
                                currencyCode = CurrencyCode.USD,
                                value = getTotalPrice().toString(),
                                breakdown = BreakDown(
                                    itemTotal = UnitAmount(
                                        currencyCode = CurrencyCode.USD,
                                        value = cartProducts.getPrice().toString(),
                                    ),
                                    shipping = UnitAmount(
                                        currencyCode = CurrencyCode.USD,
                                        value = shipping.toString(),
                                    ),
                                    discount = discount?.let {
                                        UnitAmount(
                                            currencyCode = CurrencyCode.USD,
                                            value = (it.value?.toDouble()?.absoluteValue.toString()) ?: "0",
                                        )
                                    }
                                )
                            ),

                            items = cartProducts.toItems(),

                            shipping = Shipping(
                                address = selectedAddress?.toPaypalAddress()
                            )

                        )
                    ),
                )
                createOrderActions.create(order)
            }
        )
    }


    init {
        productRepo.getCart().observeOnce {
            cartProducts = it
        }
    }

    val loadingLiveData = MutableLiveData(false)

    val pageIndexLiveData = MutableLiveData(0)

    val products: LiveData<List<RoomCart>> by lazy {
        productRepo.getCart()
    }

    private suspend fun <T> loadingBloc(func: suspend () -> T): T {
        var res: T? = null
        loadingLiveData.value = true
        res = func()
        loadingLiveData.value = false
        return res
    }

    class Factory(private val application: Application, val productRepo: ProductRepo) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CheckoutViewModel(application, productRepo) as T
        }
    }

    companion object {
        fun create(context: CheckoutActivity): CheckoutViewModel {
            return ViewModelProvider(
                context,
                Factory(
                    context.applicationContext as Application,
                    ProductRepo(
                        ShopifyApi.api,
                        SettingsPreferences.getInstance(context.applicationContext as Application),
                        context.applicationContext as Application
                    )
                )
            )[CheckoutViewModel::class.java]
        }
    }

}