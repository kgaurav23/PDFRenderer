package `in`.pdftesting

import android.content.Context
import android.os.Environment
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object PDFRendererUtility {

    fun downloadPDFFileFromWeb(context: Context, pdfUrl: String): File? {

        val fileName = PdfRenderActivity.FILE_NAME
        val pdfFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        if (pdfFile.exists()) {
            return pdfFile
        }

        val url = URL(pdfUrl)
        val urlConnection = if (url.toString().contains("http://")) {
            url.openConnection() as HttpURLConnection
        } else {
            url.openConnection() as HttpsURLConnection
        }

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