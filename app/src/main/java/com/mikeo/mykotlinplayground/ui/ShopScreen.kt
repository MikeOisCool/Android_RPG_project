package com.mikeo.mykotlinplayground.ui


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikeo.mykotlinplayground.GameEvent
import com.mikeo.mykotlinplayground.GameViewModel
import com.mikeo.mykotlinplayground.Item
import com.mikeo.mykotlinplayground.ItemType
import com.mikeo.mykotlinplayground.Player
import com.mikeo.mykotlinplayground.availableShopItems
import com.mikeo.mykotlinplayground.buyPrice
import com.mikeo.mykotlinplayground.calculateItemHeal
import com.mikeo.mykotlinplayground.sellPrice


@Composable
fun ShopScreen(
    viewModel: GameViewModel, onBackToGame: () -> Unit
) {
    val player by viewModel.player.collectAsState()
    val items = player.inventory.items
    val scrollState = rememberScrollState()
    val potionItems = visibleItemsByType(items, ItemType.POTION)
    val weaponItems = visibleItemsByType(items, ItemType.WEAPON)
    val armorItems = visibleItemsByType(items, ItemType.ARMOR)
    val shopItems = availableShopItems(player.level)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFF26C6DA))
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Itemsshop", fontSize = 24.sp, textDecoration = TextDecoration.Underline
        )
        Spacer(modifier = Modifier.height(16.dp))

        ShopInventorySection(
            title = "Angebote",
            emptyText = "Im Moment gibt es keine Angebote",
            isEmpty = shopItems.isEmpty()
        ) {
            shopItems.forEach { item ->

                val itemInventory = items.find { inventoryItem -> inventoryItem.name == item.name }
                val hasNotEnoughGold =
                    player.gold < buyPrice(item = item, playerLevel = player.level)
                val potionIsFull =
                    item.type == ItemType.POTION && (itemInventory?.amount ?: 0) >= 10
                val uniqueItemIsAlreadyInInventory =
                    itemInventory != null && item.type != ItemType.POTION
                val canBuy = !hasNotEnoughGold && !potionIsFull && !uniqueItemIsAlreadyInInventory


                Text(
                    "${item.name} x${item.amount} | Kaufpreis: ${
                        buyPrice(
                            item = item, playerLevel = player.level
                        )
                    }"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (canBuy) {


                        ShopButton(
                            text = "Kauf $item",
                            onClick = {
                                viewModel.onEvent(GameEvent.BuyItem(item = item))
                            })
                    } else {

                        if (hasNotEnoughGold) {
                            Text(
                                text = "Nicht genügend Gold",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        } else if (potionIsFull) {
                            Text(
                                text = "Tränke sind voll",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        } else if (uniqueItemIsAlreadyInInventory) {
                            Text(
                                text = "${item.name} ist bereits im Inventar",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        ShopInventorySection(
            title = "Tränke",
            emptyText = "Es sind keine Tränke im Inventar",
            isEmpty = potionItems.isEmpty()
        ) {
            potionItems.forEach { item ->
                SellPotionItem(
                    item = item, playerLevel = player.level
                )
                Spacer(modifier = Modifier.height(8.dp))

                ShopButton(
                    text = "Verkauf $item",
                    onClick = {
                        viewModel.onEvent(GameEvent.SellItem(item = item))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        ShopInventorySection(
            title = "Rüstung",
            emptyText = "Es sind keine Rüstungen im Inventar",
            isEmpty = armorItems.isEmpty()
        ) {
            val sellableArmorItems = sellableArmorItems(armorItems, player)
            if (sellableArmorItems.isEmpty()) {
                Text("Rüstung muss abgelegt werden")
            } else {
                sellableArmorItems.forEach { item ->
                    ShopSellItem(
                        item = item,
                        statText = "Verteidigung",
                        statValue = item.defense,
                        viewModel = viewModel,
                        onSell = {
                            viewModel.onEvent(GameEvent.SellItem(item = item))
                        })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        ShopInventorySection(
            title = "Waffen",
            emptyText = "Es sind keine Waffen im Inventar",
            isEmpty = weaponItems.isEmpty()
        ) {
            val sellableWeaponItems = sellableWeaponItems(weaponItems, player)
            if (sellableWeaponItems.isEmpty()) {
                Text("Waffe muss abgelegt werden")
            } else {
                sellableWeaponItems.forEach { item ->
                    ShopSellItem(
                        item = item,
                        statText = "Angriff",
                        statValue = item.damage,
                        viewModel = viewModel,
                        onSell = {
                            viewModel.onEvent(GameEvent.SellItem(item = item))
                        })
                }
                Spacer(modifier = Modifier.height(8.dp))

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        GameButtonHoch(
            text = "Shop verlassen",
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(55.dp),
            fontSize = 26.sp,
            onClick = onBackToGame
        )

    }


}

@Composable
fun ShopInventorySection(
    title: String,
    emptyText: String,
    isEmpty: Boolean,
    content: @Composable () -> Unit
) {
    Text(text = title, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(8.dp))
    if (isEmpty) {
        Text(
            text = emptyText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        content()
    }
}

@Composable
fun ShopButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth(0.6f)
        .height(65.dp)
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SellPotionItem(
    item: Item,
    playerLevel: Int,
) {
    val healAmount = calculateItemHeal(
        baseHeal = item.heal,
        level = playerLevel
    )

    Text(
        text = "${item.name} x${item.amount} | Heilung +$healAmount | Verkaufspreis: ${
            sellPrice(
                item = item,
                playerLevel = playerLevel
            )
        }",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

fun sellableArmorItems(items: List<Item>, player: Player): List<Item> {
    val sellableArmorItems = items.filter { item ->
        player.equippedArmor?.name != item.name
    }
    return sellableArmorItems
}

fun sellableWeaponItems(items: List<Item>, player: Player): List<Item> {
    val sellableWeaponItems = items.filter { item ->
        player.equippedWeapon?.name != item.name
    }
    return sellableWeaponItems
}

@Composable
fun ShopSellItem(
    item: Item, statText: String, statValue: Int, viewModel: GameViewModel, onSell: () -> Unit
) {
    val player by viewModel.player.collectAsState()
    val sellPrice = sellPrice(item, playerLevel = player.level)
    Text(
        text = "${item.name} x${item.amount} | $statText +$statValue | Verkaufspreis: $sellPrice Gold",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row {

        ShopButton(
            text = "Verkauf $item",
            onClick = onSell
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

private fun visibleItemsByType(
    items: List<Item>, type: ItemType
): List<Item> {
    return items.filter {
        it.type == type && it.amount > 0
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    name = "Shop Screen", showBackground = true
)
@Composable
fun ShopScreenPreview() {
    ShopScreen(
        viewModel = GameViewModel(), onBackToGame = {})
}
