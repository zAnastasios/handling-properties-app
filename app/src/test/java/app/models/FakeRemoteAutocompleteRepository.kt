package app.models

import app.placesautocomplete.remoterepository.RemoteAutocompleteRepository
import app.placesautocomplete.remoterepository.model.AutocompleteResponse
import java.lang.IllegalStateException

class FakeRemoteAutocompleteRepository(
    private val autocompleteResponse: AutocompleteResponse? = null
) : RemoteAutocompleteRepository {
    override suspend fun getPlaces(userInput: String): AutocompleteResponse {
        return autocompleteResponse ?: throw IllegalStateException("testOnly")
    }
}
