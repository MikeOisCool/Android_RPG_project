package com.ouazzaniapps.felixquest

object EnemyFactory {

    fun createRandomEnemy(playerLevel: Int, dayPhase: DayPhase = DayPhase.DAY): Enemy {

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

        if (playerLevel >= 4) {
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

        if (playerLevel >= 8) {
            possibleEnemies.add(
                Enemy(
                    "Troll",
                    hp = 1500,
                    maxHp = 1500,
                    level = 8,
                    attack = 70,
                    defense = 20,
                    goldReward = 280,
                    xpReward = 280
                )
            )
        }

        if (playerLevel >= 12) {
            possibleEnemies.add(
                Enemy(
                    "Drache",
                    hp = 10000,
                    maxHp = 10000,
                    level = 12,
                    attack = 150,
                    defense = 50,
                    goldReward = 350,
                    xpReward = 350
                )
            )
        }

        if (dayPhase == DayPhase.NIGHT) {
            possibleEnemies.add(
                Enemy(
                    name = "Geist",
                    hp = 35,
                    maxHp = 35,
                    level = 1,
                    attack = 7,
                    defense = 2,
                    goldReward = 25,
                    xpReward = 30
                )
            )

            possibleEnemies.add(
                Enemy(
                    name = "Skelett",
                    hp = 45,
                    maxHp = 45,
                    level = 1,
                    attack = 8,
                    defense = 3,
                    goldReward = 30,
                    xpReward = 35
                )
            )
        }
        val baseEnemy = possibleEnemies.random()

        return createScaledEnemy(baseEnemy, playerLevel)
    }
}
