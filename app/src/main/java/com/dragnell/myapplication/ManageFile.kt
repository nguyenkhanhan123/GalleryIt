package com.dragnell.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.text.format.DateFormat
import android.util.Log
import com.dragnell.myapplication.model.FolderImg
import com.dragnell.myapplication.model.FolderVideo
import com.dragnell.myapplication.model.ImgModel
import com.dragnell.myapplication.model.VideoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

class ManageFile {
    private var folderImage: ArrayList<FolderImg> = ArrayList()

    private var folderVideo: ArrayList<FolderVideo> = ArrayList()

    companion object {
        val instance: ManageFile by lazy { ManageFile() }
    }

    suspend fun getImgAndVideoModel() {
        withContext(Dispatchers.IO) {
            val defaultDir = Environment.getExternalStorageDirectory()
            val customDir = File("/storage/ace-999")

            folderImage.clear()
            folderVideo.clear()

            val imgFromDefaultDir = async { getAllImg(defaultDir) }
            val videoFromDefaultDir = async { getAllVideo(defaultDir) }

            val imgFromCustomDir = async { if (customDir.exists()) getAllImg(customDir) else emptyList() }
            val videoFromCustomDir = async { if (customDir.exists()) getAllVideo(customDir) else emptyList() }

            folderImage.addAll(imgFromDefaultDir.await())
            folderVideo.addAll(videoFromDefaultDir.await())
            folderImage.addAll(imgFromCustomDir.await())
            folderVideo.addAll(videoFromCustomDir.await())
        }
    }

    fun getFolderImg(): ArrayList<FolderImg> {
        return folderImage
    }

    fun getFolderVideo(): ArrayList<FolderVideo> {
        return folderVideo
    }

    private suspend fun getAllVideo(file: File): ArrayList<FolderVideo> = withContext(Dispatchers.IO) {
        val folderArrayList = ArrayList<FolderVideo>()
        val files = file.listFiles()

        if (files != null) {
            val fileArrayList = ArrayList<VideoModel>()

            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden &&
                    !singleFile.name.contains("Telegram", true) &&
                    !singleFile.parent.contains("Telegram", true)
                ) {
                    folderArrayList.addAll(getAllVideo(singleFile)) // Đệ quy
                } else if (singleFile.name.lowercase(Locale.getDefault()).endsWith(".mp4")) {
                    val retriever = MediaMetadataRetriever()
                    try {
                        retriever.setDataSource(singleFile.absolutePath)
                        val firstFrame: Bitmap? = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST)
                        if (firstFrame != null) {
                            fileArrayList.add(
                                VideoModel(
                                    singleFile,
                                    DateFormat.format("dd/MM/yyyy", singleFile.lastModified()).toString(),
                                    firstFrame
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("ManageFile", "Error processing video: ${singleFile.name}, ${e.message}")
                    } finally {
                        retriever.release()
                    }
                }
            }

            if (fileArrayList.isNotEmpty()) {
                folderArrayList.add(FolderVideo(file.name, ArrayList(fileArrayList)))
            }
        }

        return@withContext folderArrayList
    }


    private suspend fun getAllImg(file: File): ArrayList<FolderImg> = withContext(Dispatchers.IO) {
        val folderArrayList = ArrayList<FolderImg>()
        val files = file.listFiles()

        if (files != null) {
            val fileArrayList = ArrayList<ImgModel>()

            for (singleFile in files) {
                if (singleFile.isDirectory && !singleFile.isHidden &&
                    !singleFile.name.contains("Telegram", true) &&
                    !singleFile.parent.contains("Telegram", true)
                ) {
                    folderArrayList.addAll(getAllImg(singleFile)) // Đệ quy
                } else if (singleFile.name.lowercase(Locale.getDefault()).run { endsWith(".jpg") || endsWith(".jpeg") || endsWith(".png") }) {
                    val bitmap = BitmapFactory.decodeFile(singleFile.absolutePath)
                    fileArrayList.add(
                        ImgModel(
                            singleFile,
                            DateFormat.format("dd/MM/yyyy", singleFile.lastModified()).toString(),
                            bitmap
                        )
                    )
                }
            }

            if (fileArrayList.isNotEmpty()) {
                folderArrayList.add(FolderImg(file.name, ArrayList(fileArrayList)))
            }
        }

        return@withContext folderArrayList
    }

}