package pawel.hn.mycookingapp.utils

import android.content.Context
import android.util.Patterns
import android.widget.Toast

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun showToast(context: Context, msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()