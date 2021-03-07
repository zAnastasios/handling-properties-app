package app.placesautocomplete.di

import app.devchallenge.propertyhandling.BuildConfig
import app.placesautocomplete.remoterepository.AutocompleteService
import app.placesautocomplete.remoterepository.RemoteAutocompleteRepository
import app.placesautocomplete.remoterepository.RemoteAutocompleteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object PlacesModule {

    @Singleton
    @Provides
    fun providesPlacesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesAutocompleteService(retrofit: Retrofit): AutocompleteService =
        retrofit.create()

    @Singleton
    @Provides
    fun providesAutocompleteRepository(autocompleteService: AutocompleteService): RemoteAutocompleteRepository =
        RemoteAutocompleteRepositoryImpl(autocompleteService = autocompleteService)
}
