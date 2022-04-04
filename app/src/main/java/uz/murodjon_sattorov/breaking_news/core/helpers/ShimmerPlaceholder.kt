package uz.murodjon_sattorov.breaking_news.core.helpers

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import uz.murodjon_sattorov.breaking_news.R

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 22/Mar/2022
 * @project Breaking News
 */

@RequiresApi(Build.VERSION_CODES.M)
fun dayAndNight(context: Context): ShimmerDrawable {

    val shimmerDrawable = ShimmerDrawable()

    when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_YES -> {
            val shimmer = Shimmer.ColorHighlightBuilder()
                .setBaseColor(context.getColor(R.color.nightBaseColor))
                .setBaseAlpha(1f)
                .setHighlightColor(context.getColor(R.color.nightHighlightColor))
                .setHighlightAlpha(1f)
                .setDropoff(10f)
                .build()

            shimmerDrawable.setShimmer(shimmer)
        }
        Configuration.UI_MODE_NIGHT_NO -> {
            val shimmer = Shimmer.ColorHighlightBuilder()
                .setBaseColor(context.getColor(R.color.lightBaseColor))
                .setBaseAlpha(1f)
                .setHighlightColor(context.getColor(R.color.lightHighlightColor))
                .setHighlightAlpha(1f)
                .setDropoff(10f)
                .build()

            shimmerDrawable.setShimmer(shimmer)

        }
        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            val shimmer = Shimmer.ColorHighlightBuilder()
                .setBaseColor(context.getColor(R.color.lightBaseColor))
                .setBaseAlpha(1f)
                .setHighlightColor(context.getColor(R.color.lightHighlightColor))
                .setHighlightAlpha(1f)
                .setDropoff(10f)
                .build()

            shimmerDrawable.setShimmer(shimmer)

        }
    }
    return shimmerDrawable
}