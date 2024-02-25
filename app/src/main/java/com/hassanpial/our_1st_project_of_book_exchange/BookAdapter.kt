// BookAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hassanpial.our_1st_project_of_book_exchange.Book
import com.hassanpial.our_1st_project_of_book_exchange.R

class BookAdapter(private val originalBookList: MutableList<Book>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {


    private var bookList: MutableList<Book> = originalBookList

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewBookCover: ImageView = itemView.findViewById(R.id.imageViewBookCover)
        val bookName: TextView = itemView.findViewById(R.id.textViewBookName)
        val authorName: TextView = itemView.findViewById(R.id.textViewAuthorName)
        val genre: TextView = itemView.findViewById(R.id.textViewGenre)
        val status: TextView = itemView.findViewById(R.id.status)
        // ... other components
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentBook = bookList[position]

        // Load the book cover image using Glide
        if (currentBook.picture != null) {
            Glide.with(holder.itemView.context)
                .load(currentBook.picture)
                .centerCrop()
                .placeholder(R.drawable.default_book_cover)
                .into(holder.imageViewBookCover)
        } else {
            holder.imageViewBookCover.setImageResource(R.drawable.default_book_cover)
        }

        holder.bookName.text = currentBook.name
        holder.authorName.text = currentBook.author
        holder.genre.text = currentBook.genre
        holder.status.text = currentBook.status


        // Set click listener for the entire item view
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(position, currentBook)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    fun filter(newText: String?) {
        bookList = if (newText.isNullOrBlank()) {
            ArrayList(originalBookList)
        } else {
            val filteredList = ArrayList<Book>()
            for (book in originalBookList) {
                if (book.name.contains(newText, ignoreCase = true) ||
                    book.author.contains(newText, ignoreCase = true) ||
                    book.genre.contains(newText, ignoreCase = true)
                ) {
                    filteredList.add(book)
                }
            }
            filteredList
        }
        notifyDataSetChanged()
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int, book: Book)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

}