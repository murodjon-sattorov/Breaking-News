package uz.murodjon_sattorov.breaking_news.core.models.follow

import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.post.Article

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 27/Mar/2022
 * @project Breaking News
 */
data class FollowModel(
    val articles: List<Article>,
    val sources: List<Source>,
)