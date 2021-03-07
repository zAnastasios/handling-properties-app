package app.placesautocomplete.core

import app.model.Failure
import app.model.Success
import app.models.FakeAutocompleteResponse
import app.models.FakeRemoteAutocompleteRepository
import io.mockk.spyk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class AutocompleteUseCaseTest {

    @Test
    fun `test valid autocomplete response from server handled correctly`() = runBlockingTest {
        // GIVEN
        val fakeRemoteAutocompleteRepository = spyk(
            FakeRemoteAutocompleteRepository(
                autocompleteResponse = FakeAutocompleteResponse.getFakeResponsesPlacesResponses(
                    numOfPlaces = 4,
                    mainLocation = "Nafplio"
                )
            )
        )

        val autocompleteUseCase = AutocompleteUseCase(
            autocompleteRepository = fakeRemoteAutocompleteRepository
        )

        // WHEN
        val response = autocompleteUseCase.getPlaces("some")

        // THEN
        assert(response is Success)
    }

    @Test
    fun `test invalid autocomplete response from server handled correctly`() = runBlockingTest {
        // GIVEN
        val fakeRemoteAutocompleteRepository = spyk(
            FakeRemoteAutocompleteRepository(
                autocompleteResponse = null
            )
        )
        val autocompleteUseCase = AutocompleteUseCase(
            autocompleteRepository = fakeRemoteAutocompleteRepository
        )

        // WHEN
        val response = autocompleteUseCase.getPlaces("some")

        // THEN
        assert(response is Failure)
    }
}

