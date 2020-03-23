package com.steiner.btmmovies.core

import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.lifecycle.MediatorLiveData
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 *
 */
sealed class Notice : Serializable, Parcelable {

    /**
     *
     */
    @Parcelize
    data class Toast(
        /** Resource string ID of the message to show */
        @StringRes val messageId: Int,

        /** Set to true for a Toast with long duration  */
        val longDuration: Boolean = false
    ) : Notice()

    /**
     *
     */
    @Parcelize
    data class Snackbar(
        /** Resource string ID of the message to show */
        @StringRes val messageId: Int,

        /** Set to true for a Snackbar with long duration  */
        val longDuration: Boolean = false
    ) : Notice()
}

/**
 *
 */
@Suppress("NOTHING_TO_INLINE")
class NoticeEventLiveData : MediatorLiveData<Event<Notice>>() {

    inline fun setNotice(notice: Notice) {
        value = Event(notice)
    }

    inline fun postNotice(notice: Notice) {
        postValue(Event(notice))
    }
}
