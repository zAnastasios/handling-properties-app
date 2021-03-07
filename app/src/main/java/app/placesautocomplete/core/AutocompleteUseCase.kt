package app.placesautocomplete.core

import app.model.Failure
import app.model.Result
import app.model.Success
import app.placesautocomplete.remoterepository.RemoteAutocompleteRepository
import app.placesautocomplete.remoterepository.model.AutocompleteResponse
import javax.inject.Inject

class AutocompleteUseCase @Inject constructor(
    private val autocompleteRepository: RemoteAutocompleteRepository
) {

    suspend fun getPlaces(
        userInput: String
    ): Result<AutocompleteResponse> =
        try {
            val autocompleteResponse: AutocompleteResponse =
                autocompleteRepository.getPlaces(
                    userInput = userInput
                )
            Success(autocompleteResponse)
        } catch (error: Throwable) {
            Failure(error)
        }
}
