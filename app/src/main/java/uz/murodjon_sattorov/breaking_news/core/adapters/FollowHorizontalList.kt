package uz.murodjon_sattorov.breaking_news.core.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.helpers.getCompanyImgUrl
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.utils.IMG_BASE_URL
import uz.murodjon_sattorov.breaking_news.core.utils.IMG_SIZE
import uz.murodjon_sattorov.breaking_news.databinding.FollowsItemBinding

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 26/Mar/2022
 * @project Breaking News
 */

class FollowHorizontalList : RecyclerView.Adapter<FollowHorizontalList.ViewHolder>() {

    private var data = ArrayList<Source>()

    @SuppressLint("NotifyDataSetChanged")
    fun addData(source: List<Source>) {
        data.addAll(source)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var binding: FollowsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(source: Source) {
            binding.followName.text = source.name
            Glide.with(binding.followImg.context)
                .load(IMG_BASE_URL + getCompanyImgUrl(source.url + IMG_SIZE))
                .placeholder(R.drawable.ic_loading_image_big)
                .into(binding.followImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FollowsItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.follows_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int = data.size
}