package uz.murodjon_sattorov.breaking_news.core.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.murodjon_sattorov.breaking_news.core.database.dao.NewsDao
import uz.murodjon_sattorov.breaking_news.core.database.database.NewsDatabase
import javax.inject.Singleton

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 22/Mar/2022
 * @project Breaking News
 */

@Module
@InstallIn(SingletonComponent::class)
object NewsDatabaseModule {

    @Provides
    @Singleton
    fun provideNewsAppDatabase(
        application: Application,
        callback: NewsDatabase.Callback
    ): NewsDatabase {
        synchronized(this) {
            return Room.databaseBuilder(
                application,
                NewsDatabase::class.java,
                "news_database"
            )
                .fallbackToDestructiveMigration()
                .addCallback(callback)
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao = newsDatabase.getDao()

}