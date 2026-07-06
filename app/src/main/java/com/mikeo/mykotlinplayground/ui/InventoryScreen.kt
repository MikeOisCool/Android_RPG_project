package com.mikeo.mykotlinplayground.ui


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.mikeo.mykotlinplayground.calculateItemHeal

/*
❤️ Heiltränke skalieren mit dem Level.
⚔️ Waffen haben feste Werte (Holzschwert +15, Eisenschwert +30 usw.).
🛡️ Rüstungen haben feste Verteidigungswerte.
*/

@Composable
fun InventoryScreen(
    viewModel: GameViewModel,
    onBackToGame: () -> Unit
) {

    val player by viewModel.player.collectAsState()
    val items = player.inventory.items
    val scrollState = rememberScrollState()
    val potionItems = visibleItemsByType(items, ItemType.POTION)
    val weaponItems = visibleItemsByType(items, ItemType.WEAPON)
    val armorItems = visibleItemsByType(items, ItemType.ARMOR)

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
            text = "Inventar",
            fontSize = 24.sp,
            textDecoration = TextDecoration.Underline
        )
        Spacer(modifier = Modifier.height(16.dp))

        InventorySection(
            title = "Tränke",
            emptyText = "Es sind keine Tränke im Inventar",
            isEmpty = potionItems.isEmpty()
        ) {
            potionItems.forEach { item ->
                PotionItem(
                    item = item,
                    playerLevel = player.level
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        InventorySection(
            title = "Rüstung",
            emptyText = "Es sind keine Rüstungen im Inventar",
            isEmpty = armorItems.isEmpty()
        ) {

            armorItems.forEach { item ->
                EquipItem(
                    statText = "Verteidigung",
                    statValue = item.defense,
                    isEquipped = player.equippedArmor?.name == item.name,
                    isEquippedText = "Rüstung ausgerüstet",
                    item = item,
                    onEquip = {
                        viewModel.onEvent(GameEvent.EquipArmor(item))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        InventorySection(
            title = "Waffen",
            emptyText = "Es sind keine Waffen im Inventar",
            isEmpty = weaponItems.isEmpty()
        ) {
            weaponItems.forEach { item ->
                EquipItem(
                    statText = "Angriff",
                    statValue = item.damage,
                    isEquipped = player.equippedWeapon?.name == item.name,
                    isEquippedText = "Waffe ausgerüstet",
                    item = item,
                    onEquip = {
                        viewModel.onEvent(GameEvent.EquipWeapon(item))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        GameButtonHoch(
            text = "Inventar Schließen",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(55.dp),
            onClick = onBackToGame
        )
    }
}

private fun visibleItemsByType(
    items: List<Item>,
    type: ItemType
): List<Item> {
    return items.filter {
        it.type == type && it.amount > 0
    }
}

@Composable
fun PotionItem(
    item: Item,
    playerLevel: Int,
) {
    val healAmount = calculateItemHeal(
        baseHeal = item.heal,
        level = playerLevel
    )

    Text(
        text = "${item.name} x${item.amount} | Heilung +$healAmount"
    )
}

@Composable
fun InventorySection(
    title: String,
    emptyText: String,
    isEmpty: Boolean,
    content: @Composable () -> Unit
) {
    Text(text = title, fontSize = 20.sp)
    Spacer(modifier = Modifier.height(8.dp))
    if (isEmpty) {
        Text(text = emptyText, fontSize = 12.sp)
    } else {
        content()
    }
}

@Composable
fun EquipItem(
    statText: String,
    statValue: Int,
    isEquipped: Boolean,
    isEquippedText: String,
    item: Item,
    onEquip: () -> Unit
) {
    Text(
        "${item.name} x${item.amount} | $statText +$statValue"
    )
    Spacer(modifier = Modifier.height(16.dp))
    GameButtonHoch(
        text = if (isEquipped) isEquippedText else "Ausrüsten",
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(50.dp),
        onClick = {
            if (!isEquipped) {
                onEquip()
            }
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(
    name = "Inventory Screen",
    showBackground = true
)
@Composable
fun InventoryScreenPreview() {
    InventoryScreen(
        viewModel = GameViewModel(),
        onBackToGame = {}
    )
}
