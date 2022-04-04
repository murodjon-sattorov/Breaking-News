package uz.murodjon_sattorov.breaking_news.core.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import uz.murodjon_sattorov.breaking_news.core.models.company.CompanyCategoryResponse
import uz.murodjon_sattorov.breaking_news.core.models.post.TopHeadlinesResponse
import uz.murodjon_sattorov.breaking_news.core.utils.API_KEY

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 18/Mar/2022
 * @project Breaking News
 */

interface NewsApi {

    @GET("top-headlines")
    fun getTopHeadlinesNews(
        @Query("country") countryCode: String = "us",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Single<TopHeadlinesResponse>

    @GET("top-headlines/sources")
    fun getSources(
        @Query("category") category: String = "",
        @Query("apiKey") apiKey: String
    ): Single<CompanyCategoryResponse>

    @GET("everything")
    fun getFollowsNews(
        @Query("domains") domains: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int = 50,
        @Query("apiKey") apiKey: String
    ): Single<TopHeadlinesResponse>

    @GET("everything")
    fun getSourcesNews(
        @Query("sources") sources:String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ):Single<TopHeadlinesResponse>

    @GET("everything")
    fun getSearchNews(
        @Query("q") q:String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ):Single<TopHeadlinesResponse>

}