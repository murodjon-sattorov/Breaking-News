package uz.murodjon_sattorov.breaking_news.core.repository

import android.util.Log
import androidx.lifecycle.LiveData
import io.reactivex.rxjava3.core.Single
import uz.murodjon_sattorov.breaking_news.core.database.dao.NewsDao
import uz.murodjon_sattorov.breaking_news.core.models.company.CompanyCategoryResponse
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.post.Article
import uz.murodjon_sattorov.breaking_news.core.models.post.TopHeadlinesResponse
import uz.murodjon_sattorov.breaking_news.core.models.saved.SavedArticles
import uz.murodjon_sattorov.breaking_news.core.network.NewsApi
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 22/Mar/2022
 * @project Breaking News
 */
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDao: NewsDao
) {

    fun getTopHeadlineNews(
        countryCode: String,
        pageNumber: Int,
        apiKey: String
    ): Single<TopHeadlinesResponse> {
        return newsApi.getTopHeadlinesNews(countryCode, pageNumber, apiKey)
    }

    fun getCompanies(category: String, apiKey: String): Single<CompanyCategoryResponse> {
        return newsApi.getSources(category, apiKey)
    }

    fun getFollowsNews(
        domains: String,
        pageNumber: Int,
        apiKey: String
    ): Single<TopHeadlinesResponse> {
        return newsApi.getFollowsNews(domains, pageNumber, 50, apiKey)
    }

    fun getSourcesNews(
        source: String,
        page: Int,
        apiKey: String
    ): Single<TopHeadlinesResponse> {
        return newsApi.getSourcesNews(source, page, apiKey)
    }

    fun getSearchNews(
        q: String,
        page: Int,
        apiKey: String
    ): Single<TopHeadlinesResponse> {
        return newsApi.getSearchNews(q, page, apiKey)
    }

    fun getAllTopHeadlineNews() = newsDao.readTopHeadlinesNewsData()

    suspend fun addTopHeadlinesNews(article: List<Article>) =
        newsDao.insertTopHeadlinesNewsData(article)

    suspend fun deleteTopHeadlinesData() = newsDao.deleteTopHeadlinesNewsData()


    //saved post news
    val readAllPostNewsData: LiveData<List<SavedArticles>> = newsDao.readPostNewsData()

    suspend fun addPostNewsData(savedArticles: SavedArticles) {
        newsDao.insertPostNewsData(savedArticles)
    }

    suspend fun checkData(url: String): Boolean {
        return newsDao.checkData(url).isNotEmpty()
    }

    suspend fun deletePostNewsData(url: String) {
        newsDao.deletePostNewsData(url)
    }

    //follows companies

    val readAllFollowData: LiveData<List<Source>> = newsDao.readAllFollowsData()
    val readAllFollowingData: LiveData<List<Source>> = newsDao.readAllFollowingData()

    suspend fun addFollowsData(source: List<Source>) = newsDao.insertAllCompaniesData(source)

    suspend fun updateFollowData(source: Source) = newsDao.updateFollowData(source)

    suspend fun getCount(): Int = newsDao.getCount()

    suspend fun getFollowsCount(): Int = newsDao.getFollowsCount()

    suspend fun getSelectedFollow(category: String): List<Source> =
        newsDao.getSelectedData(category)

    suspend fun getSearchData(name: String): List<Source> = newsDao.readSearchData(name)

}