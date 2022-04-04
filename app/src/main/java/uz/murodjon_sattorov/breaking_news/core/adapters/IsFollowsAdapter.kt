package uz.murodjon_sattorov.breaking_news.core.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.helpers.getCompanyImgUrl
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.utils.IMG_BASE_URL
import uz.murodjon_sattorov.breaking_news.core.utils.IMG_SIZE
import uz.murodjon_sattorov.breaking_news.core.utils.LIMIT
import uz.murodjon_sattorov.breaking_news.databinding.RecommendedItemBinding

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 26/Mar/2022
 * @project Breaking News
 */

class IsFollowsAdapter : RecyclerView.Adapter<IsFollowsAdapter.ViewHolder>() {

    private var data = ArrayList<Source>()
    private var setOnClickListener: SetOnClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addData(d: List<Source>) {
        data.addAll(d)
        notifyDataSetChanged()
    }

    fun setOnClickListener(setOnClickListener: SetOnClickListener) {
        this.setOnClickListener = setOnClickListener
    }

    inner class ViewHolder(var binding: RecommendedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(source: Source) {
            Glide.with(binding.followImg.context)
                .load(IMG_BASE_URL + getCompanyImgUrl(source.url + IMG_SIZE))
                .placeholder(R.drawable.ic_loading_image_big)
                .into(binding.followImg)
            binding.followName.text = source.name
            binding.companyUrl.text = source.url

            if (!source.isChecked) {
                binding.followBtn.setBackgroundResource(R.drawable.follow_button_bg)
                binding.followBtn.text = binding.followBtn.context.getString(R.string.follow)
                binding.followBtn.setTextColor(Color.WHITE)
            } else {
                binding.followBtn.setBackgroundResource(R.drawable.following_button_bg)
                binding.followBtn.text = binding.followBtn.context.getString(R.string.following)
                binding.followBtn.setTextColor(Color.GRAY)
            }

            binding.followBtn.setOnClickListener {
                setOnClickListener?.onFollowItemClick(source)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecommendedItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.recommended_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return if (data.size < 10) {
            data.size
        } else {
            LIMIT
        }
    }

    fun clearData() {
        data.clear()
    }

    interface SetOnClickListener {
        fun onItemClick(source: Source)
        fun onFollowItemClick(source: Source)
    }
}