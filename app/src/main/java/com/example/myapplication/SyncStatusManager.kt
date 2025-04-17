package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object SyncStatusManager {
    private val _isSyncActive = MutableLiveData(true)
    val isSyncActive: LiveData<Boolean> get() = _isSyncActive

    fun updateSyncStatus(active: Boolean) {
        _isSyncActive.postValue(active)
    }
}
