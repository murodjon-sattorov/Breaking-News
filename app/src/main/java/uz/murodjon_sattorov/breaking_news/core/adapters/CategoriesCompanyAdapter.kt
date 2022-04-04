package uz.murodjon_sattorov.breaking_news.core.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.helpers.getCompanyImgUrl
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.utils.IMG_BASE_URL
import uz.murodjon_sattorov.breaking_news.core.utils.SMALL_IMG_SIZE
import uz.murodjon_sattorov.breaking_news.databinding.CategoryCompanyItemBinding

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 24/Mar/2022
 * @project Breaking News
 */

class CategoriesCompanyAdapter : RecyclerView.Adapter<CategoriesCompanyAdapter.ViewModel>() {

    private var data = ArrayList<Source>()
    private var setCompanyItemClickListener: SetCompanyItemClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addData(d: List<Source>) {
        data.addAll(d)
        notifyDataSetChanged()
    }

    fun setCompanyItemClickListener(setCompanyItemClickListener: SetCompanyItemClickListener) {
        this.setCompanyItemClickListener = setCompanyItemClickListener
    }

    inner class ViewModel(var binding: CategoryCompanyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(source: Source) {
            Glide.with(binding.companyImg.context)
                .load(IMG_BASE_URL + getCompanyImgUrl(source.url) + SMALL_IMG_SIZE)
                .placeholder(R.drawable.ic_loading_image_big)
                .into(binding.companyImg)
            binding.companyName.text = source.name
            binding.agoPostReleaseTime.text = source.url

            if (!source.isChecked) {
                binding.followText.setBackgroundResource(R.drawable.follow_text_background)
                binding.followText.text = binding.followText.context.getString(R.string.follow)
                binding.followText.setTextColor(Color.parseColor("#DC2326"))
            } else {
                binding.followText.setBackgroundResource(R.drawable.following_text_background)
                binding.followText.text = binding.followText.context.getString(R.string.following)
                binding.followText.setTextColor(Color.parseColor("#888888"))
            }

            binding.followText.setOnClickListener {
                setCompanyItemClickListener?.onItemFollowClick(source)
            }

            binding.itemView.setOnClickListener {
                setCompanyItemClickListener?.onItemClick(source)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        return ViewModel(
            CategoryCompanyItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_company_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun clearData() {
        data.clear()
    }

    interface SetCompanyItemClickListener {
        fun onItemClick(source: Source)
        fun onItemFollowClick(source: Source)
    }
}