package pawel.hn.mycookingapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.databinding.FragmentSaveItemBinding
import pawel.hn.mycookingapp.model.FavouriteRecipe

class SavedRecipesAdapter(private val listener: SavedRecipesListener) :
    ListAdapter<FavouriteRecipe, SavedRecipesAdapter.SavedRecipesViewHolder>(
        SavedRecipesDiffCallback()) {

    interface SavedRecipesListener {
        fun onClickEdit(favouriteRecipe: FavouriteRecipe)
        fun onClickRecipe(url: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipesViewHolder {
        val binding = FragmentSaveItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavedRecipesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedRecipesViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)

        holder.binding.apply {

            val constrainSet = ConstraintSet()

            buttonComment.setOnClickListener {
                if (!textViewComment.isVisible) {
                    textViewComment.apply {
                        visibility = View.VISIBLE
                        text =
                            if (recipe.comment.isEmpty()) "   --- no comments ---" else recipe.comment
                    }

                    buttonComment.setImageDrawable(AppCompatResources
                        .getDrawable(root.context, R.drawable.ic_up))

                    constrainSet.run {
                        clone(constrainLayoutSave)
                        clear(R.id.imageView_saved, ConstraintSet.BOTTOM)
                        connect(R.id.imageView_saved,
                            ConstraintSet.BOTTOM,
                            R.id.text_view_comment,
                            ConstraintSet.TOP)
                        applyTo(constrainLayoutSave)
                    }

                } else {
                    textViewComment.visibility = View.GONE

                    buttonComment.setImageDrawable(AppCompatResources
                        .getDrawable(root.context, R.drawable.ic_down))

                    constrainSet.run {
                        clone(constrainLayoutSave)
                        clear(R.id.imageView_saved, ConstraintSet.BOTTOM)
                        connect(R.id.imageView_saved,
                            ConstraintSet.BOTTOM,
                            root.id,
                            ConstraintSet.BOTTOM)
                        applyTo(constrainLayoutSave)
                    }
                }
            }
        }
    }

    inner class SavedRecipesViewHolder(val binding: FragmentSaveItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favouriteRecipe: FavouriteRecipe) {

            binding.apply {
                textViewTitle.text = favouriteRecipe.title

                ratingBar.rating = favouriteRecipe.rating.toFloat()

                if (favouriteRecipe.cooked) {
                    textViewCooked.text = binding.root.context.getString(R.string.cooked)
                    textViewCooked.setTextColor(Color.GREEN)
                } else {
                    textViewCooked.text = binding.root.context.getString(R.string.not_cooked)
                    textViewCooked.setTextColor(Color.RED)
                }

                Glide.with(itemView)
                    .load(favouriteRecipe.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(imageViewSaved)

                buttonEdit.setOnClickListener {
                    listener.onClickEdit(favouriteRecipe)
                }
                itemView.setOnClickListener {
                    listener.onClickRecipe(favouriteRecipe.sourceUrl)
                }

            }
        }
    }

    class SavedRecipesDiffCallback : DiffUtil.ItemCallback<FavouriteRecipe>() {
        override fun areItemsTheSame(oldItem: FavouriteRecipe, newItem: FavouriteRecipe): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: FavouriteRecipe,
            newItem: FavouriteRecipe,
        ): Boolean = oldItem == newItem
    }
}