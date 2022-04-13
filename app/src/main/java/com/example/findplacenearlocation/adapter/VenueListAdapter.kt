package com.example.findplacenearlocation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.findplacenearlocation.R
import com.example.findplacenearlocation.model.Venue
import kotlinx.android.synthetic.main.location_result_item.view.*

class VenueListAdapter : RecyclerView.Adapter<VenueListAdapter.CustomView>() {

    inner class CustomView(view: View) : RecyclerView.ViewHolder(view)

    private val differCallback = object : DiffUtil.ItemCallback<Venue>() {
        override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomView = CustomView(
        LayoutInflater.from(parent.context).inflate(R.layout.location_result_item, parent, false)
    )

    private var onItemClickListener: ((Venue) -> Unit)? = null

    override fun onBindViewHolder(holder: CustomView, position: Int) {
        val venue = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(buildIconPath(venue)).into(locationImage)
            locationName.text = venue.name
            locationCategory.text = venue.categories.firstOrNull()?.pluralName

            setOnClickListener {
                onItemClickListener?.let { it(venue) }
            }
        }
    }

    fun setOnItemClickListener(listener: (Venue) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    @NonNull
    private fun buildIconPath(venue: Venue): String {
        venue.categories?.firstOrNull()?.icon?.let {
            if (!it.prefix.isNullOrBlank() && !it.suffix.isNullOrBlank()) {
                return it.prefix + "88" + it.suffix
            }
        }

        return ""
    }
}