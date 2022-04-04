package uz.murodjon_sattorov.breaking_news.core.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.helpers.DateUtil
import uz.murodjon_sattorov.breaking_news.core.helpers.dayAndNight
import uz.murodjon_sattorov.breaking_news.core.helpers.getCompanyImgUrl
import uz.murodjon_sattorov.breaking_news.core.models.about.AboutModel
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.core.utils.IMG_BASE_URL
import uz.murodjon_sattorov.breaking_news.core.utils.IMG_SIZE
import uz.murodjon_sattorov.breaking_news.core.utils.SMALL_IMG_SIZE
import uz.murodjon_sattorov.breaking_news.databinding.AboutItemBinding
import uz.murodjon_sattorov.breaking_news.databinding.TopHeadlineItemBinding

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 30/Mar/2022
 * @project Breaking News
 */

class AboutAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = ArrayList<BaseModel>()
    var setOnNewsClickListener: SetOnNewsClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addData(d: List<BaseModel>) {
        data.addAll(d)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        data.let {
            return it[position].getType()
        }

        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BaseModel.TYPE_ONE -> {
                AboutCompanyHolder(AboutItemBinding.inflate(LayoutInflater.from(parent.context)))
            }
            else -> {
                AboutCompanyNewsHolder(TopHeadlineItemBinding.inflate(LayoutInflater.from(parent.context)))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            BaseModel.TYPE_ONE -> {
                (holder as AboutCompanyHolder).bindData(data[position] as AboutModel)
            }
            BaseModel.TYPE_TWO -> {
                (holder as AboutCompanyNewsHolder).bindData(data[position] as Article)
            }
        }
        if (data.size - 2 == position) {
            setOnNewsClickListener?.onNextPage()
        }
    }

    override fun getItemCount(): Int = data.size

    inner class AboutCompanyHolder(var binding: AboutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindData(model: AboutModel) {
            Glide.with(binding.companyImg.context)
                .load(IMG_BASE_URL + getCompanyImgUrl(model.url) + IMG_SIZE)
                .into(binding.companyImg)
            binding.companyType.text =
                model.type.substring(0, 1).uppercase() + model.type.substring(1)
            binding.companyPostCount.text = model.totalResults
            binding.companyName.text = model.name
            binding.companyUrl.text = model.url
            binding.companyAbout.text = model.description

            setOnNewsClickListener!!.onFollowItem(binding.followBtn)

            binding.followBtn.setOnClickListener {
                setOnNewsClickListener!!.onFollowItemClick(binding.followBtn)
            }
        }
    }

    inner class AboutCompanyNewsHolder(var binding: TopHeadlineItemBinding) :
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
                setOnNewsClickListener!!.onItemClick(article, binding.newsImage, position)
            }
            binding.sharePost.setOnClickListener {
                article.url?.let { it1 -> setOnNewsClickListener!!.onItemClickForShare(it1) }
            }
        }
    }

    interface SetOnNewsClickListener {
        fun onItemClick(article: Article, imageView: ImageView, position: Int)
        fun onItemClickForShare(url: String)
        fun onFollowItem(view: TextView)
        fun onFollowItemClick(view: TextView)
        fun onNextPage()
    }

}