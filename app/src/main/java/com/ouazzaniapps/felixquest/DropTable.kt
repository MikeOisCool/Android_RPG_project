package com.ouazzaniapps.felixquest

object DropTable {
    private const val MAGIC_POTION_DROP_CHANCE = 10
    private const val WEAPON_DROP_CHANCE = 40
    private const val ARMOR_DROP_CHANCE = 50
    private const val HEAL_DROP_CHANCE = 30
    private const val POTION_DROP_CHANCE = 30
    private const val BIG_POTION_DROP_CHANCE = 20

    fun rollMagicPotion(): Item? {
        if (!chance(MAGIC_POTION_DROP_CHANCE)) return null

        return listOf(
            GameItems.critPotion,
            GameItems.critProtectionPotion,
            GameItems.dodgePotion,
            GameItems.accuracyPotion
        ).random()
    }

    fun rollWeapon(playerLevel: Int): Item? {
        val weapon = when (playerLevel) {
            in 1..2 -> GameItems.woodWeapon
            in 3..4 -> GameItems.ironWeapon
            in 5..6 -> GameItems.silverWeapon
            in 7..8 -> GameItems.goldenWeapon
            in 9..10 -> GameItems.diamondWeapon
            else -> null
        } ?: return null

        return if (chance(WEAPON_DROP_CHANCE)) weapon else null
    }

    fun rollArmor(playerLevel: Int): Item? {
        val armor = when (playerLevel) {in 0..3 -> GameItems.simpleArmor
            in 4..5 -> GameItems.ironArmor
            in 6..7 -> GameItems.silverArmor
            in 8..9 -> GameItems.goldenArmor
            in 10..11 -> GameItems.diamondArmor
            else -> null
    } ?: return null

        return if (chance(ARMOR_DROP_CHANCE)) armor else null
    }

    fun rollHealPotion(): Item? {
        return if (chance(POTION_DROP_CHANCE)) GameItems.healPotion else null
    }

    fun rollBigHealPotion(): Item? {
        return if (chance(BIG_POTION_DROP_CHANCE)) GameItems.healBigPotion else null
    }

    fun rollHealDrop(): Boolean {
        return chance(HEAL_DROP_CHANCE)
    }
}
