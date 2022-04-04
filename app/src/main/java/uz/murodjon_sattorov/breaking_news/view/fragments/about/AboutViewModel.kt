package uz.murodjon_sattorov.breaking_news.view.fragments.about

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
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.post.TopHeadlinesResponse
import uz.murodjon_sattorov.breaking_news.core.repository.NewsRepository
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 30/Mar/2022
 * @project Breaking News
 */

@HiltViewModel
class AboutViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val readAllFollowingData: LiveData<List<Source>> = newsRepository.readAllFollowingData

    private val _errorMessages = MutableLiveData<String?>()
    val errorMessages: LiveData<String?> = _errorMessages

    private val disposables = CompositeDisposable()

    private var page = 1

    private val _loadSourcesData = MutableLiveData<TopHeadlinesResponse?>()
    val loadSourcesData: LiveData<TopHeadlinesResponse?> = _loadSourcesData

    fun loadSourceData(sources: String) {

        val disposable =
            newsRepository.getSourcesNews(sources, page, context.getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<TopHeadlinesResponse>() {
                    override fun onSuccess(t: TopHeadlinesResponse?) {
                        t?.let {
                            _loadSourcesData.postValue(it)
                            page++
                        }
                    }

                    override fun onError(e: Throwable?) {
                        _errorMessages.postValue(e.toString())
                    }

                })
        disposables.addAll(disposable)
    }

    fun updateFollowData(source: Source) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.updateFollowData(source)
        }
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