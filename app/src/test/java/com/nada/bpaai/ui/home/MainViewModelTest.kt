package com.nada.bpaai.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.nada.bpaai.DataDummy.generateDummyNewStory
import com.nada.bpaai.DataDummy.generateDummyStoryResponse
import com.nada.bpaai.MainDispatcherRule
import com.nada.bpaai.data.StoryRepository
import com.nada.bpaai.data.remote.response.ListStoryItem
import com.nada.bpaai.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private val dummyToken = "authentication_token"

    @Test
    fun `When Get Data Story Should Not Return Null`() = runTest {
        val dummyStories = generateDummyStoryResponse()
        val data: PagingData<ListStoryItem> = PagedTestDataSources.snapshot(dummyStories)
        val story = MutableLiveData<PagingData<ListStoryItem>>()

        story.value = data
        `when`(storyRepository.getAllStory(dummyToken)).thenReturn(story)
        val mainViewModel = MainViewModel(storyRepository)
        val actualData: PagingData<ListStoryItem> = mainViewModel.getAllStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = Dispatchers.Unconfined,
            workerDispatcher = Dispatchers.Unconfined,
        )
        differ.submitData(actualData)

        advanceUntilIdle()

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)
        Assert.assertEquals(dummyStories[0].id, differ.snapshot()[0]?.id)
        Assert.assertEquals(dummyStories[0].name, differ.snapshot()[0]?.name)
        Assert.assertEquals(dummyStories[0].description, differ.snapshot()[0]?.description)
        Assert.assertEquals(dummyStories[0].lat, differ.snapshot()[0]?.lat)
        Assert.assertEquals(dummyStories[0].lon, differ.snapshot()[0]?.lon)
    }

    @Test
    fun `when Get Data Story is Empty Should Return Null`() = runTest {
        val data: PagingData<ListStoryItem> = PagedTestDataSources.snapshot(listOf())
        val story = MutableLiveData<PagingData<ListStoryItem>>()

        story.value = data
        `when`(storyRepository.getAllStory(dummyToken)).thenReturn(story)
        val mainViewModel = MainViewModel(storyRepository)
        val actualData: PagingData<ListStoryItem> = mainViewModel.getAllStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = Dispatchers.Unconfined,
            workerDispatcher = Dispatchers.Unconfined,
        )
        differ.submitData(actualData)

        advanceUntilIdle()

        Assert.assertNotNull(differ.snapshot())
        Assert.assertTrue(differ.snapshot().isEmpty())
        Assert.assertEquals(0, differ.snapshot().size)
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

    class PagedTestDataSources private constructor() :
        PagingSource<Int, LiveData<List<ListStoryItem>>>() {
        companion object {
            fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

}