package app.placesautocomplete.remoterepository.model

import app.placesautocomplete.remoterepository.AutocompleteService
import app.placesautocomplete.remoterepository.RemoteAutocompleteRepository

class RemoteAutocompleteRepositoryImpl(
    private val autocompleteService: AutocompleteService
) : RemoteAutocompleteRepository {
    override suspend fun getPlaces(userInput: String): AutocompleteResponse =
        autocompleteService.getPlaces(userInput = userInput)
}
