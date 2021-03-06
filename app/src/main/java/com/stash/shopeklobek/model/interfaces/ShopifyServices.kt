package com.stash.shopeklobek.model.interfaces

import com.stash.shopeklobek.model.entities.*
import com.stash.shopeklobek.model.entities.retroOrder.*
import com.stash.shopeklobek.model.entities.retroOrder.SendedOrder
import retrofit2.Response
import retrofit2.http.*

interface ShopifyServices {

    @GET("smart_collections.json")
    suspend fun getSmartCollection(): Response<SmartCollectionModel>

    @GET("products.json?")
    suspend fun getProductsByVendor(@Query("vendor") vendor: String):
            Response<ProductsModel>

    @GET("custom_collections.json")
    suspend fun getMainCategories(): Response<MainCategories>

    @GET("products.json")
    suspend fun getAllProducts(): Response<ProductsModel>

    @GET("products.json?")
    suspend fun getProductsByGender(@Query("collection_id") collectionId: Long):
            Response<ProductsModel>

    @GET("products.json?")
    suspend fun getProductsFromType(@Query("collection_id") collectionId: Long,
                                    @Query("product_type") productType: String):
            Response<ProductsModel>

    @GET("price_rules.json")
    suspend fun getAllDiscounts():Response<DiscountModel>

    @GET("products/{productID}.json")
    suspend fun getProduct(@Path("productID") ProductId:Long ):
            Response<ProductModel>

    @POST("price_rules.json")
    suspend fun createDiscount(@Body priceRule:Discount):
            Response<Discount>


    @GET("price_rules/{id}.json")
    suspend fun getDiscount(@Path("id") discountId:Long ):
            Response<Discount>

    @POST("customers.json")
    suspend fun register(@Body customer: CustomerModel):
            Response<CustomerModel>

    @GET("customers.json")
    suspend fun login(): Response<CustomerLoginModel>


    @PUT("customers/{id}.json")
    suspend fun updateCustomer(@Path("id") customerId:Long,
                               @Body customer:EditCustomerModel):
            Response<EditCustomerModel>


    @POST("orders.json")
    suspend fun addOrder(@Body order: SendOrderModel):
            Response<GettingOrderModel>

    @GET("orders.json?")
    suspend fun getOrders(@Query("email") email: String):
            Response<OrdersModels>

    @POST("customers/{customer_id}/addresses.json")
    suspend fun addAddress(@Path("customer_id") customerId:Long,
        @Body address:AddressModel):
            Response<CustomerAddressModel>

    @DELETE("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun deleteAddress(@Path("customer_id") customerId:Long,
                              @Path("address_id") addressId:Long):
            Response<CustomerAddressModel>

    @PUT("customers/{customer_id}/addresses/{address_id}.json")
    suspend fun updateAddress(@Path("customer_id") customerId:Long,
                              @Path("address_id") addressId:Long,
                              @Body address:AddressModel):
                              Response<CustomerAddressModel>


    @PUT("customers/{customer_id}/addresses/{address_id}/default.json")
    suspend fun setDefault(@Path("customer_id") customerId:Long,
                              @Path("address_id") addressId:Long):
                              Response<CustomerAddressModel>

    @GET("customers/{customer_id}.json")
    suspend fun getAddress(@Path("customer_id") customerId:Long):
            Response<CustomerModel>

}