package com.steiner.btmmovies.data.db

import androidx.room.TypeConverter
import com.steiner.btmmovies.model.enumeration.FavouriteState
import com.steiner.btmmovies.model.enumeration.Request

/**
 *
 */
internal object AccountDbConverters {

    private val requestValues by lazy(LazyThreadSafetyMode.NONE) {
        Request.values()
    }
    private val favouriteStateValues by lazy(LazyThreadSafetyMode.NONE) {
        FavouriteState.values()
    }

    @JvmStatic
    @TypeConverter
    fun fromRequest(item: Request?): String? = item?.value

    @JvmStatic
    @TypeConverter
    fun toRequest(value: String?): Request? =
        requestValues.firstOrNull { it.value == value }

    @JvmStatic
    @TypeConverter
    fun fromFavouriteState(item: FavouriteState?): String? = item?.value

    @JvmStatic
    @TypeConverter
    fun toFavouriteState(value: String?): FavouriteState? =
        favouriteStateValues.firstOrNull { it.value == value }
}
