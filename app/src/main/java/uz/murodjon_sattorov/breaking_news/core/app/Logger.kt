package uz.murodjon_sattorov.breaking_news.core.app

import timber.log.Timber
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 22/Mar/2022
 * @project Breaking News
 */
class Logger @Inject constructor() {

    fun configure() = Timber.plant(Timber.DebugTree())

    companion object {
        fun log(message: String) = Timber.d(message)
    }

}