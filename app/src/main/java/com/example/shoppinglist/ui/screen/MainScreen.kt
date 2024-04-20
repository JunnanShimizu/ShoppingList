package com.example.shoppinglist.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.shoppinglist.data.ShoppingItem
import com.example.shoppinglist.data.ShoppingPriority
import java.util.Date
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppinglist.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    shoppingViewModel: ShoppingViewModel = hiltViewModel(),
    onNavigateToSummary: (Int, Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "ShoppingList")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = {
                        showAddDialog = true
                    }) {
                        Icon(Icons.Filled.Add, null)
                    }

                    IconButton(onClick = {
                        shoppingViewModel.clearAllShoppings()
                    }) {
                        Icon(Icons.Filled.Delete, null)
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            onNavigateToSummary(
                                shoppingViewModel.getAllShoppingNum(),
                                shoppingViewModel.getImportantShoppingNum()
                            )
                        }
                    }) {
                        Icon(Icons.Filled.Info, null)
                    }
                }
            )
        },
        content = {
            ShoppingListContent(
                Modifier.padding(it), shoppingViewModel, onNavigateToSummary
            )

            if (showAddDialog) {
                AddNewShoppingDialog(shoppingViewModel,
                    onDismissRequest = {
                        showAddDialog = false
                    })
            }
        }
    )
}

@Composable
fun ShoppingListContent(
    modifier: Modifier,
    shoppingViewModel: ShoppingViewModel,
    onNavigateToSummary: (Int, Int) -> Unit
) {
    val shoppingList by shoppingViewModel.getAllToDoList().collectAsState(emptyList())

    var shoppingToEdit: ShoppingItem? by rememberSaveable {
        mutableStateOf(null)
    }
    var showEditShoppingDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
    ) {
        // show ShoppingItems from the ViewModel in a LayzColumn
        if (shoppingList.isEmpty()) {
            Text(text = "No items")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(shoppingList) {
                    ShoppingCard(it,
                        onShoppingCheckChange = { checkValue ->
                            shoppingViewModel.changeShoppingState(it, checkValue)
                        },
                        onRemoveItem = { shoppingViewModel.removeShoppingItem(it) },
                        onEditItem = {
                            shoppingToEdit = it
                            showEditShoppingDialog = true
                        }
                    )
                }
            }

            if (showEditShoppingDialog) {
                AddNewShoppingDialog(shoppingViewModel = shoppingViewModel,
                    shoppingToEdit = shoppingToEdit,
                    ) {
                    showEditShoppingDialog = false
                }
            }
        }
    }
}

@Composable
fun ShoppingCard(
    shoppingItem: ShoppingItem,
    onShoppingCheckChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (ShoppingItem) -> Unit = {}
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .animateContentSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = getIcon(shoppingItem.category)),
                    contentDescription = "Priority",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(shoppingItem.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "$${shoppingItem.cost}", // Format cost display
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Checkbox(
                    checked = shoppingItem.isDone,
                    onCheckedChange = { onShoppingCheckChange(it) }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.clickable {
                        onEditItem(shoppingItem)
                    },
                    tint = Color.Blue
                )
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    modifier = Modifier.clickable {
                        onRemoveItem()
                    },
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else
                            Icons.Filled.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            "Less"
                        } else {
                            "More"
                        }
                    )
                }

            }

            if (expanded) {
                Text(text = shoppingItem.description)
            }
        }
    }
}

@Composable
fun AddNewShoppingDialog(
    shoppingViewModel: ShoppingViewModel,
    shoppingToEdit: ShoppingItem? = null,
    onDismissRequest: () -> Unit
) {
    var itemName by rememberSaveable { mutableStateOf(shoppingToEdit?.title ?: "") }
    var itemCost by rememberSaveable { mutableStateOf(shoppingToEdit?.cost ?: "") }
    var shoppingDescription by rememberSaveable { mutableStateOf(shoppingToEdit?.description ?: "") }
    var shoppingImportant by rememberSaveable { mutableStateOf(shoppingToEdit?.priority == ShoppingPriority.HIGH) }
    var category by rememberSaveable { mutableStateOf(shoppingToEdit?.category ?: "Select a category") }
    val categories = listOf("Food", "Clothes", "Electronics", "Entertainment", "Health")

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (shoppingToEdit == null) "Add Shopping" else "Edit Shopping",
                    modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = itemCost,
                    onValueChange = { itemCost = it },
                    label = { Text("Enter item cost:") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = shoppingDescription,
                    onValueChange = { shoppingDescription = it },
                    label = { Text("Enter shopping description:") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                DropdownMenu(
                    list = categories,
                    preselected = category,
                    onSelectionChanged = { newCategory -> category = newCategory },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = shoppingImportant, onCheckedChange = { shoppingImportant = it })
                    Text("Bought")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {
                        if (shoppingToEdit == null) {
                            shoppingViewModel.addShoppingList(
                                ShoppingItem(
                                    title = itemName,
                                    cost = itemCost,
                                    description = shoppingDescription,
                                    category = category,
                                    createDate = Date(System.currentTimeMillis()).toString(),
                                    priority = if (shoppingImportant) ShoppingPriority.HIGH else ShoppingPriority.NORMAL,
                                    isDone = false
                                )
                            )
                        } else {
                            val editedShopping = shoppingToEdit.copy(
                                title = itemName,
                                cost = itemCost,
                                description = shoppingDescription,
                                priority = if (shoppingImportant)
                                    ShoppingPriority.HIGH else ShoppingPriority.NORMAL,
                                category = category
                            )
                            shoppingViewModel.editShoppingItem(editedShopping)
                        }
                        onDismissRequest()
                    }) {
                        Text("Save")
                    }
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}


@Composable
fun DropdownMenu(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value
    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        },
        colors = CardDefaults.outlinedCardColors(containerColor = backgroundColor)  // Ensuring card color matches theme
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier =
            Modifier.padding(8.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Start)
                            )
                        },
                    )
                }
            }
        }
    }
}

fun getIcon(category: String): Int {
    return when (category.lowercase()) {
        "food" -> R.drawable.food
        "clothes" -> R.drawable.clothing
        "electronics" -> R.drawable.electronics
        "health" -> R.drawable.health
        "entertainment" -> R.drawable.entertainment
        else -> R.drawable.logo // Assume you have a default icon
    }
}



