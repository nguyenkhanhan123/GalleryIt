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
import com.dragnell.myapplication.model.FolderImg
import com.dragnell.myapplication.model.FolderVideo
import com.dragnell.myapplication.model.ImgModel
import com.dragnell.myapplication.model.VideoModel


class FolderAdapter(list: List<Any>, context: Context, event: View.OnClickListener) :
    RecyclerView.Adapter<FolderAdapter.GroupHolder>() {
    private val list: List<Any>
    private val context: Context
    private val event: View.OnClickListener

    init {
        this.list = list
        this.context = context
        this.event = event
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val v: View = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false)
        return GroupHolder(v)
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        var group: Any = list[position]
        if (group is Folder) {
            if (group.list!![0] is ImgModel){
                val img = group.list!![0] as ImgModel
                Glide.with(context)
                    .load(img.file)
                    .into(holder.iv)

            } else if (group.list!![0] is VideoModel ){
                val video = group.list!![0] as VideoModel
                Glide.with(context)
                    .asBitmap()
                    .load(video.file)
                    .into(holder.iv)

            }
           holder.tvName.text=group.name
            holder.tvNumber.text= group.list?.size.toString()
            holder.lnGroup.tag=position
        }
        holder.lnGroup.setOnClickListener(event)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class GroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            tvName.ellipsize = TextUtils.TruncateAt.MARQUEE;
        }
    }
}
