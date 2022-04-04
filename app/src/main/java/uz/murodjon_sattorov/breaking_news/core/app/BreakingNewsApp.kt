package uz.murodjon_sattorov.breaking_news.core.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import uz.murodjon_sattorov.breaking_news.view.fragments.settings.SettingsViewModel
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 22/Mar/2022
 * @project Breaking News
 */

@HiltAndroidApp
class BreakingNewsApp : Application() {

    @Inject
    lateinit var logger: Logger

    override fun onCreate() {
        super.onCreate()
        logger.configure()
    }

}