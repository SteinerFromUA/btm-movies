package com.steiner.btmmovies.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 *
 */
@JsonClass(generateAdapter = true)
data class Profile(
    @Json(name = "id") val id: String,
    @Json(name = "email") val email: String,
    @Json(name = "name") val name: String,
    @Json(name = "picture") val picture: Picture?
) {

    /**
     *
     */
    @JsonClass(generateAdapter = true)
    data class Picture(
        @Json(name = "data") val parameters: Parameters
    ) {

        /**
         *
         */
        @JsonClass(generateAdapter = true)
        data class Parameters(
            @Json(name = "height") val height: Int,
            @Json(name = "is_silhouette") val isSilhouette: Boolean,
            @Json(name = "url") val url: String,
            @Json(name = "width") val width: Int
        )
    }
}
