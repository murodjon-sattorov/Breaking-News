package uz.murodjon_sattorov.breaking_news.view.fragments.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.models.post.TopHeadlinesResponse
import uz.murodjon_sattorov.breaking_news.core.repository.NewsRepository
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 02/Apr/2022
 * @project Breaking News
 */

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val disposables = CompositeDisposable()

    var page = 1

    private val _loadSearchNews = MutableLiveData<TopHeadlinesResponse?>()
    val loadSearchNews: LiveData<TopHeadlinesResponse?> = _loadSearchNews
    fun loadSearchData(q: String) {
        _isLoading.postValue(true)
        Log.d("TTT", "loadSearchData: $page")
        val disposable = newsRepository.getSearchNews(q, page, context.getString(R.string.api_key))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<TopHeadlinesResponse>() {
                override fun onSuccess(t: TopHeadlinesResponse) {
                    t.let {
                        _isLoading.postValue(false)
                        _loadSearchNews.postValue(it)
                        page++
                    }
                }

                override fun onError(e: Throwable) {
                    _isLoading.postValue(false)
                    _errorMessage.postValue(e.toString())
                }

            })
        disposables.addAll(disposable)
    }

    private fun loadFirstPage() {
        page = 1
    }

    override fun onCleared() {
        disposables.dispose()
        disposables.clear()
        loadFirstPage()
        super.onCleared()
    }

}