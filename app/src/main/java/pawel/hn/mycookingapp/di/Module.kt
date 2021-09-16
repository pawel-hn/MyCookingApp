package pawel.hn.mycookingapp.di

import android.app.Application
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pawel.hn.mycookingapp.database.RecipesDao
import pawel.hn.mycookingapp.database.RecipesDatabase
import pawel.hn.mycookingapp.database.RemoteKeysDao
import pawel.hn.mycookingapp.database.SavedRecipesDao
import pawel.hn.mycookingapp.utils.BASE_URL
import pawel.hn.mycookingapp.network.RecipesApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {


    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun providesRecipesApi(retrofit: Retrofit) = retrofit.create(RecipesApi::class.java)

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()


    @Provides
    @Singleton
    fun providesDatabase(app: Application): RecipesDatabase = Room.databaseBuilder(
        app,
        RecipesDatabase::class.java,
        "recipesDatabase"
    )
        .allowMainThreadQueries()
        .build()


    @Provides
    @Singleton
    fun providesSavedRecipesDao(database: RecipesDatabase): SavedRecipesDao = database.savedRecipesDao()

    @Provides
    @Singleton
    fun providesRecipesDao(database: RecipesDatabase): RecipesDao = database.recipesDao()

    @Provides
    @Singleton
    fun providesRemoteKeysDao(database: RecipesDatabase): RemoteKeysDao = database.remoteKeysDao()
}