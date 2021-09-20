package pawel.hn.mycookingapp.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val url = DetailsFragmentArgs.fromBundle(requireArguments()).sourceUrl
        val binding = FragmentDetailsBinding.bind(view)

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.isVisible = false

        binding.apply {
            detailsWebView.webViewClient = WebViewClient()
            detailsWebView.loadUrl(url)

        }
    }
}