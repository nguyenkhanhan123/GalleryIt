package com.dragnell.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.text.format.DateFormat
import android.util.Log
import com.dragnell.myapplication.model.Folder
import com.dragnell.myapplication.model.FolderImg
import com.dragnell.myapplication.model.FolderVideo
import com.dragnell.myapplication.model.ImgModel
import com.dragnell.myapplication.model.VideoModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

class ManageFile {
    private var folderImage: ArrayList<FolderImg> = ArrayList()

    private var folderVideo: ArrayList<FolderVideo> = ArrayList()

    private var folderList: ArrayList<Folder> = ArrayList()

    private var folder : Folder? = null

    companion object {
        val instance: ManageFile by lazy { ManageFile() }
    }

    suspend fun getImgAndVideoModel() {
        withContext(Dispatchers.IO) {
            val defaultDir = Environment.getExternalStorageDirectory()
            val customDir = File("/storage/ace-999")

            folderImage.clear()
            folderVideo.clear()

            val allImages = ArrayList<ImgModel>()
            val allVideos = ArrayList<VideoModel>()

            val imgFromDefaultDir = async { getAllImg(defaultDir, allImages) }
            val videoFromDefaultDir = async { getAllVideo(defaultDir, allVideos) }

            val imgFromCustomDir =
                async { if (customDir.exists()) getAllImg(customDir, allImages) else emptyList() }
            val videoFromCustomDir =
                async { if (customDir.exists()) getAllVideo(customDir, allVideos) else emptyList() }

            folderImage.addAll(imgFromDefaultDir.await())
            folderVideo.addAll(videoFromDefaultDir.await())
            folderImage.addAll(imgFromCustomDir.await())
            folderVideo.addAll(videoFromCustomDir.await())

            if (allImages.isNotEmpty()) {
                folderImage.add(0, FolderImg("Currents", allImages))
            }
            if (allVideos.isNotEmpty()) {
                folderVideo.add(0, FolderVideo("Currents", allVideos))
            }
        }
    }


    fun getFolderImg(): ArrayList<FolderImg> {
        return folderImage
    }

    fun getFolderVideo(): ArrayList<FolderVideo> {
        return folderVideo
    }

    private suspend fun getAllImg(
        file: File, allImages: ArrayList<ImgModel>
    ): ArrayList<FolderImg> = withContext(Dispatchers.IO) {
        val folderArrayList = ArrayList<FolderImg>()
        val files = file.listFiles()

        if (files != null) {
            val fileArrayList = ArrayList<ImgModel>()

            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden && !singleFile.name.contains(
                        "Telegram", true
                    ) && !singleFile.parent.contains("Telegram", true)
                ) {
                    folderArrayList.addAll(getAllImg(singleFile, allImages))
                } else if (singleFile.name.lowercase(Locale.getDefault())
                        .run { endsWith(".jpg") || endsWith(".jpeg") || endsWith(".png") }
                ) {
                    val imgModel = ImgModel(
                        singleFile,
                        DateFormat.format("dd/MM/yyyy", singleFile.lastModified()).toString()
                    )
                    fileArrayList.add(imgModel)
                    allImages.add(imgModel)
                }
            }

            if (fileArrayList.isNotEmpty()) {
                folderArrayList.add(FolderImg(file.name, ArrayList(fileArrayList)))
            }
        }

        return@withContext folderArrayList
    }


    private suspend fun getAllVideo(
        file: File, allVideos: ArrayList<VideoModel>
    ): ArrayList<FolderVideo> = withContext(Dispatchers.IO) {
        val folderArrayList = ArrayList<FolderVideo>()
        val files = file.listFiles()

        if (files != null) {
            val fileArrayList = ArrayList<VideoModel>()

            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden && !singleFile.name.contains(
                        "Telegram", true
                    ) && !singleFile.parent.contains("Telegram", true)
                ) {
                    folderArrayList.addAll(getAllVideo(singleFile, allVideos))
                } else if (singleFile.name.lowercase(Locale.getDefault()).endsWith(".mp4")) {
                            val videoModel = VideoModel(
                                singleFile,
                                DateFormat.format("dd/MM/yyyy", singleFile.lastModified())
                                    .toString()
                            )
                            fileArrayList.add(videoModel)
                            allVideos.add(videoModel)
                }
            }

            if (fileArrayList.isNotEmpty()) {
                folderArrayList.add(FolderVideo(file.name, ArrayList(fileArrayList)))
            }
        }

        return@withContext folderArrayList
    }

    fun merge(s: String): ArrayList<Folder> {
        when (s) {
            "All" -> {
                folderList = mergeFolders(folderImage, folderVideo)
            }
            "Image" -> {
                folderList = mergeFolders(folderImage, null)
            }
            "Video" -> {
                folderList = mergeFolders(null, folderVideo)
            }
        }
        return folderList
    }

    private fun mergeFolders(
        folderImgs: ArrayList<FolderImg>?, folderVideos: ArrayList<FolderVideo>?
    ): ArrayList<Folder> {
        val folderMap = mutableMapOf<String, ArrayList<Any>>()

        folderImgs?.forEach { folderImg ->
            val name = folderImg.name ?: return@forEach
            val list = folderImg.listImg ?: return@forEach

            folderMap.getOrPut(name) { arrayListOf() }.addAll(list)
        }

        folderVideos?.forEach { folderVideo ->
            val name = folderVideo.name ?: return@forEach
            val list = folderVideo.listVideo ?: return@forEach

            folderMap.getOrPut(name) { arrayListOf() }.addAll(list)
        }

        val mergedFolders = ArrayList<Folder>()
        folderMap.forEach { (name, list) ->

            list.sortWith { o1, o2 ->
                val lastModified1 = when (o1) {
                    is ImgModel -> o1.file?.lastModified() ?: 0L
                    is VideoModel -> o1.file?.lastModified() ?: 0L
                    else -> 0L
                }

                val lastModified2 = when (o2) {
                    is ImgModel -> o2.file?.lastModified() ?: 0L
                    is VideoModel -> o2.file?.lastModified() ?: 0L
                    else -> 0L
                }

                lastModified2.compareTo(lastModified1)
            }
            mergedFolders.add(Folder(name = name, list = ArrayList(list)))
        }

        return mergedFolders
    }

    fun setFolder(position: Int){
        folder=folderList[position]
    }

    fun getFolder(): Folder? {
        return folder
    }

}