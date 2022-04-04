package uz.murodjon_sattorov.breaking_news.core.helpers

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.ibrahimsn.lib.SmoothBottomBar
import uz.murodjon_sattorov.breaking_news.view.activities.MainActivity

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 18/Mar/2022
 * @project Breaking News
 */

fun getCompanyImgUrl(url: String): String {
    var count = 0
    val retUrl = url.toCharArray()
    var realUrl = ""
    for (i in url.indices) {
        if (retUrl[i] == '/') {
            count++
            if (count == 3) break
        }
        realUrl += retUrl[i]
    }
    return realUrl.substring(8)
}

fun closeBottomBar(bottomBar: SmoothBottomBar, lineView: RelativeLayout) {
    bottomBar.visibility = View.GONE
    lineView.visibility = View.GONE
}

fun hideBottom(context: FragmentActivity) {
    if (context is MainActivity) {
        context.mainBinding.bottomBar.visibility = View.GONE
        context.mainBinding.lineView.visibility = View.GONE
    }
}

fun showBottom(context: FragmentActivity) {
    if (context is MainActivity) {
        context.lifecycleScope.launch {
            if (!context.mainBinding.bottomBar.isVisible) {
                context.mainBinding.bottomBar.visibility = View.VISIBLE
                context.mainBinding.lineView.visibility = View.VISIBLE
            }
        }
    }
}

fun openBottomBar(bottomBar: SmoothBottomBar, lineView: RelativeLayout) {
    bottomBar.visibility = View.VISIBLE
    lineView.visibility = View.VISIBLE
}

fun isVibration(context: Context) {
    val vibration = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibration.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibration.vibrate(100)
    }
}