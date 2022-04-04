package uz.murodjon_sattorov.breaking_news.core.models.post

import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel.Companion.TYPE_TWO

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 21/Mar/2022
 * @project Breaking News
 */

data class TopHeadlinesResponse(
    val articles: List<Article>,
    val status: String, // ok
    val totalResults: Int // 38
) : BaseModel {
    override fun getType(): Int {
        return TYPE_TWO
    }
}