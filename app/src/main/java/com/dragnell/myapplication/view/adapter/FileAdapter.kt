package com.dragnell.myapplication.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dragnell.myapplication.R
import com.dragnell.myapplication.model.ImgModel
import com.dragnell.myapplication.model.VideoModel


class FileAdapter(
    private val list: List<Any>,
    private val context: Context
) : RecyclerView.Adapter<FileAdapter.FileHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)
        return FileHolder(v)
    }

    override fun onBindViewHolder(holder: FileHolder, position: Int) {
        val file: Any = list[position]
        if (file is ImgModel) {
            Glide.with(context).load(file.file).into(holder.iv)
            holder.tvTime.text=""
        } else if (file is VideoModel) {
            Glide.with(context).asBitmap().load(file.file).into(holder.iv)
            holder.tvTime.text=file.date
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class FileHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv: ImageView
        var tvTime: TextView
        var clFile: ConstraintLayout

        init {
            iv = itemView.findViewById(R.id.ivIcon)
            tvTime = itemView.findViewById(R.id.tvTime)
            clFile = itemView.findViewById(R.id.clFile)
        }
    }
}
