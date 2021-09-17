package pawel.hn.mycookingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import pawel.hn.mycookingapp.databinding.RecipesBottomSheetBinding
import pawel.hn.mycookingapp.model.MealAndDietType
import pawel.hn.mycookingapp.utils.DEFAULT_DIET_TYPE
import pawel.hn.mycookingapp.utils.DEFAULT_MEAL_TYPE
import pawel.hn.mycookingapp.viewmodels.RecipesViewModel
import java.util.*

@InternalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: RecipesBottomSheetBinding

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    private val recipesViewModel: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = RecipesBottomSheetBinding.inflate(inflater, container, false)

            recipesViewModel.mealAndDietTypeLiveData.observe(viewLifecycleOwner) {
                mealTypeChipId = it.mealTypeId
                dietTypeChipId = it.dietTypeId
                updateChip(mealTypeChipId, binding.mealTypeChipGroup)
                updateChip(dietTypeChipId, binding.dietTypeChipGroup)
            }
        subscribeToListeners()
        return binding.root
    }

    private fun subscribeToListeners() {
        binding.apply {

            mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
                val chip = group.findViewById<Chip>(checkedId)
                mealTypeChip = chip.text.toString().lowercase(Locale.getDefault())
                mealTypeChipId = checkedId
            }

            dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
                val chip = group.findViewById<Chip>(checkedId)
                dietTypeChip = chip.text.toString().lowercase(Locale.getDefault())
                dietTypeChipId = checkedId
            }

            buttonApply.setOnClickListener {

                val mealAndDietType = MealAndDietType(
                    mealTypeChip, mealTypeChipId, dietTypeChip, dietTypeChipId
                )

                recipesViewModel.saveRecipesTypes(mealAndDietType)

                val action = RecipesBottomSheetDirections
                    .actionRecipesBottomSheetToRecipesFragment()
                findNavController().navigate(action)

            }
        }
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
        }
    }
}