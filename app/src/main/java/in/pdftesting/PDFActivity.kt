package `in`.pdftesting

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pdf_renderer.*
import java.io.File
import java.io.IOException

class PdfRenderActivity : AppCompatActivity() {
    private var pageIndex = 0
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_renderer)
        pageIndex = 0

        button_pre_doc.setOnClickListener {
            onPreviousDocClick()
        }
        button_next_doc.setOnClickListener {
            onNextDocClick()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onStart() {
        super.onStart()
        try {
            openRenderer(applicationContext)
            showPage(pageIndex)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public override fun onStop() {
        try {
            closeRenderer()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        super.onStop()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun onPreviousDocClick() {
        showPage(currentPage!!.index - 1)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun onNextDocClick() {
        showPage(currentPage!!.index + 1)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(IOException::class)
    private fun openRenderer(context: Context) {
        val fileName = "eula.pdf"
        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        if (parcelFileDescriptor != null) {
            pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(IOException::class)
    private fun closeRenderer() {
        if (null != currentPage) {
            currentPage!!.close()
        }
        pdfRenderer!!.close()
        parcelFileDescriptor!!.close()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun showPage(index: Int) {
        if (pdfRenderer!!.pageCount <= index) {
            return
        }
        if(null != currentPage) {
            currentPage!!.close()
        }
        currentPage = pdfRenderer!!.openPage(index)
        // Important: the destination bitmap must be ARGB (not RGB).
        val bitmap = Bitmap.createBitmap(
            currentPage!!.width, currentPage!!.height,
            Bitmap.Config.ARGB_8888
        )
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        currentPage!!.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        // We are ready to show the Bitmap to user.
        pdf_image.setImageBitmap(bitmap)
        updateUi()
    }

    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun updateUi() {
        val index = currentPage!!.index
        val pageCount = pdfRenderer!!.pageCount
        button_pre_doc.isEnabled = 0 != index
        button_next_doc.isEnabled = index + 1 < pageCount
    }

    @get:RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    val pageCount: Int
        get() = pdfRenderer!!.pageCount

    companion object {
        private const val FILENAME = "report.pdf"
    }
}