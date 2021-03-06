package com.stash.shopeklobek.model.repositories

import android.app.Application
import com.stash.shopeklobek.model.api.ShopifyApi.api
import com.stash.shopeklobek.model.entities.CustomerLoginModel
import com.stash.shopeklobek.model.entities.CustomerModel
import com.stash.shopeklobek.model.interfaces.ShopifyServices
import com.stash.shopeklobek.model.shareprefrances.SettingsPreferences
import com.stash.shopeklobek.model.utils.Either
import com.stash.shopeklobek.model.utils.LoginErrors
import com.stash.shopeklobek.model.utils.SignUpErrors
import com.stash.shopeklobek.utils.NetworkingHelper

class AuthenticationRepo(
    val ShopifyServices: ShopifyServices,
    val settingsPreferences: SettingsPreferences,
    val application: Application
) {


    suspend fun signUp(customer: CustomerModel): Either<CustomerModel, SignUpErrors> {
        return try {
            return if (NetworkingHelper.hasInternet(application.applicationContext)) {
                val res = api.register(customer)
                if (res.isSuccessful) {

                    settingsPreferences.update {
                        it.copy(
                            customer = res.body()?.customer
                        )
                    }

                    Either.Success(res.body()!!)
                } else {
                    if (res.code() == 422) {
                        Either.Error(SignUpErrors.EmailAlreadyExist, res.message())
                    } else {
                        Either.Error(SignUpErrors.ServerError, res.message())
                    }
                }

            } else
                Either.Error(SignUpErrors.NoInternetConnection, "NoInternetConnection")

        } catch (t: Throwable) {
            Either.Error(SignUpErrors.ServerError, t.message)
        }
    }

    suspend fun login(email: String, pass: String): Either<CustomerLoginModel, LoginErrors> {
        return try {
            if (NetworkingHelper.hasInternet(application.applicationContext)) {
                val res = api.login()
                if (res.isSuccessful) {
                    val customer = res.body()?.customer?.first {
                        it?.email.equals(email)
                    } ?: return Either.Error(LoginErrors.CustomerNotFound, "CustomerNotFound")
                    if (customer.lastName.equals(pass)) {
                        settingsPreferences.update {
                            it.copy(
                                customer = customer
                            )
                        }
                    } else return Either.Error(
                        LoginErrors.IncorrectEmailOrPassword,
                        "Please enter correct email or password"
                    )


                    return Either.Success(res.body()!!)
                } else
                    return Either.Error(LoginErrors.ServerError, res.message())
            } else
                return Either.Error(LoginErrors.NoInternetConnection, "NoInternetConnection")

        } catch (t: Throwable) {
            Either.Error(LoginErrors.ServerError, t.message)
        }
    }


}