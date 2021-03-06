package com.stash.shopeklobek.model.entities.room

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stash.shopeklobek.model.entities.*
import com.stash.shopeklobek.utils.Constants.CART_TABLE
import kotlinx.parcelize.Parcelize


@Entity(
    tableName = CART_TABLE
)

@Parcelize
data class RoomCart(
    @PrimaryKey val id: Long,
    val customerEmail:String,
    val product:Products,
    val variantId:Long?=null,
    var count:Int =1,
): Parcelable {
    fun variant():Variants?{
        return if(variantId==null){
            product.variants.first()
        }else{
            product.variants.first{
                it?.id == variantId
            }
        }
    }

    fun getTotal():String{
        return ((variant()?.price?.toDouble()?:0.0)  * count).toString()
    }

}

