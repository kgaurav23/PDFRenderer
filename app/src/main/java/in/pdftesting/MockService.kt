package `in`.pdftesting

class MockService: IService {
    
    override fun getHelloString(): String {
        return "HelloFromMock"
    }
}