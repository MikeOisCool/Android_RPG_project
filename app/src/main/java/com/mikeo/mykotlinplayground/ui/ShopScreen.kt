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
    val sellablePotionItems = sellableItemsByType(items, player, ItemType.POTION)
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
        Text(
            text = "Dein Level: ${player.level}, Gold: ${player.gold}",
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline
        )
        ShopInventorySection(
            title = "Angebote",
            emptyText = "Im Moment gibt es keine Angebote",
            isEmpty = shopItems.isEmpty()
        ) {
            shopItems.forEach { item ->
                ShopOfferItem(
                    item = item,
                    player = player,
                    onBuy = {
                        viewModel.onEvent(GameEvent.BuyItem(item = item))
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        ShopInventorySection(
            title = "Tränke",
            emptyText = "Es sind keine Tränke im Inventar",
            isEmpty = sellablePotionItems.isEmpty()
        ) {


            ShopSellItems(
                player = player,
                type = ItemType.POTION,
                onSell = { item ->
                    viewModel.onEvent(GameEvent.SellItem(item = item))
                }
            )

        }

        Spacer(modifier = Modifier.height(8.dp))

        ShopInventorySection(
            title = "Rüstung",
            emptyText = "Es sind keine Rüstungen im Inventar",
            isEmpty = armorItems.isEmpty()
        ) {


            ShopSellItems(
                player = player,
                type = ItemType.ARMOR,
                onSell = { item ->
                    viewModel.onEvent(GameEvent.SellItem(item = item))
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        ShopInventorySection(
            title = "Waffen",
            emptyText = "Es sind keine Waffen im Inventar",
            isEmpty = weaponItems.isEmpty()
        ) {
            ShopSellItems(
                player = player,
                type = ItemType.WEAPON,
                onSell = { item ->
                    viewModel.onEvent(GameEvent.SellItem(item = item))
                })
            Spacer(modifier = Modifier.height(8.dp))
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
fun ShopSellItems(player: Player, type: ItemType, onSell: (Item) -> Unit) {
    val items = player.inventory.items
    val sellablePotionItems = sellableItemsByType(items, player, ItemType.POTION)
    val weaponItems = visibleItemsByType(items, ItemType.WEAPON)
    val sellableWeaponItems = sellableItemsByType(weaponItems, player, ItemType.WEAPON)
    val armorItems = visibleItemsByType(items, ItemType.ARMOR)
    val sellableArmorItems = sellableItemsByType(armorItems, player, ItemType.ARMOR)


    when {
        type == ItemType.POTION && sellablePotionItems.isEmpty() -> StatusText("Es sind keine Tränke im Inventar")
        type == ItemType.WEAPON && sellableWeaponItems.isEmpty() -> StatusText("Waffe muss abgelegt werden")
        type == ItemType.ARMOR && sellableArmorItems.isEmpty() -> StatusText("Rüstung muss abgelegt werden")
    }

    when {
        type == ItemType.POTION && sellablePotionItems.isNotEmpty() -> {
            sellablePotionItems.forEach { item ->
                ShopSellPotionItem(
                    item = item,
                    playerLevel = player.level,
                    onSell = {
                        onSell(item)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        type == ItemType.WEAPON && sellableWeaponItems.isNotEmpty() -> {
            sellableWeaponItems.forEach { item ->
                ShopSellItem(
                    item = item,
                    playerLevel = player.level,
                    statText = "Angriff",
                    statValue = item.damage,
                    onSell = {
                        onSell(item)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        type == ItemType.ARMOR && sellableArmorItems.isNotEmpty() -> {
            sellableArmorItems.forEach { item ->
                ShopSellItem(
                    item = item,
                    playerLevel = player.level,
                    statText = "Verteidigung",
                    statValue = item.defense,
                    onSell = {
                        onSell(item)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ShopSellPotionItem(
    item: Item,
    playerLevel: Int,
    onSell: () -> Unit
) {
    PotionSellText(
        item = item, playerLevel = playerLevel
    )
    Spacer(modifier = Modifier.height(8.dp))

    ShopButton(
        text = "Verkauf $item für ${sellPrice(item, playerLevel)} Gold",
        onClick = {
            onSell()
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
}


@Composable
fun ShopOfferItem(
    item: Item,
    player: Player,
    onBuy: () -> Unit
) {
    val itemBuyPrice = buyPrice(item = item, playerLevel = player.level)
    val itemInventory =
        player.inventory.items.find { inventoryItem -> inventoryItem.name == item.name }
    val hasNotEnoughGold = player.gold < itemBuyPrice
    val potionIsFull =
        item.type == ItemType.POTION && (itemInventory?.amount ?: 0) >= 10
    val uniqueItemIsAlreadyInInventory =
        itemInventory != null && item.type != ItemType.POTION
    val canBuy = !hasNotEnoughGold && !potionIsFull && !uniqueItemIsAlreadyInInventory
    val statusText = when {
        hasNotEnoughGold -> "Nicht genügend Gold"
        potionIsFull -> "Tränke sind voll"
        uniqueItemIsAlreadyInInventory -> "${item.name} ist bereits im Inventar"
        else -> null
    }

    Text(
        text = "${item.name} x${item.amount} | Kaufpreis: $itemBuyPrice"
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
                text = "Kauf $item für $itemBuyPrice Gold",
                onClick = {
                    onBuy()
                })
        } else if (statusText != null) {
            StatusText(text = statusText)
        }
    }
}

@Composable
fun StatusText(
    text: String
) {
    Text(
        text = text,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
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
        .fillMaxWidth(0.7f)
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
fun PotionSellText(
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

fun sellableItemsByType(items: List<Item>, player: Player, type: ItemType): List<Item> {
    return items.filter { it ->
        it.type == type &&
                it.amount > 0 &&
                when (type) {
                    ItemType.WEAPON -> player.equippedWeapon?.name != it.name
                    ItemType.ARMOR -> player.equippedArmor?.name != it.name
                    ItemType.POTION -> true
                }
    }
}

@Composable
fun ShopSellItem(
    item: Item,
    playerLevel: Int,
    statText: String,
    statValue: Int,
    onSell: () -> Unit
) {

    val itemSellPrice = sellPrice(item, playerLevel = playerLevel)
    Text(
        text = "${item.name} x${item.amount} | $statText +$statValue | Verkaufspreis: $itemSellPrice Gold",
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
