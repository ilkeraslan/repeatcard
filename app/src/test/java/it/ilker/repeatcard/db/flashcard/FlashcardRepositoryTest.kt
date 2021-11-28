package it.ilker.repeatcard.db.flashcard

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import it.ilker.repeatcard.test.RepositoryTestBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach

@ExperimentalCoroutinesApi
class FlashcardRepositoryTest : RepositoryTestBase() {

    private lateinit var subject: FlashcardRepository

    private val sampleFlashcard = me.ilker.business.flashcard.Flashcard(
        id = 1337,
        title = "Some flashcard",
        description = "Some description",
        creationDate = null,
        lastModified = null,
        directoryId = null,
        imageUri = null
    )

    private val dbFlashcard = Flashcard(
        id = 1337,
        title = "Some flashcard",
        description = "Some description",
        creationDate = null,
        lastModified = null,
        directoryId = null,
        imageUri = null
    )

    @RelaxedMockK
    lateinit var dao: FlashcardDao

    @BeforeEach
    fun setUp() {
        subject = FlashcardRepository(dao)
    }

    @Test
    fun `deleteAll calls dao when called`() = runBlockingTest {
        subject.deleteAll()

        coVerify { dao.deleteAll() }
    }

    @Test
    fun `deleteFlashcard calls dao to delete the expected flashcard`() = runBlockingTest {
        val idToDelete = 1337
        val idSlot = slot<Int>()
        subject.deleteFlashcard(idToDelete)

        coVerify { dao.deleteFlashcard(capture(idSlot)) }

        assertEquals(idToDelete, idSlot.captured)
    }

    @Test
    fun `getFlashcard calls dao to get the expected flashcard`() = runBlockingTest {
        val idToGet = 8675309
        val idSlot = slot<Int>()
        subject.getFlashcard(idToGet)

        coVerify { dao.getFlashcard(capture(idSlot)) }

        assertEquals(idToGet, idSlot.captured)
    }

    @Test
    fun `getFlashcards calls dao to get all flashcards`() = runBlockingTest {
        subject.getFlashcards()

        coVerify { dao.getFlashcards() }
    }

    @Test
    fun `getFlashcards gets expected flashcards`() = runBlockingTest {
        coEvery { dao.getFlashcards() } returns listOf(dbFlashcard)

        val result = subject.getFlashcards()

        assertEquals(sampleFlashcard, result[0])
    }

    @Test
    fun `getFlashcardsForDirectory calls dao to get flashcards for expected directory`() = runBlockingTest {
        val directoryToGet = 123
        val idSlot = slot<Int>()
        subject.getFlashcardsForDirectory(directoryToGet)

        coVerify { dao.getFlashcardsForDirectory(capture(idSlot)) }

        assertEquals(directoryToGet, idSlot.captured)
    }

    @Test
    fun `insert calls dao to insert flashcard`() = runBlockingTest {
        val flashcardSlot = slot<Flashcard>()
        subject.insert(sampleFlashcard)

        coVerify { dao.insert(capture(flashcardSlot)) }

        assertEquals(sampleFlashcard, flashcardSlot.captured)
    }

    @Test
    fun `updateFlashcard calls dao to update flashcard`() = runBlockingTest {
        val flashcardSlot = slot<Flashcard>()
        subject.updateFlashcard(sampleFlashcard)

        coVerify { dao.updateFlashcard(capture(flashcardSlot)) }

        assertEquals(sampleFlashcard, flashcardSlot.captured)
    }
}
