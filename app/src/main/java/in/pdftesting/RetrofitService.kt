package `in`.pdftesting

import androidx.lifecycle.LiveData

class RetrofitService: IService {
    
    override fun getHelloString(): String {
        return "HelloFromRetrofit"
    }
}