package com.fanny.crud

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fanny.crud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myDB: SQLiteDatabase
    private lateinit var sqliteHelper: SQLiteOpenHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // create database
        createDatabase()

        binding.btnCreate.setOnClickListener {
            val nrp = binding.etNrp.text.toString()
            val nama = binding.etNama.text.toString()
            insertData(nrp, nama)
        }

        binding.btnRead.setOnClickListener {
            readData()
        }

        binding.btnUpdate.setOnClickListener {
            val nrp = binding.etNrp.text.toString()
            val nama = binding.etNama.text.toString()
            updateData(nrp, nama)
        }

        binding.btnDelete.setOnClickListener {
            val nrp = binding.etNrp.text.toString()
            deleteData(nrp)
        }
    }

    private fun createDatabase() {
        sqliteHelper = object : SQLiteOpenHelper(this, "crud", null, 1) {
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS mahasiswa (nrp TEXT PRIMARY KEY, nama TEXT)")
            }

            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                db.execSQL("DROP TABLE IF EXISTS mahasiswa")
                onCreate(db)
            }
        }

        myDB = sqliteHelper.writableDatabase
    }

    override fun onStop() {
        myDB.close()
        sqliteHelper.close()
        super.onStop()
    }

    private fun insertData(nrp: String, nama: String) {
        if (nrp.isEmpty() || nama.isEmpty()) {
            Toast.makeText(this, "NRP atau Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }
        val cursor = myDB.rawQuery("SELECT nama, nrp FROM mahasiswa WHERE nrp = '$nrp'", null)
        if (cursor.count > 0) {
            Toast.makeText(this, "NRP sudah ada", Toast.LENGTH_SHORT).show()
            return
        }
        myDB.execSQL("INSERT INTO mahasiswa VALUES ('$nrp', '$nama')")
        Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()
    }

    private fun readData() {
        binding.readResult.text = ""
        val cursor = myDB.rawQuery("SELECT nrp, nama FROM mahasiswa", null)
        if (cursor.count == 0) {
            Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show()
            return
        } else {
            binding.readResult.append("Data yang ditemukan:\n")
            Toast.makeText(this, "Ditemukan ${cursor.count} data", Toast.LENGTH_SHORT).show()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val nrp = cursor.getString(0)
                val nama = cursor.getString(1)
                justReadData(nrp, nama)
                cursor.moveToNext()
            }
        }
        cursor.close()
    }

    private fun updateData(nrp: String, nama: String) {
        if (nrp.isEmpty() || nama.isEmpty()) {
            Toast.makeText(this, "NRP atau Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }
        val cursor = myDB.rawQuery("SELECT nama, nrp FROM mahasiswa WHERE nrp = '$nrp'", null)
        if (cursor.count == 0) {
            Toast.makeText(this, "NRP tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }
        myDB.execSQL("UPDATE mahasiswa SET nama = '$nama' WHERE nrp = '$nrp'")
        Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
    }

    private fun deleteData(nrp: String) {
        if (nrp.isEmpty()) {
            Toast.makeText(this, "Untuk penghapusan data, NRP tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }
        val cursor = myDB.rawQuery("SELECT nama, nrp FROM mahasiswa WHERE nrp = '$nrp'", null)
        if (cursor.count == 0) {
            Toast.makeText(this, "NRP tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }
        myDB.execSQL("DELETE FROM mahasiswa WHERE nrp = '$nrp'")
        Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
    }

    private fun justReadData(nrp: String, nama: String) {
        binding.readResult.append("NRP: $nrp, Nama: $nama\n")
    }

}