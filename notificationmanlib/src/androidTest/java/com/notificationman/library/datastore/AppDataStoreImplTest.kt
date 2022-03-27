package com.notificationman.library.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.notificationman.library.extensions.getRandomUUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AppDataStoreImplTest {

    companion object {
        private const val NOTIFICATION_MAN_FAKE_PREFERENCES = "notification_man_fake_preferences"
    }

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher + Job())
    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = testCoroutineScope,
            produceFile = { testContext.preferencesDataStoreFile(NOTIFICATION_MAN_FAKE_PREFERENCES) }
        )
    private lateinit var appDataStoreImpl: AppDataStoreImpl

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        appDataStoreImpl = AppDataStoreImpl(testDataStore)
    }

    @Test
    fun test_appDataStoreImpl_saveWorkerId() {
        testCoroutineScope.runBlockingTest {
            with(appDataStoreImpl) {
                val id = getRandomUUID()
                saveWorkerId(id)
                val actual = getFirstWorkerId()
                assertThat(actual, `is`(id))
            }
        }
    }

    @Test
    fun test_appDataStoreImpl_deleteWorkerId() {
        testCoroutineScope.runBlockingTest {
            with(appDataStoreImpl) {
                val id1 = getRandomUUID()
                val id2 = getRandomUUID()
                val id3 = getRandomUUID()
                saveWorkerId(id1)
                saveWorkerId(id2)
                saveWorkerId(id3)
                deleteWorkerId(id2)
                val actual = getFirstWorkerId()
                assertThat(actual, `is`(id1))
            }
        }
    }

    @Test
    fun test_appDataStoreImpl_deleteAllWorkerIds() {
        testCoroutineScope.runBlockingTest {
            with(appDataStoreImpl) {
                val id = getRandomUUID()
                saveWorkerId(id)
                deleteAllWorkerIds()
                val actual = getWorkerIds()
                assertThat(actual, `is`(emptyList()))
            }
        }
    }

    @Test
    fun test_appDataStoreImpl_getWorkerIds() {
        testCoroutineScope.runBlockingTest {
            with(appDataStoreImpl) {
                val id = getRandomUUID()
                saveWorkerId(id)
                val actual = getWorkerIds().size
                assertThat(actual, `is`(1))
            }
        }
    }

    @Test
    fun test_appDataStoreImpl_getFirstWorkerId() {
        testCoroutineScope.runBlockingTest {
            with(appDataStoreImpl) {
                val id = getRandomUUID()
                saveWorkerId(id)
                val actual = getFirstWorkerId()
                assertThat(actual, `is`(id))
            }
        }
    }


    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        testCoroutineScope.runBlockingTest {
            testDataStore.edit { it.clear() }
        }
        testCoroutineScope.cancel()
    }
}