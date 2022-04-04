package uz.murodjon_sattorov.breaking_news.core.helpers

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 22/Mar/2022
 * @project Breaking News
 */
class DateUtil {

    @SuppressLint("SimpleDateFormat")
    companion object {
        
        fun getPostTime(time: String): String {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

            var date: Date? = null
            try {
                date = input.parse(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return "${String.format("%02d", date!!.hours)}:${String.format("%02d", date.minutes)}"
        }

        fun timeCalculate(time: String): String {
            val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

            var date: Date? = null
            try {
                date = input.parse(time)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            val now = Date()

            val days = TimeUnit.MILLISECONDS.toDays(now.time - date!!.time)
            val hours = TimeUnit.MILLISECONDS.toHours(now.time - date.time)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - date.time)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - date.time)

            return when {
                days > 0 -> "~${days}d"
                hours > 0 -> "${hours}h"
                minutes > 0 -> "${minutes}m"
                else -> "${seconds}s"
            }
        }
    }

}