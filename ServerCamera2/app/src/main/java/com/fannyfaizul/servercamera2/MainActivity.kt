package com.fannyfaizul.servercamera2

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fannyfaizul.servercamera2.databinding.ActivityMainBinding
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.Manifest
import android.content.pm.PackageManager
import android.text.format.DateFormat
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Date


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var kodekamera = 222
    private var MY_PERMISSIONS_REQUEST_WRITE = 223
    private var MY_PERMISSIONS_REQUEST_CAMERA = 224

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askWritePermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, kodekamera)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode) {
                kodekamera -> proseskamera(data)
            }
        }
    }

    private fun proseskamera(datanya: Intent?) {
        val bm = datanya?.extras?.get("data") as Bitmap
        Toast.makeText(this, "Test $bm", Toast.LENGTH_SHORT).show()

        binding.imgView.setImageBitmap(bm)

        val stream = ByteArrayOutputStream()

        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream)

        val byteArray = stream.toByteArray()

        val imagesFolder = File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "HasilFoto")
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs()
        }
        val d = Date()
        val fileName = DateFormat.format("yyyyMMdd-hh-mm-ss", d.time).toString() + ".jpg"
        val output = File(imagesFolder, fileName)

        val fo = FileOutputStream(output)
        fo.write(byteArray)
        fo.flush()
        fo.close()
        Toast.makeText(this, "Data telah dimuat ke ImageView", Toast.LENGTH_SHORT).show()
    }

    private fun askWritePermission(){
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE)
            }
            else {
                Toast.makeText(this, "Write permission already granted", Toast.LENGTH_SHORT).show()
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
            }
            else {
                Toast.makeText(this, "Camera permission already granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Izin penyimpanan diberikan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Izin penyimpanan ditolak", Toast.LENGTH_SHORT).show()
                }
            }

            MY_PERMISSIONS_REQUEST_CAMERA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Izin kamera diberikan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}