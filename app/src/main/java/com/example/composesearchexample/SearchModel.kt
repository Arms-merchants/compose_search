package com.example.composesearchexample

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

/**
 * Created by heyueyang on 2021/6/7
 */
class SearchModel : ViewModel() {

    private val request by lazy {
        SearchRequest()
    }

    @ExperimentalCoroutinesApi
    private var mChannel = ConflatedBroadcastChannel<String>()

    @FlowPreview
    @ExperimentalCoroutinesApi
    val searchResult = mChannel.asFlow()
        .filter {
            return@filter it.isNotEmpty()
        }.debounce(200)
        .distinctUntilChanged()
        .flatMapLatest { search ->
            try {
                request.search(search)
            } catch (cause: Exception) {
                cause.message?.let { Log.e("TAG", it) }
                MutableLiveData<ArrayList<String>>(null).asFlow()
            }
        }
        .asLiveData()

    @ExperimentalCoroutinesApi
    fun queryByName(name: String) {
        mChannel.offer(name)
        mChannel.runCatching { }
    }

}