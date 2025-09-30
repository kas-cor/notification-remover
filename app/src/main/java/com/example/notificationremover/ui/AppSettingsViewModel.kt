package com.example.notificationremover.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notificationremover.data.AppSettingEntity
import com.example.notificationremover.data.AppSettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppSettingsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = AppSettingsRepository(application)
    val allAppSettings: LiveData<List<AppSettingEntity>> = repository.getAllAppSettings()
    val enabledAppSettings: LiveData<List<AppSettingEntity>> = repository.getAllEnabledAppSettings()
    
    private val _operationResult = MutableLiveData<OperationResult>()
    val operationResult: LiveData<OperationResult> = _operationResult
    
    private val _settingsCount = MutableLiveData<Int>()
    val settingsCount: LiveData<Int> = _settingsCount
    
    companion object {
        private const val TAG = "AppSettingsViewModel"
    }
    
    init {
        updateSettingsCount()
    }
    
    fun getDefaultRemovalTime(): Int {
        return repository.getDefaultRemovalTime()
    }
    
    fun saveDefaultRemovalTime(minutes: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val success = repository.saveDefaultRemovalTime(minutes)
                val result = if (success) {
                    OperationResult.Success("Default time saved successfully")
                } else {
                    OperationResult.Error("Failed to save default time")
                }
                _operationResult.postValue(result)
            } catch (e: Exception) {
                Log.e(TAG, "Error saving default removal time", e)
                _operationResult.postValue(OperationResult.Error("Error: ${e.message}"))
            }
        }
    }
    
    fun addAppSetting(packageName: String, appName: String, removalTimeMinutes: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!repository.isValidRemovalTime(removalTimeMinutes)) {
                    _operationResult.postValue(OperationResult.Error("Invalid removal time"))
                    return@launch
                }
                
                val appSetting = AppSettingEntity(
                    packageName = packageName,
                    appName = appName,
                    removalTimeMinutes = removalTimeMinutes,
                    isEnabled = true
                )
                
                val success = repository.saveAppSetting(appSetting)
                val result = if (success) {
                    updateSettingsCount()
                    OperationResult.Success("App setting added successfully")
                } else {
                    OperationResult.Error("Failed to add app setting")
                }
                _operationResult.postValue(result)
            } catch (e: Exception) {
                Log.e(TAG, "Error adding app setting", e)
                _operationResult.postValue(OperationResult.Error("Error: ${e.message}"))
            }
        }
    }
    
    fun updateAppSetting(appSetting: AppSettingEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val success = repository.saveAppSetting(appSetting)
                val result = if (success) {
                    OperationResult.Success("App setting updated successfully")
                } else {
                    OperationResult.Error("Failed to update app setting")
                }
                _operationResult.postValue(result)
            } catch (e: Exception) {
                Log.e(TAG, "Error updating app setting", e)
                _operationResult.postValue(OperationResult.Error("Error: ${e.message}"))
            }
        }
    }
    
    fun toggleAppSetting(packageName: String, enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val success = repository.toggleAppSetting(packageName, enabled)
                val result = if (success) {
                    OperationResult.Success("App setting toggled successfully")
                } else {
                    OperationResult.Error("Failed to toggle app setting")
                }
                _operationResult.postValue(result)
            } catch (e: Exception) {
                Log.e(TAG, "Error toggling app setting", e)
                _operationResult.postValue(OperationResult.Error("Error: ${e.message}"))
            }
        }
    }
    
    fun deleteAppSetting(appSetting: AppSettingEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val success = repository.deleteAppSetting(appSetting)
                val result = if (success) {
                    updateSettingsCount()
                    OperationResult.Success("App setting deleted successfully")
                } else {
                    OperationResult.Error("Failed to delete app setting")
                }
                _operationResult.postValue(result)
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting app setting", e)
                _operationResult.postValue(OperationResult.Error("Error: ${e.message}"))
            }
        }
    }
    
    private fun updateSettingsCount() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val count = repository.getSettingsCount()
                _settingsCount.postValue(count)
            } catch (e: Exception) {
                Log.e(TAG, "Error getting settings count", e)
            }
        }
    }
    
    fun isValidRemovalTime(minutes: Int): Boolean {
        return repository.isValidRemovalTime(minutes)
    }
    
    sealed class OperationResult {
        data class Success(val message: String) : OperationResult()
        data class Error(val message: String) : OperationResult()
    }
}
