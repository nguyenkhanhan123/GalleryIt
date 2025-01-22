package com.dragnell.myapplication.view.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dragnell.myapplication.R
import com.dragnell.myapplication.model.Folder
import com.dragnell.myapplication.model.ImgModel
import com.dragnell.myapplication.model.VideoModel


class FolderAdapter(
    private val list: List<Any>,
    private val context: Context,
    private val onClickCurrentsFolder: (Int) -> Unit,
    private val onClickFolder: (Int) -> Unit,
    private val onClickAddFolder: () -> Unit
) : RecyclerView.Adapter<FolderAdapter.FolderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false)
        return FolderHolder(v)
    }

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {
        if (position < list.size) {
            val group: Any = list[position]
            if (group is Folder) {
                if (group.list!![0] is ImgModel) {
                    val img = group.list!![0] as ImgModel
                    Glide.with(context).load(img.file).into(holder.iv)
                } else if (group.list!![0] is VideoModel) {
                    val video = group.list!![0] as VideoModel
                    Glide.with(context).asBitmap().load(video.file).into(holder.iv)
                }
                holder.tvName.text = group.name
                holder.tvNumber.text = group.list?.size.toString()
                holder.lnGroup.tag = position
            }
            if (position == 0) {
                holder.lnGroup.setOnClickListener {
                    onClickCurrentsFolder(0)
                }
            } else {
                holder.lnGroup.setOnClickListener {
                    onClickFolder(position)
                }
            }
        } else {
            holder.iv.setImageResource(R.drawable.white_app)
            holder.tvName.text = "Create"
            holder.tvNumber.text = ""
            holder.lnGroup.setOnClickListener {
                onClickAddFolder()
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size + 1
    }

    class FolderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv: ImageView
        var tvName: TextView
        var tvNumber: TextView
        var lnGroup: LinearLayout

        init {
            iv = itemView.findViewById(R.id.ivIcon)
            tvName = itemView.findViewById(R.id.name)
            tvNumber = itemView.findViewById(R.id.number)
            lnGroup = itemView.findViewById(R.id.lnGroup)
            tvName.isSelected = true
            tvName.ellipsize = TextUtils.TruncateAt.MARQUEE
        }
    }
}
