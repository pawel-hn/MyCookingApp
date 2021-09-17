package pawel.hn.mycookingapp.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import pawel.hn.mycookingapp.model.MealAndDietType
import pawel.hn.mycookingapp.utils.*
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton




@Singleton
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context) {

    private val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore(name = PREFERENCE_NAME)

    private object PreferenceKeys {

        val selectedMealType = stringPreferencesKey(PREFERENCE_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCE_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCE_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCE_DIET_TYPE_ID)
        val backOnLine = booleanPreferencesKey(PREFERENCE_BACK_ONLINE)
    }

    val readMealAndDietType: Flow<MealAndDietType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->

            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0

            Timber.d("PHN, store: $selectedMealType, $selectedDietType")
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    suspend fun saveMealAndDietType(mealAndDietType: MealAndDietType) {
        Timber.d("PHN, store saved")
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealAndDietType.mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealAndDietType.mealTypeId
            preferences[PreferenceKeys.selectedDietType] = mealAndDietType.dietType
            preferences[PreferenceKeys.selectedDietTypeId] = mealAndDietType.dietTypeId
            Timber.d("PHN, edit done ${mealAndDietType.mealType}"
            )
        }
    }

    suspend fun saveBackOnLine(backOnLine: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnLine] = backOnLine
        }
    }

    val readBackOnLine  = context.dataStore.data
        .catch{ exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnLine] ?: false
            backOnline
        }
}
