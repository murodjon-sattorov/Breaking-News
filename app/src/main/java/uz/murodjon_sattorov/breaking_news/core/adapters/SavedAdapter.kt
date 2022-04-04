package uz.murodjon_sattorov.breaking_news.core.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.core.models.saved.SavedArticles
import uz.murodjon_sattorov.breaking_news.databinding.MiniNewsItemBinding

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 01/Apr/2022
 * @project Breaking News
 */
class SavedAdapter : RecyclerView.Adapter<SavedAdapter.ViewHolder>() {

    private var data = ArrayList<SavedArticles>()
    var setOnNewsClickListener: SetOnNewsClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun addData(d: List<SavedArticles>) {
        data.addAll(d)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private var binding: MiniNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(article: SavedArticles) {

            binding.postImage.transitionName = "news_image_$position"

            binding.companyName.text = article.source.name
            binding.postTitle.text = article.title

            if (article.urlToImage == "") {
                binding.postImage.scaleType = ImageView.ScaleType.CENTER
            } else {
                binding.postImage.scaleType = ImageView.ScaleType.CENTER_CROP
            }
            Glide.with(binding.postImage.context).load(article.urlToImage)
                .placeholder(R.drawable.ic_loading_image_big)
                .into(binding.postImage)

            binding.newsItemClick.setOnClickListener {

                val article1 = Article(
                    article.id,
                    article.author,
                    article.content,
                    article.description,
                    article.publishedAt,
                    article.source,
                    article.title,
                    article.url,
                    article.urlToImage
                )

                article1.source.id = "news_image_$position"

                setOnNewsClickListener?.onItemClick(
                    article1,
                    binding.postImage,
                    position
                )
                Log.d("TTT", "bindData: 1 $article1")
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MiniNewsItemBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.mini_news_item, parent, false)
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