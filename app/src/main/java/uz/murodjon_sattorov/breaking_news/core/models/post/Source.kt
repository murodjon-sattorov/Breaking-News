package uz.murodjon_sattorov.breaking_news.core.models.post

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 21/Mar/2022
 * @project Breaking News
 */
//@Parcelize
data class Source(
    var id: String?, // null
    val name: String // New York Times
): Serializable