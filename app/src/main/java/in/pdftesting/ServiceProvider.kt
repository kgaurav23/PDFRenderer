package `in`.pdftesting

object ServiceProvider {
    
    fun getServiceProvider(serviceProviderName: String): IService {
        return if(serviceProviderName.equals("retrofit", true)) {
            RetrofitService()
        } else {
            MockService()
        }
    }
}