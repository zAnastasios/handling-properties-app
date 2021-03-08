package app.placesautocomplete.ui.model

sealed class GetPlacesResponseState {
    data class GetPlacesFailure(
        val autocompleteFailureResponse: GetPlacesFailureResponse
    ) : GetPlacesResponseState()

    data class GetPlacesSuccess(
        val places: List<Places>
    ) : GetPlacesResponseState()

}

data class Places(
    val placeId: String,
    val mainText: String,
    val secondaryText: String
)

enum class GetPlacesFailureResponse {
    NO_INTERNET,
    SERVER_DOWN,
    UNKNOWN_ERROR
}
