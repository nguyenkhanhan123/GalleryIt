package com.dragnell.myapplication.viewmodel

import android.os.Environment
import android.text.format.DateFormat
import com.dragnell.myapplication.model.FolderImg
import com.dragnell.myapplication.model.FolderVideo
import com.dragnell.myapplication.model.ImgModel
import com.dragnell.myapplication.model.VideoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class SplashViewModel : BaseViewModel() {

    private var folderImage: ArrayList<FolderImg> = ArrayList()

    private var folderVideo: ArrayList<FolderVideo> = ArrayList()

    open fun getImgAndVideoModel() {
        CoroutineScope(Dispatchers.IO).launch {
            val file = File("/storage/ace-999")
            folderImage.addAll(getAllImg(Environment.getExternalStorageDirectory()))
            folderVideo.addAll(getAllVideo(Environment.getExternalStorageDirectory()))
            if (file.exists()) {
                folderImage.addAll(getAllImg(file))
                folderVideo.addAll(getAllVideo(file))
            }
        }
    }

    open fun getFolderImg(): ArrayList<FolderImg> {
        return folderImage
    }

    open fun getFolderVideo(): ArrayList<FolderVideo> {
        return folderVideo
    }

    private fun getAllVideo(file: File): ArrayList<FolderVideo> {
        val folderArrayList = ArrayList<FolderVideo>()
        val files = file.listFiles()

        if (files != null) {
            val fileArrayList = ArrayList<VideoModel>()

            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden && !singleFile.name.contains("Telegram") && !singleFile.parent.contains(
                        "Telegram"
                    )
                ) {
                    folderArrayList.addAll(getAllVideo(singleFile))
                } else {
                    if (singleFile.name.lowercase(Locale.getDefault()).endsWith(".mp4")) {
                        fileArrayList.add(
                            VideoModel(
                                singleFile,
                                DateFormat.format("dd/MM/yyyy", singleFile.lastModified())
                                    .toString()
                            )
                        )
                    }
                }
            }

            if (fileArrayList.isNotEmpty()) {
                folderArrayList.add(FolderVideo(file.name, ArrayList(fileArrayList)))
            }
        }

        return folderArrayList
    }


    private fun getAllImg(file: File): ArrayList<FolderImg> {
        val folderArrayList = ArrayList<FolderImg>()
        val files = file.listFiles()

        if (files != null) {
            val fileArrayList = ArrayList<ImgModel>()

            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden && !singleFile.name.contains("Telegram") && !singleFile.parent.contains(
                        "Telegram"
                    )
                ) {
                    folderArrayList.addAll(getAllImg(singleFile))

                } else {
                    if (singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".jpg") || singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".jpeg") || singleFile.name.lowercase(Locale.getDefault())
                            .endsWith(".png")
                    ) {
                        fileArrayList.add(
                            ImgModel(
                                singleFile,
                                DateFormat.format("dd/MM/yyyy", singleFile.lastModified())
                                    .toString()
                            )
                        )
                    }
                }
            }
            if (fileArrayList.isNotEmpty()) {
                folderArrayList.add(FolderImg(file.name, ArrayList(fileArrayList)))
            }
        }

        return folderArrayList
    }


}