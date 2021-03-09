# Property Handling App

## Purpose

The purpose of this App is to make a property handling app where the user can submit a new property at a specific location.
The main focus is the location Autocomplete feature that consumes property location through an API and feeds them dynamically
to the AutocompleteTextView.

## Know issues / Next steps (In random order)

1. The UI needs refactoring since it is no good looking nor is it "functional" **Backlog**
2. Some business logic is handled on the activity instead of the Viewmodel with is bad practise **Backlog**
3. There needs to be a more concise naming convention in order to be more self explanatory (e.g "property" is also referred to as "place" in some cases) **Backlog**
4. Add more Unit test mainly on the ViewModel **Backlog**
5. Add some form of instrumental tesing (ideally using Robot pattern) **Backlog**
6. Add a design system module for the App **Backlog**
7. Add device-level caching on the autocomplete API level to ensure that you minimize the number of calls made to the service. **Backlog**
