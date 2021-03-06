package com.stash.shopeklobek.ui.home.favorites

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.stash.shopeklobek.R
import com.stash.shopeklobek.model.entities.Products
import com.stash.shopeklobek.model.entities.room.RoomFavorite
import com.stash.shopeklobek.ui.home.brands.VendorFragmentDirections
import com.stash.shopeklobek.utils.Constants
import com.stash.shopeklobek.utils.toCurrency

class AdapterFavorite(
    var listFavorites: List<RoomFavorite>,
    var setOnClickListener:((RoomFavorite)->Unit)?=null,
    var setNavigation:((Products)->Unit)?=null
) :
    RecyclerView.Adapter<AdapterFavorite.ViewHolder>() {


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val tvTitle: TextView
            get() = view.findViewById(R.id.tv_title_item)
        val tvPrice: TextView
            get() = view.findViewById(R.id.tv_price_item)
        val imageItem: ImageView
            get() = view.findViewById(R.id.image_product_item)
        val ivDeleteFavorite: ImageView
            get() = view.findViewById(R.id.iv_delete_favorite)
        val content_favorite: CardView
            get() = view.findViewById(R.id.content_favorite)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viwe: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(viwe)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(holder.imageItem.context).load(listFavorites.get(position).product.image.src)
            .into(holder.imageItem)
        holder.tvTitle.text = listFavorites.get(position).product.title
        holder.tvPrice.text =
            listFavorites.get(position).product.variants[0]?.price?.toCurrency(holder.imageItem.context)

        val context = holder.itemView.context
        holder.ivDeleteFavorite.setOnClickListener {

            AlertDialog.Builder(holder.imageItem.context).apply {
                setNegativeButton(context.getString(R.string.no)) { d, i ->
                    d.dismiss()
                }

                setPositiveButton(context.getString(R.string.yes)) { d, i ->
                    setOnClickListener?.invoke(listFavorites.get(position))
                    Toast.makeText(
                        holder.imageItem.context,
                        holder.imageItem.context.getString(R.string.product_deleted),
                        Toast.LENGTH_LONG
                    ).show()
                    notifyDataSetChanged()
                    d.dismiss()
                }

                setTitle(holder.imageItem.context.getString(R.string.do_u_want_remove_product))
            }.create().show()


        }

        holder.content_favorite.setOnClickListener {
            setNavigation?.invoke(listFavorites[position].product)
        }
        // open product details fragment



    }

    fun setFavorite(favorite: List<RoomFavorite>) {
            this.listFavorites = favorite
            notifyDataSetChanged()
    }


    override fun getItemCount(): Int = listFavorites.size
}