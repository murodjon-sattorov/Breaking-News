package uz.murodjon_sattorov.breaking_news.view.fragments.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.murodjon_sattorov.breaking_news.core.models.company.Source
import uz.murodjon_sattorov.breaking_news.core.models.saved.SavedArticles
import uz.murodjon_sattorov.breaking_news.core.repository.NewsRepository
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 01/Apr/2022
 * @project Breaking News
 */

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val readAllSavedPostNewsData: LiveData<List<SavedArticles>> = newsRepository.readAllPostNewsData

}