package com.dragnell.myapplication.model

import java.io.Serializable

class FolderVideo (
    var name: String? = null,
    var listVideo: ArrayList<VideoModel>? = null
): Serializable {
    override fun toString(): String {
        return name.toString()
    }
}