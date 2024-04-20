package com.example.shoppinglist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDAO {
    @Query("SELECT * from shoppingtable")
    fun getAllShoppings(): Flow<List<ShoppingItem>>

    @Query("SELECT * from shoppingtable WHERE id = :id")
    fun getShopping(id: Int): Flow<ShoppingItem>

    @Query("SELECT COUNT(*) from shoppingtable")
    suspend fun getShoppingsNum(): Int

    @Query("""SELECT COUNT(*) from shoppingtable WHERE priority="HIGH"""")
    suspend fun getImportantShoppingsNum(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(shopping: ShoppingItem)

    @Update
    suspend fun update(shopping: ShoppingItem)

    @Delete
    suspend fun delete(shopping: ShoppingItem)

    @Query("DELETE from shoppingtable")
    suspend fun deleteAllShoppings()
}