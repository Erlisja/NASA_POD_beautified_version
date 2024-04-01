package com.example.nasapodrecyclerview
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.TileMode
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class PODAdapter (private val POD: List<PODItem>):RecyclerView.Adapter<PODAdapter.PODViewHolder>() {

    class PODViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val pod_image: ImageView
        val podDescription: TextView
        val podTitle: TextView

        init {
            pod_image = view.findViewById(R.id.pod_View)
            podDescription= view.findViewById(R.id.pod_description)
            podTitle = view.findViewById(R.id.pod_title)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PODViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_viewer, parent, false)

        return PODViewHolder(view)
    }

    override fun getItemCount(): Int {
        return POD.size
    }

    override fun onBindViewHolder(holder: PODViewHolder, position: Int) {
        val currentItem = POD[position]
        // val imageUrl = POD[position]
        Glide.with(holder.itemView)
            .load(currentItem.imageUrl)
            .fitCenter()
            .circleCrop()
            .into(holder.pod_image)

        holder.podDescription.text = currentItem.description
        holder.podTitle.text = currentItem.title


        // Set click listener for showing item name in a Toast
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            Toast.makeText(context, currentItem.title, Toast.LENGTH_SHORT).show()

        }
    }
}


