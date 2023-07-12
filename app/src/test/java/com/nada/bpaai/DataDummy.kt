package com.nada.bpaai

import com.nada.bpaai.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val quote = ListStoryItem(
                i.toString(),
                "Title",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "This is description",
                null,
                null,
            )
            items.add(quote)
        }
        return items
    }

    fun generateDummyNewStory(): List<ListStoryItem> {
        val newsList = ArrayList<ListStoryItem>()
        for (i in 0..5) {
            val stories = ListStoryItem(
                i.toString(),
                "Title",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "This is description",
                null,
                null,
            )
            newsList.add(stories)
        }
        return newsList
    }
}