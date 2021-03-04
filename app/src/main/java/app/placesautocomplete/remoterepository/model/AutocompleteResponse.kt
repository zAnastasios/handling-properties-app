package app.placesautocomplete.remoterepository.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AutocompleteResponse(
    @Json(name = "placeId") val placeId: String,
    @Json(name = "mainText") val mainLocationDescription: String,
    @Json(name = "secondaryText") val secondaryLocationDescription: String
)
