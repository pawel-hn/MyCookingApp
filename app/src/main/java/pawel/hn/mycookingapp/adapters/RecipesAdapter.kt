package pawel.hn.mycookingapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.databinding.FragmentRecipesItemBinding
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.model.Recipe

class RecipesAdapter
    (
    private val listener: RecipesOnClickListener,
    private val list: List<FavouriteRecipe>,
) : PagingDataAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(MyDiffUtil()) {

    interface RecipesOnClickListener {
        fun onClickRecipe(url: String)
        fun onClickAdd(recipe: Recipe)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = getItem(position)
        recipe?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentRecipesItemBinding.inflate(inflater, parent, false)
        return RecipeViewHolder(binding)
    }

    inner class RecipeViewHolder(private val binding: FragmentRecipesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.apply {
                textViewTitle.text = recipe.title

                textViewTime.text =
                    itemView.context.getString(R.string.minutes, recipe.readyInMinutes)

                imageViewVege.setColorFilter(
                    if (recipe.vegetarian) Color.GREEN else Color.GRAY
                )
                textViewVege.setTextColor(
                    if (recipe.vegetarian) Color.GREEN else Color.GRAY
                )

                Glide.with(itemView).load(recipe.image)
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(imageView)


                itemView.setOnClickListener {
                    listener.onClickRecipe(recipe.sourceUrl)
                }

                val item = list.find { it.id == recipe.id }
                buttonAdd.isEnabled = item == null
                if (buttonAdd.isEnabled) {
                    buttonAdd.apply {
                        setImageDrawable(
                            AppCompatResources.getDrawable(root.context, R.drawable.ic_add)
                        )
                        setOnClickListener {
                            listener.onClickAdd(recipe)
                        }
                    }
                } else {
                    buttonAdd.setImageDrawable(
                        AppCompatResources.getDrawable(root.context, R.drawable.ic_ok)
                    )
                }
            }
        }
    }
}

class MyDiffUtil : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}