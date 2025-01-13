package com.dragnell.myapplication.model

import java.io.Serializable

class FolderImg (
    var name: String? = null,
    var listImg: ArrayList<ImgModel>? = null
):Serializable{
    override fun toString(): String {
        return name.toString()
    }
}