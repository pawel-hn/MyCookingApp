package pawel.hn.mycookingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import pawel.hn.mycookingapp.databinding.RecipesBottomSheetBinding
import pawel.hn.mycookingapp.model.MealAndDietType
import pawel.hn.mycookingapp.utils.DEFAULT_DIET_TYPE
import pawel.hn.mycookingapp.utils.DEFAULT_MEAL_TYPE
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: RecipesBottomSheetBinding

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = RecipesBottomSheetBinding.inflate(inflater, container, false)

        mealTypeChipId = RecipesBottomSheetArgs.fromBundle(requireArguments()).mealTypeId
        dietTypeChipId = RecipesBottomSheetArgs.fromBundle(requireArguments()).dietTypeId
        mealTypeChip = RecipesBottomSheetArgs.fromBundle(requireArguments()).mealType
        dietTypeChip = RecipesBottomSheetArgs.fromBundle(requireArguments()).dietType


        updateChip(mealTypeChipId, binding.mealTypeChipGroup)
        updateChip(dietTypeChipId, binding.dietTypeChipGroup)

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

                val action = RecipesBottomSheetDirections
                    .actionRecipesBottomSheetToRecipesFragment(mealAndDietType)
                findNavController().navigate(action)

            }
        }
    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Timber.d(e.message)
            }
        }
    }

}