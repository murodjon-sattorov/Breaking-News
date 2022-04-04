package uz.murodjon_sattorov.breaking_news.core.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.helpers.DateUtil.Companion.getPostTime
import uz.murodjon_sattorov.breaking_news.core.helpers.dayAndNight
import uz.murodjon_sattorov.breaking_news.core.helpers.getCompanyImgUrl
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.core.utils.IMG_BASE_URL
import uz.murodjon_sattorov.breaking_news.core.utils.SMALL_IMG_SIZE
import uz.murodjon_sattorov.breaking_news.databinding.TopHeadlineItemBinding

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 22/Mar/2022
 * @project Breaking News
 */
class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private var data = ArrayList<Article>()
    var setOnNewsClickListener: SetOnNewsClickListener? = null

    fun addData(d: List<Article>) {
        data.addAll(d)
        notifyItemRangeInserted(data.size - d.size, data.size)
    }

    inner class ViewHolder(var binding: TopHeadlineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(article: Article) {

            binding.newsImage.transitionName = "news_image_$position"
            binding.companyName.transitionName = article.source.name

            binding.companyName.text = article.source.name
            binding.postTitle.text = article.title
            Glide.with(binding.companyImg.context)
                .load(IMG_BASE_URL + article.url?.let { getCompanyImgUrl(it) } + SMALL_IMG_SIZE)
                .placeholder(R.drawable.ic_loading_image_big)
                .into(binding.companyImg)
            binding.postReleaseDate.text = article.publishedAt?.let { getPostTime(it) }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Glide.with(binding.newsImage.context).load(article.urlToImage)
                    .placeholder(dayAndNight(binding.newsImage.context)).into(binding.newsImage)
            } else {
                Glide.with(binding.newsImage.context).load(article.urlToImage)
                    .placeholder(R.drawable.ic_loading_image_big)
                    .into(binding.newsImage)
            }

            article.source.id = "news_image_$position"

            binding.newsItemClick.setOnClickListener {
                setOnNewsClickListener?.onItemClick(
                    article,
                    binding.newsImage,
                    position
                )
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TopHeadlineItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.top_headline_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
        if (data.size - 1 == position) {
            setOnNewsClickListener?.onNextPage()
        }
    }

    fun clearData() {
        data.clear()
    }

    override fun getItemCount(): Int = data.size

    interface SetOnNewsClickListener {
        fun onItemClick(article: Article, imageView: ImageView, position: Int)
        fun onNextPage()
    }

}