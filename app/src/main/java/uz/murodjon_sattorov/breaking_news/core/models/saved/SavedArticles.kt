package uz.murodjon_sattorov.breaking_news.core.models.saved

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.murodjon_sattorov.breaking_news.core.models.post.Source

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 31/Mar/2022
 * @project Breaking News
 */

@Entity(tableName = "saved_post_news_table")
data class SavedArticles(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val author: String?, // Sharon LaFraniere
    val content: String?, // As of 11:45 a.m. Eastern.Source: FactSetBy The New York TimesOil prices dropped, falling below $100 a barrel, as China, the worlds largest oil importer, imposed new lockdowns to combat an outbr… [+3532 chars]
    val description: String?, // The White House says some Covid response plans are already being scaled back amid funding uncertainty. Countries in the Asia-Pacific region that were once similarly stringent are taking different approaches as cases surge. The British government is scrapping …
    val publishedAt: String?, // 2022-03-15T18:39:52Z
    val source: Source,
    val title: String?, // Covid Live Updates: Latest News, Case Counts and Mandates - The New York Times
    val url: String?, // https://www.nytimes.com/live/2022/03/15/world/covid-19-mandates-cases-vaccine
    val urlToImage: String? // https://static01.nyt.com/images/2021/12/20/nyregion/20maxwellBlogPromo01/20maxwellBlogPromo01-facebookJumbo.jpg
)