package `in`.pdftesting

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private val pdfUrl =
        "https://www.citrix.com/content/dam/citrix/en_us/documents/buy/enterprise-eula-cn.pdf"

    private val PDF_RENDERING_GDOCS_PREFIX1 = "https://docs.google.com/gview?embedded=true&url="
    private val PDF_RENDERING_GDOCS_PREFIX2 = "https://docs.google.com/viewer?url="
    private val PDF_RENDERING_GDOCS_PREFIX3 = "https://docs.google.com/viewer?embedded=true&url="
    private val PDF_RENDERING_GDOCS_PREFIX4 =
        "http://drive.google.com/viewerng/viewer?embedded=true&url="
    private val PDF_RENDERING_GDOCS_PREFIX5 = "https://drive.google.com/viewerng/viewer?url="

    private val legalComplianceUrl = PDF_RENDERING_GDOCS_PREFIX4 + pdfUrl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_renderer)

        //Toast.makeText(this, DataVM().getHello(), Toast.LENGTH_LONG).show()

        showPDFInPDFView()
    }

    private fun showPDFInPDFView() {
        CoroutineScope(Dispatchers.IO).launch {
            val deferred = async {
                getPDFFile()
            }

            val pdfFile = deferred.await()
            withContext(Dispatchers.Main) {
                startActivity(Intent(this@MainActivity, PdfRenderActivity::class.java))
            }
        }
    }

    private fun getPDFFile(): File? {
        val fileName = "eula.pdf"
        val pdfFile = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        if (pdfFile.exists()) {
            return pdfFile
        }

        val url = URL(pdfUrl)
        val urlConnection = url.openConnection() as HttpsURLConnection
        if (urlConnection.responseCode == 200) {
            val inputStream = BufferedInputStream(urlConnection.inputStream)
            val fos = FileOutputStream(pdfFile)
            val buffer = ByteArray(1024)

            var len = 0
            while (inputStream.read(buffer).also { len = it } != -1) {
                fos.write(buffer, 0, len)
            }
            fos.close()
            inputStream.close()
        }

        return pdfFile
    }
}