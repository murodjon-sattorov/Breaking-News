package uz.murodjon_sattorov.breaking_news.core.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.helpers.DateUtil
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
 * @date 26/Mar/2022
 * @project Breaking News
 */
class FollowNewsVerticalList : RecyclerView.Adapter<FollowNewsVerticalList.ViewHolder>() {

    private var data = ArrayList<Article>()
    var setOnNewsClickListener: SetOnNewsClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addData(article: List<Article>) {
        data.addAll(article)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var binding: TopHeadlineItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(article: Article) {

            binding.newsImage.transitionName = "news_image_$position"

            binding.companyName.text = article.source.name
            binding.postTitle.text = article.title
            Glide.with(binding.companyImg.context)
                .load(IMG_BASE_URL + article.url?.let { getCompanyImgUrl(it) } + SMALL_IMG_SIZE)
                .placeholder(R.drawable.ic_loading_image_big)
                .into(binding.companyImg)
            binding.postReleaseDate.text = article.publishedAt?.let { DateUtil.getPostTime(it) }

            @SuppressLint("CheckResult")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Glide.with(binding.newsImage.context).load(article.urlToImage)
                    .placeholder(dayAndNight(binding.newsImage.context)).into(binding.newsImage)
            } else {
                Glide.with(binding.newsImage.context).load(article.urlToImage)
                    .placeholder(R.drawable.ic_loading_image_big)
            }

            article.source.id = "news_image_$position"

            binding.newsItemClick.setOnClickListener {
                setOnNewsClickListener?.onItemClick(article, binding.newsImage, position)
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
    }

    override fun getItemCount(): Int = data.size

    interface SetOnNewsClickListener {
        fun onItemClick(article: Article, imageView: ImageView, position: Int)
    }

}