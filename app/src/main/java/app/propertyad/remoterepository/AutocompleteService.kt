package app.propertyad.remoterepository

import app.propertyad.remoterepository.model.AutocompleteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AutocompleteService {
    @GET("autocomplete")
    suspend fun getPlaces(
        @Query("input") userInput: String
    ): AutocompleteResponse
}
