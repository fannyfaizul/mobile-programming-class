package com.fannyfaizul.bbc

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fannyfaizul.bbc.databinding.ActivityMainBinding
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var kodekamera = 222
    private var MY_PERMISSIONS_REQUEST_CAMERA = 224

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askCameraPermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonTakePhoto.setOnClickListener {
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun proseskamera(datanya: Intent?) {
        val bm = datanya?.extras?.get("data") as Bitmap
//        Toast.makeText(this, "Test $bm", Toast.LENGTH_SHORT).show()

        binding.imageViewResult.setImageBitmap(bm)

        val stream = ByteArrayOutputStream()

        bm.compress(Bitmap.CompressFormat.PNG, 100, stream)

        val byteArray = stream.toByteArray()
        val d = Date()
        val date = d.toString().replace(" ", "_").replace(":", "_")
        val filename = "IMG_$date.png"

        GlobalScope.launch {
            val urlString = "https://78fe-180-247-168-75.ngrok-free.app/predict"
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=*****")

            val outputStream = connection.outputStream
            val writer = DataOutputStream(outputStream)
            writer.writeBytes("--*****\r\n")
            writer.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"$filename\"\r\n")
            writer.writeBytes("Content-Type: image/jpeg\r\n\r\n")
            writer.write(byteArray)
            writer.writeBytes("\r\n")
            writer.writeBytes("--*****--\r\n")
            writer.flush()
            writer.close()

            val responseCode = connection.responseCode

            Log.d("Response Code", responseCode.toString())
            Log.d("Response Message", connection.responseMessage.toString())
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use(BufferedReader::readText)
                Log.d("Response", response)

                val jsonResponse = JSONObject(response)
                val classifications = mutableListOf<Pair<String, Double>>()

                // Iterate over keys and add to list
                jsonResponse.keys().forEach {
                    classifications.add(it to jsonResponse.getDouble(it))
                }

                // Sort classifications by confidence value in descending order
                classifications.sortByDescending { it.second }

                // Update UI on UI thread
                runOnUiThread {
                    // Ensure there are at least three classifications
                    if (classifications.size > 0) {
                        binding.textViewTop1.text = classifications[0].first
                        binding.textViewTop1Confidence.text = String.format("%.2f%%", classifications[0].second)
                    }
                    if (classifications.size > 1) {
                        binding.textViewTop2.text = classifications[1].first
                        binding.textViewTop2Confidence.text = String.format("%.2f%%", classifications[1].second)
                    }
                    if (classifications.size > 2) {
                        binding.textViewTop3.text = classifications[2].first
                        binding.textViewTop3Confidence.text = String.format("%.2f%%", classifications[2].second)
                    }

                    Toast.makeText(this@MainActivity, "Upload Success", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Upload Failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
        createFolder()
        val imagesFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "Server Camera App"
        val folder = File(imagesFolder)
        val output = File(folder, filename)

        val fo = FileOutputStream(output)
        fo.write(byteArray)
        fo.flush()
        fo.close()
        Toast.makeText(this, "Data telah dimuat ke ImageView", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            MY_PERMISSIONS_REQUEST_CAMERA -> {
                if(grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Camera Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun askCameraPermission(){
        val cameraPermission = checkSelfPermission(Manifest.permission.CAMERA)
        if(cameraPermission != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
        }
        else{
            Toast.makeText(this, "Camera Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createFolder() {
        val imagesFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + File.separator + "Server Camera App"
        val folder = File(imagesFolder)
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                // Folder created successfully
                Toast.makeText(this, "Ok let's move out!!", Toast.LENGTH_SHORT).show()
            } else {
                // Failed to create the folder
                Toast.makeText(this, "Failed to create folder", Toast.LENGTH_SHORT).show()
            }
        }
    }
}