package com.steiner.btmmovies.core

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 *
 */
sealed class RefreshInfo(
    val isEnabled: Boolean = false,
    val isRefreshing: Boolean = false
) : Serializable, Parcelable {

    @Parcelize
    object Disabled : RefreshInfo(isEnabled = false, isRefreshing = false)

    @Parcelize
    object Enabled : RefreshInfo(isEnabled = true, isRefreshing = false)

    @Parcelize
    data class InProgress @JvmOverloads constructor(
        val notice: Notice? = null
    ) : RefreshInfo(isEnabled = true, isRefreshing = true)
}