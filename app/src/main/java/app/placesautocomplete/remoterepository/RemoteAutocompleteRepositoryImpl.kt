package app.placesautocomplete.remoterepository

import app.placesautocomplete.remoterepository.model.AutocompleteResponse

class RemoteAutocompleteRepositoryImpl(private val autocompleteService: AutocompleteService) :
    RemoteAutocompleteRepository {

    override suspend fun getPlaces(userInput: String): AutocompleteResponse =
        autocompleteService.getPlaces(userInput = userInput)
}
