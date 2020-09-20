package `in`.pdftesting

import android.app.Service
import androidx.lifecycle.ViewModel

class DataVM : ViewModel() {

    fun getHello(): String {
        //val service = ServiceProvider.getServiceProvider("retrofit")
        val service = ServiceProvider.getServiceProvider("mock")
        val dataRepository = DataRepository(service)
        return dataRepository.getHelloString()
    }
}