package `in`.pdftesting

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_pdf_renderer.*
import java.io.File
import java.io.IOException

class PdfRenderActivity : AppCompatActivity() {
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var parcelFileDescriptor: ParcelFileDescriptor? = null

    companion object {
        const val FILE_NAME = "testPDF.pdf"
        private const val INTENT_PDF_FILE = "pdf_file"

        fun getCallingIntent(context: Context, pdfFile: File): Intent {
            val intent = Intent(context, PdfRenderActivity::class.java)
            intent.putExtra(INTENT_PDF_FILE, pdfFile)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_renderer)
        openRenderer()
        showPagesInRecyclerView()
    }

    private fun showPagesInRecyclerView() {
        currentPage = pdfRenderer!!.openPage(0)
        val pdfAdapter = PdfRendererAdapter(renderer = pdfRenderer, pageWidth = currentPage!!.width)
        currentPage!!.close()
        pdf_rv.adapter = pdfAdapter
        pdf_rv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    public override fun onStop() {
        try {
            closeRenderer()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        super.onStop()
    }

    private fun openRenderer() {
        val file = intent.extras?.get(INTENT_PDF_FILE) as File
        parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        if (parcelFileDescriptor != null) {
            pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
        }
    }

    private fun closeRenderer() {
        pdfRenderer!!.close()
        parcelFileDescriptor!!.close()
    }
}