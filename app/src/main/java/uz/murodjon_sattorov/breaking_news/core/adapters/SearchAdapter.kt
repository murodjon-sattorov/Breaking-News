package uz.murodjon_sattorov.breaking_news.core.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.databinding.SearchItemLayoutBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 02/Apr/2022
 * @project Breaking News
 */
class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var data = ArrayList<Article>()
    var setOnNewsClickListener: SetOnNewsClickListener? = null

    fun addData(d: List<Article>) {
        data.addAll(d)
        notifyItemRangeInserted(data.size - d.size, data.size)
    }

    inner class ViewHolder(var binding: SearchItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(article: Article) {

            binding.postImage.transitionName = "news_image_$position"

            binding.postTitle.text = article.title
            binding.companyName.text = article.source.name
            binding.likeCount.text = Random().nextInt(10).toString()
            if (article.urlToImage == "") {
                binding.postImage.scaleType = ImageView.ScaleType.CENTER
            } else {
                binding.postImage.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            Glide.with(binding.postImage.context).load(article.urlToImage)
                .placeholder(R.drawable.ic_loading_image_big)
                .into(binding.postImage)


            binding.newsItemClick.setOnClickListener {
                article.source.id = "news_image_$position"
                setOnNewsClickListener?.onItemClick(
                    article,
                    binding.postImage,
                    position
                )
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchItemLayoutBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.search_item_layout, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
        if (data.size - 1 == position) {
            setOnNewsClickListener?.onNextPage()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData(){
        data.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    interface SetOnNewsClickListener {
        fun onItemClick(article: Article, imageView: ImageView, position: Int)
        fun onNextPage()
    }
}