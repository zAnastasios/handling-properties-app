package app.models

import app.placesautocomplete.remoterepository.model.AutocompleteResponse
import app.placesautocomplete.remoterepository.model.PlacesResponse

object FakeAutocompleteResponse {

    fun getFakeResponsesPlacesResponses(
        numOfPlaces: Int,
        mainLocation: String = "main",
        secondaryLocation: String = "second"
    ): AutocompleteResponse {
        val placesList = mutableListOf<PlacesResponse>()
        for (num in 0..numOfPlaces) {
            placesList.add(
                PlacesResponse(
                    placeId = num.toString(),
                    mainLocationDescription = mainLocation,
                    secondaryLocationDescription = secondaryLocation
                )
            )
        }
        return AutocompleteResponse(places = placesList)
    }
}
