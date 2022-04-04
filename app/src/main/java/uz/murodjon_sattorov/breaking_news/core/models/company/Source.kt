package uz.murodjon_sattorov.breaking_news.core.models.company

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel
import uz.murodjon_sattorov.breaking_news.core.models.base.BaseModel.Companion.TYPE_ONE
import java.io.Serializable

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 24/Mar/2022
 * @project Breaking News
 */
@Entity(tableName = "follows_table")
data class Source(
    @PrimaryKey(autoGenerate = true)
    var ids: Int,
    val category: String, // entertainment
    val country: String, // us
    val description: String, // BuzzFeed is a cross-platform, global network for news and entertainment that generates seven billion views each month.
    val id: String, // buzzfeed
    val language: String, // en
    val name: String, // Buzzfeed
    val url: String, // https://www.buzzfeed.com
    var isChecked: Boolean = false
) : Serializable