package uz.murodjon_sattorov.breaking_news.core.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kotlinx.coroutines.CoroutineScope
import uz.murodjon_sattorov.breaking_news.core.database.convertor.Converters
import uz.murodjon_sattorov.breaking_news.core.database.dao.NewsDao
import uz.murodjon_sattorov.breaking_news.core.di.ApplicationScope
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.core.models.saved.SavedArticles
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 22/Mar/2022
 * @project Breaking News
 */

@Database(
    entities = [Article::class, Source::class, SavedArticles::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getDao(): NewsDao

    class Callback @Inject constructor(
        private val database: Provider<NewsDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()

}