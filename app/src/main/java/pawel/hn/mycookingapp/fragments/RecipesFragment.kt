package pawel.hn.mycookingapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.activity.MainActivity
import pawel.hn.mycookingapp.adapters.RecipesAdapter
import pawel.hn.mycookingapp.adapters.RecipesLoadStateAdapter
import pawel.hn.mycookingapp.databinding.FragmentRecipesBinding
import pawel.hn.mycookingapp.model.Recipe
import pawel.hn.mycookingapp.utils.DEFAULT_DIET_TYPE
import pawel.hn.mycookingapp.utils.DEFAULT_MEAL_TYPE
import pawel.hn.mycookingapp.utils.Resource
import pawel.hn.mycookingapp.viewmodels.RecipesViewModel
import timber.log.Timber
import javax.inject.Inject

@InternalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes), RecipesAdapter.RecipesOnClickListener {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private var mealTypeChipId = 0
    private var dietTypeChipId = 0
    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    private val recipesViewModel: RecipesViewModel by viewModels()
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipesAdapter: RecipesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mealAndDietType = RecipesFragmentArgs.fromBundle(requireArguments()).mealAndDietType
        mealAndDietType?.let {
            recipesViewModel.saveRecipesTypes(it)
        }
        recipesViewModel.getQueries(mealAndDietType)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecipesBinding.bind(view).apply {
            textViewRecipesHeader.text =
                getString(R.string.logged_as, firebaseAuth.currentUser?.email)
        }

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.isVisible = true

        setAdapter()
        subscribeToObservers()
        subscribeToListeners()
        setUiStates()
        setHasOptionsMenu(true)
    }


    private fun subscribeToObservers() {
        recipesViewModel.mealAndDietTypeLiveData.asLiveData().observe(viewLifecycleOwner) {
            mealTypeChipId = it.mealTypeId
            dietTypeChipId = it.dietTypeId
            mealType = it.mealType
            dietType = it.dietType

            Timber.d("observer: $mealType PHN")
        }

        recipesViewModel.recipesLiveData.observe(viewLifecycleOwner) {

            recipesAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    private fun subscribeToListeners() {
        binding.apply {
            buttonRecipesRetry.setOnClickListener {
                recipesAdapter.retry()
            }

            recipesFab.setOnClickListener {
                val action = RecipesFragmentDirections
                    .actionRecipesFragmentToRecipesBottomSheet(
                        mealTypeChipId, dietTypeChipId, mealType, dietType)
                findNavController().navigate(action)
            }
        }
    }

    private fun setAdapter() {
        val list = recipesViewModel.savedRecipes

        recipesAdapter = RecipesAdapter(this, list)

        binding.apply {
            recipesRecyclerView.setHasFixedSize(true)
            recipesRecyclerView.adapter = recipesAdapter.withLoadStateHeaderAndFooter(
                header = RecipesLoadStateAdapter { recipesAdapter.retry() },
                footer = RecipesLoadStateAdapter { recipesAdapter.retry() }
            )
        }
    }

    private fun setUiStates() {
        recipesAdapter.addLoadStateListener { loadState ->
            binding.apply {
                recipesProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recipesRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRecipesRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewRecipesError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    recipesAdapter.itemCount < 1
                ) {
                    recipesRecyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_log_out -> {
                recipesViewModel.logOut()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onClickRecipe(url: String) {
        val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsFragment(url)
        findNavController().navigate(action)
    }

    override fun onClickAdd(recipe: Recipe) {
        val action = RecipesFragmentDirections.actionRecipesFragmentToAddFavouriteDialog(recipe)
        findNavController().navigate(action)
    }
}