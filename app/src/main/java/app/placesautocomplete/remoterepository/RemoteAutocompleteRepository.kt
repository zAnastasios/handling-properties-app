package app.placesautocomplete.remoterepository

import app.placesautocomplete.remoterepository.model.AutocompleteResponse

interface RemoteAutocompleteRepository {

    suspend fun getPlaces(userInput: String): List<AutocompleteResponse>
}
