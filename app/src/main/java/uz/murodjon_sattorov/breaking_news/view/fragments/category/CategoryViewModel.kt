package uz.murodjon_sattorov.breaking_news.view.fragments.category

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
import uz.murodjon_sattorov.breaking_news.core.database.database.NewsDatabase
import uz.murodjon_sattorov.breaking_news.core.models.company.CompanyCategoryResponse
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.repository.NewsRepository
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 24/Mar/2022
 * @project Breaking News
 */

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val readAllFollowsData: LiveData<List<Source>> = newsRepository.readAllFollowData

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> = _isLoading

    private val disposables = CompositeDisposable()

    private val _loadCompanies = MutableLiveData<CompanyCategoryResponse?>()
    val loadCompanies: LiveData<CompanyCategoryResponse?> = _loadCompanies
    fun getLoadCompanies(category: String) {
        _isLoading.postValue(true)
        val disposable = newsRepository.getCompanies(category, context.getString(R.string.api_key))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<CompanyCategoryResponse>() {
                override fun onSuccess(t: CompanyCategoryResponse) {
                    t.let {
                        _isLoading.postValue(false)
                        _loadCompanies.postValue(it)
                    }
                }

                override fun onError(e: Throwable) {
                    _isLoading.postValue(false)
                    _errorMessage.postValue(e.toString())
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

    private val _getSelectedDate = MutableLiveData<List<Source>?>()
    val getSelectedDate: LiveData<List<Source>?> = _getSelectedDate
    fun getSelectedFollow(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getSelectedDate.postValue(newsRepository.getSelectedFollow(category))
        }
    }

    private val _getSearchData = MutableLiveData<List<Source>?>()
    val getSearchData: LiveData<List<Source>?> = _getSearchData
    fun getSearchData(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _getSearchData.postValue(newsRepository.getSearchData(name))
        }
    }

    override fun onCleared() {
        disposables.dispose()
        disposables.clear()
        super.onCleared()
    }

}