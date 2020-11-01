package it.ilker.repeatcard.db.directory

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import it.ilker.repeatcard.db.flashcard.Flashcard
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import it.ilker.repeatcard.test.RepositoryTestBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals

@ExperimentalCoroutinesApi
internal class FlashcardDirectoryRepositoryTest : RepositoryTestBase() {

    private lateinit var subject: FlashcardDirectoryRepository

    private val sampleDirectory = Directory(
        id = 1337,
        title = "Some directory",
        creationDate = null
    )

    private val sampleFlashcard = Flashcard(
        id = 1337,
        title = "Some flashcard",
        description = "Some description",
        creationDate = null,
        lastModified = null,
        directoryId = null,
        imageUri = null
    )

    @RelaxedMockK
    lateinit var dao: FlashcardDirectoryDao

    @BeforeEach
    fun setUp() {
        subject = FlashcardDirectoryRepository(dao)
    }

    @Test
    fun `addDirectory calls dao to insert directory`() = runBlockingTest {
        val directorySlot = slot<Directory>()
        subject.addDirectory(sampleDirectory)

        coVerify { dao.insert(capture(directorySlot)) }

        assertEquals(sampleDirectory, directorySlot.captured)
    }

    @Test
    fun `getDirectories calls dao to get directories`() = runBlockingTest {
        subject.getDirectories()

        coVerify { dao.getDirectories() }
    }

    @Test
    fun `getDirectories gets expected directories`() = runBlockingTest {
        coEvery { dao.getDirectories() } returns listOf(sampleDirectory)

        val result = subject.getDirectories()

        coVerify { dao.getDirectories() }

        assertEquals(sampleDirectory, result[0])
    }

    @Test
    fun `getDirectoryContent calls dao to get directory content`() = runBlockingTest {
        val idSlot = slot<Int>()
        subject.getDirectoryContent(sampleDirectory.id)

        coVerify { dao.getDirectoryContent(capture(idSlot)) }

        assertEquals(sampleDirectory.id, idSlot.captured)
    }

    @Test
    fun `getDirectoryContent gets expected directory content`() = runBlockingTest {
        coEvery { dao.getDirectoryContent(any()) } returns listOf(sampleFlashcard)

        val result = subject.getDirectoryContent(sampleDirectory.id)

        coVerify { dao.getDirectoryContent(any()) }

        assertEquals(sampleFlashcard, result[0])
    }

    @Test
    fun `deleteDirectory calls dao to delete expected directory`() = runBlockingTest {
        val idSlot = slot<Int>()
        subject.deleteDirectory(sampleDirectory.id)

        coVerify { dao.deleteDirectory(capture(idSlot)) }

        assertEquals(sampleDirectory.id, idSlot.captured)
    }
}
