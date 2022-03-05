package com.stash.shopeklobek.model.room.daw

import androidx.lifecycle.LiveData
import androidx.room.*
import com.stash.shopeklobek.model.entities.room.RoomCart
import com.stash.shopeklobek.utils.Constants.CART_TABLE


@Dao
interface CartDao : BaseDao<RoomCart> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun upsert(item: RoomCart): Long

    @Query("Select * from $CART_TABLE")
    override fun getAll(): LiveData<List<RoomCart>>

    @Query("SELECT * FROM $CART_TABLE WHERE id=:id")
    override suspend fun getWithId(id: Long): RoomCart?

    @Query("DELETE FROM $CART_TABLE WHERE id = :id")
    override suspend fun delete(id: Long)

}