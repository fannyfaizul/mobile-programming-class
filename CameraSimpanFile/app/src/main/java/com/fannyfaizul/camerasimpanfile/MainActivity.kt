package com.fannyfaizul.camerasimpanfile

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fannyfaizul.camerasimpanfile.databinding.ActivityMainBinding
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var kodekamera = 222
    private var MY_PERMISSIONS_REQUEST_WRITE = 223

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

        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)

        val byteArray = stream.toByteArray()

        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val output = File(dir, "simpan.png")

        val fo = FileOutputStream(output)
        fo.write(byteArray)
        fo.flush()
        fo.close()
        Toast.makeText(this, "Data telah dimuat ke ImageView", Toast.LENGTH_SHORT).show()
    }

    private fun askWritePermission(){
        if(android.os.Build.VERSION.SDK_INT >= 23){
            val cameraPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if(cameraPermission != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), MY_PERMISSIONS_REQUEST_WRITE)
            }
        }
    }
}