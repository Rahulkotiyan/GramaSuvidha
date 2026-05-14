package com.example.gramasuvidha.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramasuvidha.models.Notice
import com.example.gramasuvidha.network.NetworkRepository
import com.example.gramasuvidha.network.NetworkResult
import com.example.gramasuvidha.network.RetrofitClient
import com.example.gramasuvidha.data.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NoticeViewModel(application: Application) : AndroidViewModel(application) {

    private val networkRepository = NetworkRepository(
        apiService = RetrofitClient.apiService,
        projectDao = AppDatabase.getDatabase(application).projectDao(),
        context = application
    )

    private val _notices = MutableStateFlow<List<Notice>>(emptyList())
    val notices: StateFlow<List<Notice>> = _notices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchNotices()
    }

    fun fetchNotices() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = networkRepository.getAllNotices()
            if (result is NetworkResult.Success) {
                _notices.value = result.data
            }
            _isLoading.value = false
        }
    }
}
