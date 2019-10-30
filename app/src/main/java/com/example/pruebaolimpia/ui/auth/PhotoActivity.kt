package com.example.pruebaolimpia.ui.auth

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.pruebaolimpia.BuildConfig
import com.example.pruebaolimpia.R
import kotlinx.android.synthetic.main.activity_photo.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.media.MediaScannerConnection
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R



class PhotoActivity : AppCompatActivity(), View.OnClickListener {

    private val REQUEST_TAKE_PHOTO = 100

    private val REQUEST_GALLERY_PHOTO = 200

    private var mPhotoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == this.RESULT_CANCELED) {
            return
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
                    imageview.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data!!.extras!!.get("data") as Bitmap?
            imageview.setImageBitmap(thumbnail)
            saveImage(thumbnail!!)
            Toast.makeText(this@MainActivity, "Image Saved!", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(
                wallpaperDirectory, Calendar.getInstance()
                    .timeInMillis.toString() + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath())

            return f.getAbsolutePath()
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }

    override fun onClick(v: View?) {
        when (v) {
            ivGallery -> {

            }
            ivCamera -> {

            }
        }
    }
}
