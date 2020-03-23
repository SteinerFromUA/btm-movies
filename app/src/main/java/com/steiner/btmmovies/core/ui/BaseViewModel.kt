package com.steiner.btmmovies.core.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.steiner.btmmovies.core.Event
import com.steiner.btmmovies.core.Notice
import com.steiner.btmmovies.core.NoticeEventLiveData
import com.steiner.btmmovies.core.util.CoroutineDispatcherProvider

/**
 *
 */
abstract class BaseViewModel(
    application: Application,
    protected val coroutineDispatchers: CoroutineDispatcherProvider,
    protected val handle: SavedStateHandle
) : AndroidViewModel(application) {

    /**  */
    @Suppress("PropertyName")
    protected val _anyNoticeEvent = NoticeEventLiveData()
    val anyNoticeEvent: LiveData<Event<Notice>>
        get() = _anyNoticeEvent
}
