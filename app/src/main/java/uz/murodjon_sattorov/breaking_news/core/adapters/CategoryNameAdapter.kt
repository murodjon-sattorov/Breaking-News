package uz.murodjon_sattorov.breaking_news.core.adapters

import android.content.res.Configuration
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.models.Model
import uz.murodjon_sattorov.breaking_news.databinding.CategoryNameItemBinding

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 23/Mar/2022
 * @project Breaking News
 */

class CategoryNameAdapter : RecyclerView.Adapter<CategoryNameAdapter.ViewHolder>() {

    private var selectedPosition = 0

    private var data = ArrayList<Model>()
    private var categoryNameItemClick: CategoryNameItemClick? = null
    fun addData(d: List<Model>) {
        data.addAll(d)
    }

    fun setOnCategoryNameItemClickListener(categoryNameItemClick: CategoryNameItemClick) {
        this.categoryNameItemClick = categoryNameItemClick
    }

    inner class ViewHolder(var binding: CategoryNameItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(model: Model) {
            binding.categoryTitle.text = model.title
            if (selectedPosition == model.id) {
                binding.categoryTitle.isSelected = true
                changeItemBack(binding.categoryTitle)
            } else {
                binding.categoryTitle.isSelected = false
                binding.categoryTitle.setBackgroundColor(Color.parseColor("#00000000"))
            }
            binding.categoryTitle.setOnClickListener {
                if (selectedPosition >= 0) {
                    notifyItemChanged(selectedPosition)
                    selectedPosition = model.id
                    notifyItemChanged(selectedPosition)
                    categoryNameItemClick!!.onItemNameClick(model)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryNameItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_name_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun clearData() {
        data.clear()
    }

    interface CategoryNameItemClick {
        fun onItemNameClick(model: Model)
    }

    private fun changeItemBack(view: TextView) {
        when (view.context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                view.setBackgroundColor(Color.parseColor("#404040"))
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                view.setBackgroundColor(Color.parseColor("#CDCDCD"))
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                view.setBackgroundColor(Color.parseColor("#CDCDCD"))
            }
        }
    }
}