package com.stash.shopeklobek.utils.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.stash.shopeklobek.R
import com.stash.shopeklobek.databinding.ItemAddressCardBinding


/**
 * TODO: document your custom view class.
 */
class AddressCardView : FrameLayout {

    fun setLocationImageResource(image:Int){
        locationImage = ContextCompat.getDrawable(context,image)
    }

    var locationImage:Drawable? = null
    var locationImageUrl:String? = null
    var title:String? = null
    var address:String? = null

    fun setImageFromUrl(url:String){
        locationImageUrl = url
        Glide.with(context).load(url)
            .into(binding.ivAddress)
    }

    lateinit var binding:ItemAddressCardBinding

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AddressCardView,
            0, 0).apply {

            try {
                locationImage = getDrawable(R.styleable.AddressCardView_locationImage)

                title = getString(R.styleable.AddressCardView_title)

                address = getString(R.styleable.AddressCardView_address)
            } finally {
                recycle()
            }
        }

        binding = ItemAddressCardBinding.bind(inflate(context, R.layout.item_address_card, this))

        if(locationImage == null&&locationImageUrl==null)
            binding.cvAddressCard.visibility = View.GONE
        else
        {
            binding.cvAddressCard.visibility = View.VISIBLE
            if(locationImageUrl==null)
                binding.ivAddress.setImageDrawable(locationImage)
        }

        binding.tvLocationTitle.text = title
        binding.tvAddress.text = address


    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.cvContainer.setOnClickListener(l)
    }


    fun refresh(){
        if(locationImage == null&&locationImageUrl==null)
            binding.cvAddressCard.visibility = View.GONE
        else
        {
            binding.cvAddressCard.visibility = View.VISIBLE
            if(locationImageUrl==null)
                binding.ivAddress.setImageDrawable(locationImage)
        }

        binding.tvLocationTitle.text = title
        binding.tvAddress.text = address
    }

    override fun onDraw(canvas: Canvas) {

        if(locationImage == null)
            binding.cvAddressCard.visibility = View.GONE
        else
        {
            binding.cvAddressCard.visibility = View.VISIBLE
            binding.ivAddress.setImageDrawable(locationImage)
        }

        binding.tvLocationTitle.text = title
        binding.tvAddress.text = address

        super.onDraw(canvas)
    }
}