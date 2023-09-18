

import HomeViewModel
import WebViewModel
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cheezycode.randomquote.repository.HomeRepository
import com.cheezycode.randomquote.repository.WebRepository

class WebViewModelFactory(private val repository: WebRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WebViewModel(repository) as T
    }
}