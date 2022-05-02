package com.example.dialogexample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import java.io.File

object ImageController {
    fun selectPhotoFromGallery(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    fun saveImage(context: Context, id: Int, uri: Uri) {
        val file = File(context.filesDir, id.toString())

        val bytes = context.contentResolver.openInputStream(uri)?.readBytes()!!

        file.writeBytes(bytes)
    }

    fun getImageUri(context: Context, id: Int): Uri {
        val file = File(context.filesDir, id.toString())

        return if (file.exists()) Uri.fromFile(file)
        else Uri.parse("android.resource://com.example.dialogexample/drawable/placeholder")
    }

    fun deleteImage(context: Context, id: Int) {
        val file = File(context.filesDir, id.toString())
        file.delete()
    }
}