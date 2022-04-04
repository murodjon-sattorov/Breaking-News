package uz.murodjon_sattorov.breaking_news.view.fragments.follow

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.murodjon_sattorov.breaking_news.R
import uz.murodjon_sattorov.breaking_news.core.models.company.CompanyCategoryResponse
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.post.TopHeadlinesResponse
import uz.murodjon_sattorov.breaking_news.core.repository.NewsRepository
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 25/Mar/2022
 * @project Breaking News
 */

@HiltViewModel
class FollowViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    //get recommended posts from database
    val readAllFollowsData: LiveData<List<Source>> = newsRepository.readAllFollowData
    val readAllFollowingData: LiveData<List<Source>> = newsRepository.readAllFollowingData

    private val _errorMessages = MutableLiveData<String?>()
    val errorMessages: LiveData<String?> = _errorMessages

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    private val disposables = CompositeDisposable()

    private var page = 1

    private val _loadCompanies = MutableLiveData<CompanyCategoryResponse?>()
    val loadCompanies: LiveData<CompanyCategoryResponse?> = _loadCompanies
    fun getLoadCompanies(category: String) {
        val disposable = newsRepository.getCompanies(category, context.getString(R.string.api_key))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<CompanyCategoryResponse>() {
                override fun onSuccess(t: CompanyCategoryResponse) {
                    t.let {
                        _loadCompanies.postValue(it)
                    }
                }

                override fun onError(e: Throwable) {
                    _errorMessages.postValue(e.toString())
                }

            })
        disposables.addAll(disposable)
    }

    private val _loadFollowsNews = MutableLiveData<TopHeadlinesResponse?>()
    val loadFollowsNews: LiveData<TopHeadlinesResponse?> = _loadFollowsNews
    fun loadFollowsNews(context: Context, domains: String) {
        _isLoading.postValue(true)
        val disposable =
            newsRepository.getFollowsNews(domains, page, context.getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TopHeadlinesResponse>() {
                    override fun onSuccess(t: TopHeadlinesResponse) {
                        _isLoading.postValue(false)
                        t.let {
                            _loadFollowsNews.postValue(it)
                        }
                    }

                    override fun onError(e: Throwable) {
                        _isLoading.postValue(true)
                        _errorMessages.postValue(e.toString())
                    }

                })
        disposables.addAll(disposable)
    }

    fun addFollowData(source: List<Source>) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.addFollowsData(source)
        }
    }

    fun updateFollowData(source: Source) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.updateFollowData(source)
        }
    }

    private val _getCount = MutableLiveData<Int?>()
    val getCount: LiveData<Int?> = _getCount
    fun getCount() {
        viewModelScope.launch {
            _getCount.postValue(newsRepository.getCount())
        }
    }

    private val _getFollowsCount = MutableLiveData<Int?>()
    val getFollowsCount: LiveData<Int?> = _getFollowsCount
    fun getFollowsCount() {
        viewModelScope.launch(Dispatchers.IO) {
            _getFollowsCount.postValue(newsRepository.getFollowsCount())
        }
    }

    override fun onCleared() {
        disposables.dispose()
        disposables.clear()
        page = 1
        super.onCleared()
    }


}