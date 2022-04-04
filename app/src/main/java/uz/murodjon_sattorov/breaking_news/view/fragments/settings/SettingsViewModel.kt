package uz.murodjon_sattorov.breaking_news.view.fragments.settings

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by <a href="mailto: sattorovmurodjon43@gmail.com">Murodjon Sattorov</a>
 *
 * @author murodjon
 * @date 03/Apr/2022
 * @project Breaking News
 */

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _getSavedNightModeData = MutableLiveData<String?>()
    val getSavedNightModeData: LiveData<String?> = _getSavedNightModeData
    fun getSavedNightModeData() {
        viewModelScope.launch(Dispatchers.IO) {
            _getSavedNightModeData.postValue(
                sharedPreferences.getString("NightMode", "").toString()
            )
        }
    }

    private val _getSavedNotificationData = MutableLiveData<String?>()
    val getSavedNotificationData: LiveData<String?> = _getSavedNotificationData
    fun getSavedNotificationData() {
        viewModelScope.launch(Dispatchers.IO) {
            _getSavedNotificationData.postValue(
                sharedPreferences.getString("Notification", "").toString()
            )
        }
    }

    fun setSavedNotificationData(type: String) {
        sharedPreferences.edit().putString("Notification", type).apply()
    }

    fun setSavedNightModeData(nightType: String) {
        sharedPreferences.edit().putString("NightMode", nightType).apply()
    }

}