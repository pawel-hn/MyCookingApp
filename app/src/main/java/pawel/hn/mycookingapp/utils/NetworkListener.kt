package pawel.hn.mycookingapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkListener : ConnectivityManager.NetworkCallback() {

    private val isNetworkAvailable = MutableStateFlow(false)


    fun checkNetworkAvailability(context: Context): MutableStateFlow<Boolean> {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        cm.registerDefaultNetworkCallback(this)

        var isConnected = false

        cm.allNetworks.forEach {
            val networkCapability = cm.getNetworkCapabilities(it)
            networkCapability?.let {capability ->
               if (capability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                   isConnected = true
                   return@forEach
               }
            }
        }
        isNetworkAvailable.value = isConnected

        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable.value = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable.value = false
    }
}