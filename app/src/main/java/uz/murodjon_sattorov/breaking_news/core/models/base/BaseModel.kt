package uz.murodjon_sattorov.breaking_news.core.models.base

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 26/Mar/2022
 * @project Breaking News
 */

interface BaseModel {

    companion object {
        var TYPE_ONE: Int
            set(value) {

            }
            get() = 0

        var TYPE_TWO: Int
            set(value) {

            }
            get() = 1
    }

    fun getType(): Int

}