package app.placesautocomplete.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.di.IoDispatcher
import app.model.Failure
import app.model.Success
import app.placesautocomplete.core.AutocompleteUseCase
import app.placesautocomplete.remoterepository.model.AutocompleteResponse
import app.placesautocomplete.ui.model.GetPlacesFailureResponse
import app.placesautocomplete.ui.model.GetPlacesResponseState
import app.placesautocomplete.ui.model.Places
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class SubmitPlacesViewModel @ViewModelInject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val autocompleteUseCase: AutocompleteUseCase
) : ViewModel() {

    private val _getPlacesResponseState = MutableLiveData<GetPlacesResponseState>()
    val getPlacesResponseState: LiveData<GetPlacesResponseState> = _getPlacesResponseState
    private val _isUserInputValid = MutableLiveData<String>("")
    val isUserInputValid = _isUserInputValid

    fun setUserInput(userInput: String) {
        _isUserInputValid.postValue(userInput)
    }

    fun getPlacesForUserInput(userInput: String) {
        viewModelScope.launch(ioDispatcher) {
            val getPlacesResult = autocompleteUseCase.getPlaces(
                userInput = userInput
            )
            when (getPlacesResult) {
                is Success -> onGetPlacesSuccess(
                    getPlacesResult.data
                )
                is Failure -> onGetPlacesFailure(
                    getPlacesResult
                )
            }
        }
    }

    //MAP RESPONSE FROM SERVER TO LIST COMBINING MAIN AND SECONDARY TEXT IN ORDER
    // TO POPULATE AUTOCOMPLETE TEXTVIEW ADAPTER
    private fun onGetPlacesSuccess(places: List<AutocompleteResponse>) {
        _getPlacesResponseState.postValue(GetPlacesResponseState.GetPlacesSuccess(
            places = places.map {
                Places(
                    placeId = it.placeId,
                    mainText = it.mainLocationDescription,
                    secondaryText = it.secondaryLocationDescription
                )
            }
        )
        )
    }

    private fun onGetPlacesFailure(placesResult: Failure) {
        when (placesResult.error) {
            is IOException -> {
                _getPlacesResponseState.postValue(
                    GetPlacesResponseState.GetPlacesFailure(
                        GetPlacesFailureResponse.NO_INTERNET
                    )
                )
            }
            is HttpException -> {
                when (placesResult.error.code()) {
                    500 -> _getPlacesResponseState.postValue(
                        GetPlacesResponseState.GetPlacesFailure(
                            GetPlacesFailureResponse.SERVER_DOWN
                        )
                    )
                }
            }
            else -> _getPlacesResponseState.postValue(
                GetPlacesResponseState.GetPlacesFailure(
                    GetPlacesFailureResponse.UNKNOWN_ERROR
                )
            )
        }
    }
}

