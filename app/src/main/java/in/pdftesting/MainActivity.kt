package `in`.pdftesting

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val pdfUrl = "http://www.pdf995.com/samples/pdf.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        downloadPDFAndRenderIt()
    }

    private fun downloadPDFAndRenderIt() {
        progress_bar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val deferred = async {
                PDFRendererUtility.downloadPDFFileFromWeb(
                    context = this@MainActivity,
                    pdfUrl = pdfUrl
                )
            }

            val pdfFile = deferred.await()
            withContext(Dispatchers.Main) {
                progress_bar.visibility = View.GONE
                val intent = PdfRenderActivity.getCallingIntent(this@MainActivity, pdfFile!!)
                startActivity(intent)
            }
        }
    }
}