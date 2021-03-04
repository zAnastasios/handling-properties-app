package app.propertyad.remoterepository.model

import app.propertyad.remoterepository.AutocompleteService
import app.propertyad.remoterepository.RemoteAutocompleteRepository

class RemoteAutocompleteRepositoryImpl(
    private val autocompleteService: AutocompleteService
) : RemoteAutocompleteRepository {
    override suspend fun getPlaces(userInput: String): AutocompleteResponse =
        autocompleteService.getPlaces(userInput = userInput)
}
