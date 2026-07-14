package com.mikeo.mykotlinplayground.ui


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

        Text(
            text = "Itemsshop", fontSize = 24.sp, textDecoration = TextDecoration.Underline
        )
        Spacer(modifier = Modifier.height(16.dp))

        InventorySection(
            title = "Angebote",
            emptyText = "Im Moment gibt es keine Angebote",
            isEmpty = shopItems.isEmpty()
        ) {
            shopItems.forEach { item ->

                val itemInventory = items.find { inventoryItem -> inventoryItem.name == item.name}
                val hasNotEnoughGold = player.gold < buyPrice(item = item, playerLevel = player.level)
                val potionIsFull = item.type == ItemType.POTION && (itemInventory?.amount ?: 0) >= 10
                val uniqueItemIsAlreadyInInventory = itemInventory != null && item.type != ItemType.POTION
                val canBuy = !hasNotEnoughGold && !potionIsFull && !uniqueItemIsAlreadyInInventory


                Text(
                    "${item.name} x${item.amount} | Kaufpreis: ${
                        buyPrice(
                            item = item, playerLevel = player.level
                        )
                    }"
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    if (canBuy) {


                    GameButtonHoch(
                        text = "Kauf",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(55.dp),
                        onClick = {
                            viewModel.onEvent(GameEvent.BuyItem(item = item))
                        })
                    } else {
                        if (hasNotEnoughGold) {
                            Text("Nicht genügend Gold")
                        } else if (potionIsFull) {
                            Text("Tränke sind voll")
                        } else if (uniqueItemIsAlreadyInInventory) {
                            Text("$item.name ist bereits im Inventar")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        InventorySection(
            title = "Tränke",
            emptyText = "Es sind keine Tränke im Inventar",
            isEmpty = potionItems.isEmpty()
        ) {
            potionItems.forEach { item ->
                SellPotionItem(
                    item = item, playerLevel = player.level
                )
                Spacer(modifier = Modifier.height(8.dp))

                GameButtonHoch(
                    text = "Verkauf",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(55.dp),
                    onClick = {
                        viewModel.onEvent(GameEvent.SellItem(item = item))
                    })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        InventorySection(
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

        InventorySection(
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


            Spacer(modifier = Modifier.height(8.dp))

            GameButtonHoch(
                text = "Shop verlassen",
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(55.dp),
                onClick = onBackToGame
            )
        }
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
        text = "${item.name} x${item.amount} | Heilung +$healAmount | Verkaufspreis: ${sellPrice(item = item, playerLevel = playerLevel)}"
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
        "${item.name} x${item.amount} | $statText +$statValue | Verkaufspreis: $sellPrice Gold"
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row {

        GameButtonHoch(
            text = "Verkaufen",
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(55.dp),
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
