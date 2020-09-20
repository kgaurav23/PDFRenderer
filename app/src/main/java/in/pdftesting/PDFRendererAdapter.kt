package `in`.pdftesting

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.pdf_page_item.view.*

class PdfRendererAdapter(
    private val renderer: PdfRenderer?,
    private val pageWidth: Int
) : RecyclerView.Adapter<PdfRendererAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.imageView
        fun bind(bitmap: Bitmap) = imageView.setImageBitmap(bitmap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.pdf_page_item, parent, false)
        )

    override fun getItemCount() = renderer!!.pageCount

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPage = renderer?.openPage(position)
        val bitmap = currentPage!!.render(pageWidth)
        holder.bind(bitmap)
    }
}
