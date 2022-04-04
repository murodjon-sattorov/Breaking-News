package uz.murodjon_sattorov.breaking_news.core.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.core.models.saved.SavedArticles

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 21/Mar/2022
 * @project Breaking News
 */

@Dao
interface NewsDao {

    //top-headlines news
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopHeadlinesNewsData(article: List<Article>)

    @Query("DELETE FROM top_headlines_news_table")
    suspend fun deleteTopHeadlinesNewsData()

    @Query("select * from top_headlines_news_table")
    fun readTopHeadlinesNewsData(): LiveData<List<Article>>


    //save post news
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostNewsData(article: SavedArticles)

    @Query("DELETE FROM saved_post_news_table WHERE url LIKE :url")
    suspend fun deletePostNewsData(url: String)

    @Query("SELECT * FROM saved_post_news_table WHERE url LIKE :url")
    suspend fun checkData(url: String): List<SavedArticles>

    @Query("SELECT * FROM saved_post_news_table ORDER BY id ASC")
    fun readPostNewsData(): LiveData<List<SavedArticles>>


    //follows companies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCompaniesData(source: List<Source>)

    @Update
    suspend fun updateFollowData(source: Source)

    @Query("SELECT COUNT(*) FROM follows_table")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM follows_table WHERE isChecked=1")
    suspend fun getFollowsCount(): Int

    @Query("SELECT * FROM follows_table WHERE category LIKE :category")
    suspend fun getSelectedData(category: String): List<Source>

    @Query("SELECT * FROM follows_table WHERE name LIKE '%'||:name||'%'")
    fun readSearchData(name: String): List<Source>

    @Query("SELECT * FROM follows_table WHERE isChecked=1")
    fun readAllFollowingData(): LiveData<List<Source>>
//
//    @Query("SELECT * FROM follows_table WHERE isChecked=1")
//    fun readFollowingData(): List<Source>

    @Query("SELECT * FROM follows_table ORDER BY ids ASC")
    fun readAllFollowsData(): LiveData<List<Source>>

}