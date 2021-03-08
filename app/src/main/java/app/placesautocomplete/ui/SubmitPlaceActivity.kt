package app.placesautocomplete.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import app.devchallenge.propertyhandling.R
import app.devchallenge.propertyhandling.databinding.ActivitySubmitPlaceBinding
import app.placesautocomplete.ui.model.GetPlacesResponseState
import app.placesautocomplete.ui.model.Places
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

    private fun collectUserInput(userInput: String) {
        if (userInput.length > 2) {
            submitPlacesViewModel.getPlacesForUserInput(
                userInput = binding.placesAutocompleteTextView.text.toString()
            )
        }
    }

    private fun handleGetPlacesResponse(getPlacesResponseState: GetPlacesResponseState) {
        when (getPlacesResponseState) {
            is GetPlacesResponseState.GetPlacesSuccess -> initAutocompleteAdapter(
                getPlacesResponseState.places
            )
            is GetPlacesResponseState.GetPlacesFailure -> showErrorMessage(
                getPlacesResponseState
            )
        }
    }

    private fun showErrorMessage(placesResponseState: GetPlacesResponseState.GetPlacesFailure) {
        TODO("Not yet implemented")
    }

    private fun initAutocompleteAdapter(places: List<Places>) {
        val placesCompact = places.map { places ->
            places.mainText + ", " + places.secondaryText
        }
        val adapter = ArrayAdapter(this, R.layout.places_item, placesCompact)
        binding.placesAutocompleteTextView.setAdapter(adapter)
    }


    private fun initListeners() {
        binding.placesAutocompleteTextView.addTextChangedListener {
            submitPlacesViewModel.setUserInput(userInput = it.toString())
        }
    }
}
