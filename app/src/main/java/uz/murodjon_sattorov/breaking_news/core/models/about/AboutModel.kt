package uz.murodjon_sattorov.breaking_news.core.models.about

import androidx.annotation.Keep
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel.Companion.TYPE_ONE

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 30/Mar/2022
 * @project Breaking News
 */

class AboutModel(
    var type: String,
    var totalResults: String,
    var name: String,
    var url: String,
    var description: String
) : BaseModel {
    override fun getType(): Int {
        return TYPE_ONE
    }
}