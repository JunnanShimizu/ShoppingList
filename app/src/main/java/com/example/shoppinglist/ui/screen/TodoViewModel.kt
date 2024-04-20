package com.example.shoppinglist.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.shoppinglist.data.ShoppingDAO
import com.example.shoppinglist.data.ShoppingItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    val shoppingDAO: ShoppingDAO
) : ViewModel() {

    fun getAllToDoList(): Flow<List<ShoppingItem>> {
        return shoppingDAO.getAllShoppings()
    }

    suspend fun getAllShoppingNum(): Int {
        return shoppingDAO.getShoppingsNum()
    }

    suspend fun getImportantShoppingNum(): Int {
        return shoppingDAO.getImportantShoppingsNum()
    }

    fun addShoppingList(shoppingItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingDAO.insert(shoppingItem)
        }
    }


    fun removeShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingDAO.delete(shoppingItem)
        }
    }

    fun editShoppingItem(editedShopping: ShoppingItem) {
        viewModelScope.launch {
            shoppingDAO.update(editedShopping)
        }
    }

    fun changeShoppingState(shoppingItem: ShoppingItem, value: Boolean) {
        val updatedShopping = shoppingItem.copy()
        updatedShopping.isDone = value
        viewModelScope.launch {
            shoppingDAO.update(updatedShopping)
        }
    }

    fun clearAllShoppings() {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingDAO.deleteAllShoppings()
        }
    }

}