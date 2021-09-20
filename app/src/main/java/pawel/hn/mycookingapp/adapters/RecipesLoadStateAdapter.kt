package pawel.hn.mycookingapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import pawel.hn.mycookingapp.databinding.LoaderStateRecipesBinding

class RecipesLoadStateAdapter (private val retry:() -> Unit)
    : LoadStateAdapter<RecipesLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoaderStateRecipesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LoadStateViewHolder((binding))
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: LoaderStateRecipesBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(loadState: LoadState) {

                binding.apply {
                    progressBar.isVisible = loadState is LoadState.Loading
                    buttonRetry.isVisible = loadState !is LoadState.Loading
                    textViewError.isVisible = loadState !is LoadState.Loading

                    buttonRetry.setOnClickListener {
                        retry.invoke()
                    }
                }
            }
        }
}