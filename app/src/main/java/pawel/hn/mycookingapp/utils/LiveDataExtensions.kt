package pawel.hn.mycookingapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> LiveData<T>.toMutableLiveData(): MutableLiveData<T> {
    return this as MutableLiveData<T>
}