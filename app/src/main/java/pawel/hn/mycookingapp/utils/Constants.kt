package pawel.hn.mycookingapp.utils

const val REGISTER_RESULT = "registerUser"
const val RESET_RESULT = "resetPassword"
const val FRAGMENT_RESULT_KEY = "result"
const val REGISTER_RESULT_BUNDLE_KEY = "result"

const val BASE_URL ="https://api.spoonacular.com/"

//API query
const val QUERY_SEARCH = "query"
const val QUERY_NUMBER = "number"
const val QUERY_API_KEY = "apiKey"
const val QUERY_MEAL = "type"
const val QUERY_DIET = "diet"
const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
const val QUERY_FILL_INGREDIENTS = "fillIngredients"
const val QUERY_OFFSET = "offset"

const val DEFAULT_MEAL_TYPE = "main course"
const val DEFAULT_DIET_TYPE = "Whole30"


//firestore fields
const val FIELD_ID = "id"
const val FIELD_TITLE = "title"
const val FIELD_COMMENT = "comment"
const val FIELD_SOURCE_URL = "sourceUrl"
const val FIELD_IMAGE = "image"
const val FIELD_RATING = "rating"
const val FIELD_COOKED = "cooked"
const val FIELD_READY_IN_MINS = "readInMins"
const val FIELD_VEGE = "vege"

//datastore
const val PREFERENCE_NAME = "food_preference"
const val PREFERENCE_MEAL_TYPE = "mealType"
const val PREFERENCE_MEAL_TYPE_ID = "mealTypeId"
const val PREFERENCE_DIET_TYPE = "dietType"
const val PREFERENCE_DIET_TYPE_ID = "dietTypeId"
const val PREFERENCE_BACK_ONLINE = "backOnline"
