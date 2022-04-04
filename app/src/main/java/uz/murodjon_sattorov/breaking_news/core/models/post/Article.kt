package uz.murodjon_sattorov.breaking_news.core.models.post

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel.Companion.TYPE_TWO
import java.io.Serializable

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 21/Mar/2022
 * @project Breaking News
 */

//@Parcelize
@Entity(tableName = "top_headlines_news_table")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val author: String?, // Sharon LaFraniere
    val content: String?, // As of 11:45 a.m. Eastern.Source: FactSetBy The New York TimesOil prices dropped, falling below $100 a barrel, as China, the worlds largest oil importer, imposed new lockdowns to combat an outbr… [+3532 chars]
    val description: String?, // The White House says some Covid response plans are already being scaled back amid funding uncertainty. Countries in the Asia-Pacific region that were once similarly stringent are taking different approaches as cases surge. The British government is scrapping …
    val publishedAt: String?, // 2022-03-15T18:39:52Z
    val source: Source,
    val title: String?, // Covid Live Updates: Latest News, Case Counts and Mandates - The New York Times
    val url: String?, // https://www.nytimes.com/live/2022/03/15/world/covid-19-mandates-cases-vaccine
    val urlToImage: String? // https://static01.nyt.com/images/2022/03/15/multimedia/15virus-briefing-WHPROMO12:30PM/15virus-briefing-WHPROMO12:30PM-facebookJumbo.jpg
) : Serializable, BaseModel{
    override fun getType(): Int = TYPE_TWO

}