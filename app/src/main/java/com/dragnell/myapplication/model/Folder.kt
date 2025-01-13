package com.dragnell.myapplication.model

import java.io.Serializable

class Folder(
    var name: String? = null,
    var list: ArrayList<Any>? = null
): Serializable {
}