package com.example.rikharthu.itunestopcharts

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.runner.AndroidJUnit4
import com.example.rikharthu.itunestopcharts.browse.BrowseTracksViewModel
import com.example.rikharthu.itunestopcharts.repository.DataWrapper
import com.example.rikharthu.itunestopcharts.repository.TracksRepository
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class BrowseTracksViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testSomething() {
        val repo = mock(TracksRepository::class.java)
        val entries = listOf(EntryDataFactory.makeEntry(), EntryDataFactory.makeEntry())
        `when`(repo.getTracks()).thenReturn(DataWrapper(entries).asLiveData())

        val viewModel = BrowseTracksViewModel(repo)
        val tracks = viewModel.tracks.blockingObserve()
        val x = 4
    }
}