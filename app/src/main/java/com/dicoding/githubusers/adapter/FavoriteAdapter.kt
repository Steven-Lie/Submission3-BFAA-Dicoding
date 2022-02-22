package com.dicoding.githubusers.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubusers.database.Favorite
import com.dicoding.githubusers.databinding.ItemUsersBinding
import com.dicoding.githubusers.ui.detailUser.DetailUserActivity
import com.dicoding.githubusers.ui.favorite.FavoriteDiffCallback

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listFavorites = ArrayList<Favorite>()

    fun setListFavorites(listFavorite: List<Favorite>){
        val diffCallback = FavoriteDiffCallback(this.listFavorites, listFavorite)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listFavorites.clear()
        this.listFavorites.addAll(listFavorite)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class FavoriteViewHolder(private val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite){
            Glide.with(itemView.context)
                .load(favorite.avatar)
                .circleCrop()
                .into(binding.imgProfile)

            binding.tvUsername.text = favorite.username
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailUserActivity::class.java)
                intent.putExtra(DetailUserActivity.EXTRA_USERNAME, favorite.username)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorites[position])
    }

    override fun getItemCount(): Int {
        return listFavorites.size
    }
}