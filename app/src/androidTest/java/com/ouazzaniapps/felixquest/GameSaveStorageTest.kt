package com.ouazzaniapps.felixquest

import android.content.Context
import android.content.ContextWrapper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.util.UUID
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameSaveStorageTest {
    private lateinit var directory: File
    private lateinit var context: Context
    private lateinit var storage: GameSaveStorage

    @Before
    fun setUp() {
        val target = InstrumentationRegistry.getInstrumentation().targetContext
        directory = File(target.cacheDir, "save-storage-test-" + UUID.randomUUID())
        check(directory.mkdirs())
        // Real Android file operations, isolated from the player's save slots.
        context = object : ContextWrapper(target) {
            override fun getFilesDir(): File = directory
            override fun getApplicationContext(): Context = this
        }
        storage = GameSaveStorage(context)
    }

    @After
    fun tearDown() {
        if (::directory.isInitialized) directory.deleteRecursively()
    }

    private fun sampleSave(): GameSave {
        val weapon = GameItems.goblinWeapon.copy(name = "Schwert Äöü", damage = 73)
        val armor = GameItems.simpleArmor
        return GameSave(
            player = Player(
                name = "Felix Äöü", inventory = Inventory(listOf(
                    weapon, armor, GameItems.healPotion.copy(amount = 4)
                )),
                hp = 67, maxHp = 140, attack = 19,
                equippedWeapon = weapon, equippedArmor = armor,
                gold = 321, isDead = false, level = 8, xp = 45, xpToNextLevel = 800
            ),
            enemy = Enemy("Troll", 31, 120, 8, 25, 9, 60, 80),
            quests = QuestName.allQuests.map {
                it.copy(currentAmount = 1, isStarted = true, weaponLevel = 2)
            },
            log = listOf("Kampf gewonnen", "Trank benutzt: Äöü"),
            elapsedSeconds = 83,
            savedAtMillis = 123456789L
        )
    }

    @Test
    fun emptySlotsReturnNull() = runBlocking {
        for (slot in 1..3) assertNull(storage.load(slot))
    }

    @Test
    fun saveSurvivesNewStorageInstanceWithAllFields() = runBlocking {
        val original = sampleSave()
        storage.save(1, original)
        assertEquals(original, GameSaveStorage(context).load(1))
    }

    @Test
    fun threeSlotsRemainIndependent() = runBlocking {
        for (slot in 1..3) {
            storage.save(slot, sampleSave().copy(
                player = sampleSave().player.copy(gold = slot * 100)
            ))
        }
        for (slot in 1..3) {
            assertEquals(slot * 100, storage.load(slot)!!.player.gold)
        }
    }

    @Test
    fun overwriteReplacesOnlySelectedSlot() = runBlocking {
        val original = sampleSave()
        storage.save(1, original)
        storage.save(2, original)
        val replacement = original.copy(
            player = original.player.copy(gold = 999, level = 12),
            log = emptyList(), elapsedSeconds = 5
        )
        storage.save(1, replacement)
        assertEquals(replacement, storage.load(1))
        assertEquals(original, storage.load(2))
        assertNull(storage.load(3))
    }

    @Test
    fun invalidSlotsAreRejectedForSaveAndLoad() = runBlocking {
        for (slot in listOf(-1, 0, 4)) {
            try {
                storage.save(slot, sampleSave())
                fail("Saving invalid slot $slot must fail")
            } catch (expected: IllegalArgumentException) {
                // Expected.
            }
            try {
                storage.load(slot)
                fail("Loading invalid slot $slot must fail")
            } catch (expected: IllegalArgumentException) {
                // Expected.
            }
        }
    }

    @Test
    fun corruptFileReportsErrorInsteadOfEmptySlot() = runBlocking {
        File(directory, "save1.json").writeText("{invalid JSON")
        try {
            storage.load(1)
            fail("Corrupt save must report an error")
        } catch (expected: SerializationException) {
            // The UI must distinguish a damaged save from an empty slot.
        }
    }
}