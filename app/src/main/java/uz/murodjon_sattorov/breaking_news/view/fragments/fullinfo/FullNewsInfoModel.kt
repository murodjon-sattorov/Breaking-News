package uz.murodjon_sattorov.breaking_news.view.fragments.fullinfo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.saved.SavedArticles
import uz.murodjon_sattorov.breaking_news.core.repository.NewsRepository
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 31/Mar/2022
 * @project Breaking News
 */

@HiltViewModel
class FullNewsInfoModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    fun saveToBase(savedArticles: SavedArticles) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.addPostNewsData(savedArticles)
        }
    }

    private val _isChecked = MutableLiveData<Boolean?>()
    val isChecked: LiveData<Boolean?> = _isChecked
    fun saved(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isChecked.postValue(newsRepository.checkData(url))
        }
    }

    fun deleteFromBase(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.deletePostNewsData(url)
        }
    }

}