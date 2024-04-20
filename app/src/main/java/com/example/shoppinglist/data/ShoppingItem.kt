package com.example.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shoppinglist.R
import java.io.Serializable

@Entity(tableName = "shoppingtable")
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") val title:String,
    @ColumnInfo(name = "cost") val cost:String,
    @ColumnInfo(name = "description") val description:String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "createDate") val createDate:String,
    @ColumnInfo(name = "priority") var priority:ShoppingPriority,
    @ColumnInfo(name = "isDone") var isDone: Boolean
) : Serializable

enum class ShoppingPriority {
    NORMAL, HIGH;

    fun getIcon(): Int {
        return if (this == NORMAL) R.drawable.normal else R.drawable.important
    }
}





