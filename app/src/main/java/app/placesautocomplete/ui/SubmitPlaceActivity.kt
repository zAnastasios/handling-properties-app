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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import app.devchallenge.propertyhandling.R
import app.devchallenge.propertyhandling.databinding.ActivitySubmitPlaceBinding
import app.placesautocomplete.ui.model.GetPlacesFailureResponse
import app.placesautocomplete.ui.model.GetPlacesResponseState
import app.placesautocomplete.ui.model.Places
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

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
        binding.clearButton.setOnClickListener {
            clearAllFields()
        }
        binding.submitButton.setOnClickListener {
            checkIfPlaceIsValid()
        }
    }

    private fun checkIfPlaceIsValid() {
        if (binding.submitPlaceTitleTextInputEditText.text.toString().isEmpty()) {
            binding.submitPlaceTitleTextInputLayout.error =
                getString(R.string.error_field_is_required)
            return
        }
        if (
            binding.placesAutocompleteTextView.text.toString().isEmpty() ||
            binding.placesAutocompleteTextView.text.toString() == getString
                (
                R.string.no_location_found_en
            )
        ) {
            binding.placesDropdownTextInputLayout.error =
                getString(R.string.error_field_is_required)
            return
        }
        submitPlace(
            title = binding.submitPlaceTitleTextInputEditText.text.toString(),
            location = binding.placesAutocompleteTextView.text.toString(),
            price = binding.submitPlacePriceTextInputEditText.text.toString(),
            description = binding.submitPlaceDescriptionTextInputEditText.text.toString(),
        )
    }

    private fun submitPlace(
        title: String,
        location: String,
        price: String,
        description: String
    ) {
        val rootObject = JSONObject()
        rootObject.put("title", title)
        rootObject.put("location", location)
        rootObject.put("price", price)
        rootObject.put("description", description)
        AlertDialog.Builder(this)
            .setTitle("Final listing")
            .setMessage(rootObject.toString())
            .setPositiveButton(
                android.R.string.ok,
                null //Instead of adding null listener in order ot dismiss, we could add saving locally functionality through the ViewModel
            )
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun clearAllFields() {
        binding.submitPlaceTitleTextInputEditText.setText("")
        binding.placesAutocompleteTextView.setText("")
        initAutocompleteAdapter(emptyList())
        binding.submitPlacePriceTextInputEditText.setText("")
        binding.submitPlaceDescriptionTextInputEditText.setText("")
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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}
