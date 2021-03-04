package app.propertyad.remoterepository

import app.propertyad.remoterepository.model.AutocompleteResponse

interface RemoteAutocompleteRepository {

    suspend fun getPlaces(userInput: String): AutocompleteResponse
}
