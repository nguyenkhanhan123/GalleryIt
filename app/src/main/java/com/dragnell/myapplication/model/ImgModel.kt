package com.dragnell.myapplication.model

import java.io.File
import java.io.Serializable

data class ImgModel(
    var file: File? = null,
    var date: String? = null
) : Serializable
