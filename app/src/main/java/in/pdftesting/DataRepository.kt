package `in`.pdftesting

class DataRepository(private val service: IService) {
    
    fun getHelloString(): String = service.getHelloString()
    
}