package app.placesautocomplete.ui

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import app.devchallenge.propertyhandling.R
import app.devchallenge.propertyhandling.databinding.ActivitySubmitPlaceBinding
import app.placesautocomplete.ui.model.GetPlacesFailureResponse
import app.placesautocomplete.ui.model.GetPlacesResponseState
import app.placesautocomplete.ui.model.Places
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubmitPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmitPlaceBinding
    private val submitPlacesViewModel: SubmitPlacesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmitPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        submitPlacesViewModel.getPlacesResponseState.observe(
            this, {
                handleGetPlacesResponse(getPlacesResponseState = it)
            }
        )
        submitPlacesViewModel.isUserInputValid.observe(
            this, {
                collectUserInput(userInput = it)
            }
        )

    }

    private fun initListeners() {
        binding.placesAutocompleteTextView.addTextChangedListener {
            submitPlacesViewModel.setUserInput(userInput = it.toString())
        }
    }

    private fun collectUserInput(userInput: String) {
        if (userInput.length > 2) {
            submitPlacesViewModel.getPlacesForUserInput(
                userInput = binding.placesAutocompleteTextView.text.toString()
            )
        } else {
            initAutocompleteAdapter(emptyList())
        }
    }

    private fun handleGetPlacesResponse(getPlacesResponseState: GetPlacesResponseState) {
        when (getPlacesResponseState) {
            is GetPlacesResponseState.GetPlacesSuccess -> initAutocompleteAdapter(
                getPlacesResponseState.places
            )
            is GetPlacesResponseState.GetPlacesFailure -> {
                showErrorMessage(getPlacesResponseState)
                initAutocompleteAdapter(emptyList())
            }
        }
    }

    private fun showErrorMessage(placesResponseState: GetPlacesResponseState.GetPlacesFailure) {
        when (placesResponseState.autocompleteFailureResponse) {
            GetPlacesFailureResponse.NO_INTERNET -> showSnackBarError(
                getString(R.string.no_internet_connection_en)
            )

            GetPlacesFailureResponse.SERVER_DOWN -> showSnackBarError(
                getString(R.string.server_down_error_en)
            )

            GetPlacesFailureResponse.UNKNOWN_ERROR -> showSnackBarError(
                getString(R.string.unknown_error_en)
            )
        }
    }

    private fun initAutocompleteAdapter(places: List<Places>) {
        var adapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            R.layout.places_item,
            listOf(
                getString(R.string.no_location_found_en)
            )
        )
        if (places.isNotEmpty()) {
            val placesCompact = places.map { place ->
                place.mainText + ", " + place.secondaryText
            }
            adapter = ArrayAdapter(this, R.layout.places_item, placesCompact)
        }
        binding.placesAutocompleteTextView.setAdapter(adapter)
    }

    private fun showSnackBarError(error: String) {
        Snackbar.make(
            binding.placesAutocompleteTextView,
            error,
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(getColor(R.color.red_alert))
            .setTextColor(getColor(R.color.white))
            .show()
    }
}
