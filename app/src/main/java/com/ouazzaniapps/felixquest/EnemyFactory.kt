package com.ouazzaniapps.felixquest

object EnemyFactory {

    fun createRandomEnemy(playerLevel: Int): Enemy {

        val possibleEnemies = mutableListOf(
            Enemy(
                "Goblin",
                hp = 30,
                maxHp = 30,
                level = 1,
                attack = 5,
                defense = 2,
                goldReward = 20,
                xpReward = 25
            ),
            Enemy(
                "Wolf",
                hp = 40,
                maxHp = 40,
                level = 1,
                attack = 8,
                defense = 3,
                goldReward = 30,
                xpReward = 35
            )
        )
        if (playerLevel >= 2) {
            possibleEnemies.add(
                Enemy(
                    "Stier",
                    hp = 1000,
                    maxHp = 1000,
                    level = 500,
                    attack = 50,
                    defense = 15,
                    goldReward = 200,
                    xpReward = 200
                )
            )
        }

        if (playerLevel >= 3) {
            possibleEnemies.add(
                Enemy(
                    "Ork",
                    hp = 60,
                    maxHp = 60,
                    level = 3,
                    attack = 12,
                    defense = 4,
                    goldReward = 50,
                    xpReward = 60
                )
            )
        }

        val baseEnemy = possibleEnemies.random()

        return createScaledEnemy(baseEnemy, playerLevel)
    }
}
