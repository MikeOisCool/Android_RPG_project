package com.mikeo.mykotlinplayground
object EnemyFactory {

    fun createRandomEnemy(playerLevel: Int): Enemy {

        val baseEnemy = listOf(
            Enemy(
                "Goblin",
                hp = 30,
                maxHp = 30,
                level = 1,
                damage = 5,
                goldReward = 20,
                xpReward = 25
            ),
            Enemy(
                "Wolf",
                hp = 40,
                maxHp = 40,
                level = 1,
                damage = 8,
                goldReward = 30,
                xpReward = 35
            ),
            Enemy(
                "Ork",
                hp = 60,
                maxHp = 60,
                level = 1,
                damage = 12,
                goldReward = 50,
                xpReward = 60
            ),
            Enemy(
                "Stier",
                hp = 1000,
                maxHp = 1000,
                level = 500,
                damage = 50,
                goldReward = 100,
                xpReward = 100
            )
        ).random()

        return createScaledEnemy(baseEnemy, playerLevel)
    }
}