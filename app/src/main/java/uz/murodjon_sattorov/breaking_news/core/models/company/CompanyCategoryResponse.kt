package uz.murodjon_sattorov.breaking_news.core.models.company

import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel.Companion.TYPE_ONE

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 24/Mar/2022
 * @project Breaking News
 */
data class CompanyCategoryResponse(
    var sources: List<Source>,
    var status: String = "" // ok
) : BaseModel {
    override fun getType(): Int {
        return TYPE_ONE
    }
}