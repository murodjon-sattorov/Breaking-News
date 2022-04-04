package uz.murodjon_sattorov.breaking_news.core.adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel
import uz.murodjon_sattorov.breaking_news.core.models.company.CompanyCategoryResponse
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.follow.FollowModel
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.core.models.post.TopHeadlinesResponse
import uz.murodjon_sattorov.breaking_news.databinding.FollowItemBinding
import uz.murodjon_sattorov.breaking_news.databinding.TopHeadlineItemBinding


/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 26/Mar/2022
 * @project Breaking News
 */

class FollowAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    FollowNewsVerticalList.SetOnNewsClickListener {

    private var data = ArrayList<BaseModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun addData(d: BaseModel) {
        data.add(d)
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
                HorizontalViewHolder(
                    FollowItemBinding.bind(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.follow_item, parent, false)
                    )
                )
            }
            BaseModel.TYPE_TWO -> {
                VerticalViewHolder(
                    FollowItemBinding.bind(
                        LayoutInflater.from(parent.context)
                            .inflate(R.layout.follow_item, parent, false)
                    )
                )
            }
            else -> {
                throw IllegalArgumentException("Not arguments")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = data[position]
        if (holder is HorizontalViewHolder) {
            horizontalView(holder, model as CompanyCategoryResponse)
        }
        if (holder is VerticalViewHolder) {
            verticalView(holder, model as TopHeadlinesResponse)
        }
    }

    override fun getItemCount(): Int = data.size

    private fun horizontalView(
        holder: HorizontalViewHolder,
        companyCategoryResponse: CompanyCategoryResponse
    ) {
        val adapter = FollowHorizontalList()
        adapter.addData(companyCategoryResponse.sources)
        val recyclerView = holder.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(
            holder.recyclerView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        recyclerView.adapter = adapter
    }

    private fun verticalView(
        holder: VerticalViewHolder,
        topHeadlinesResponse: TopHeadlinesResponse
    ) {
        val adapter = FollowNewsVerticalList()
        adapter.addData(topHeadlinesResponse.articles)
        val recyclerView = holder.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = adapter

        adapter.setOnNewsClickListener = this

    }

    inner class HorizontalViewHolder(private var binding: FollowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var recyclerView = binding.setLists
    }

    inner class VerticalViewHolder(private var binding: FollowItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var recyclerView = binding.setLists
    }

    override fun onItemClick(article: Article, imageView: ImageView, position: Int) {

        val navController = Navigation.findNavController(imageView)

        val bundle = Bundle()
        bundle.putSerializable("news_item_data", article)

        val extras = FragmentNavigatorExtras(
            imageView to "news_image_$position"
        )
        Log.d("TTT", "onItemClick: -news_image_$position")
        navController.navigate(
            R.id.action_followFragment_to_fullNewsInfoFragment,
            bundle,
            null,
            extras
        )
    }

}