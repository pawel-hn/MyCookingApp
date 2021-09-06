package pawel.hn.mycookingapp.utils

sealed class Resource<T>(
        val data: T? = null,
        val error: Throwable? = null
) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null): Resource<T>(data, throwable)
    class Loading<T>: Resource<T>()
}