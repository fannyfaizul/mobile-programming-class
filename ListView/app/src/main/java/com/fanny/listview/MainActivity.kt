package com.fanny.listview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fanny.listview.databinding.ActivityMainBinding
import com.fanny.listview.databinding.AddContactBinding
import com.fanny.listview.databinding.EditContactBinding
import com.fanny.listview.databinding.SearchContactBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myDB: SQLiteDatabase
    private lateinit var openDB: SQLiteOpenHelper
    private lateinit var mAdapter: KontakAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAdapter = KontakAdapter()
        createDatabase()

        binding.lvKontak.adapter = mAdapter

        tampilSemuaKontak()

        binding.btnAdd.setOnClickListener {
            tampilkanDialogAddContact()
        }

        binding.btnSearch.setOnClickListener {
            tampilkanDialogSearch()
        }

        mAdapter.setOnEdit {
            TampilkanDialogEdit(it)
        }

        mAdapter.setOnDelete {
            TampilkanDialogHapus(it)
        }

        binding.btnShowAll.setOnClickListener {
            tampilSemuaKontak()
        }

    }

    private fun createDatabase() {
        openDB = object : SQLiteOpenHelper(this, "crud", null, 1) {
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS kontak (nama TEXT, nohp TEXT)")
            }

            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                db.execSQL("DROP TABLE IF EXISTS kontak")
                onCreate(db)
            }
        }

        myDB = openDB.writableDatabase
    }

    private fun tampilkanDialogAddContact() {
        val contactBinding = AddContactBinding.inflate(layoutInflater)

        val dialognya = AlertDialog.Builder(this)
        dialognya.setView(contactBinding.root)

        dialognya
            .setCancelable(false)
            .setPositiveButton("Simpan") { dialog, _ ->
                val nama = contactBinding.etNama.text.toString()
                val nohp = contactBinding.etnoHP.text.toString()
                if (nama.isNotEmpty() && nohp.isNotEmpty()) {
                    val cursor = myDB.rawQuery("SELECT nama, nohp FROM kontak WHERE nama = '$nama'", null)
                    if (cursor.count > 0) {
                        Toast.makeText(this, "Nama sudah terdaftar", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    myDB.execSQL("INSERT INTO kontak VALUES ('$nama', '$nohp')")
                    cursor.close()
                    Toast.makeText(this, "Berhasil menambahkan kontak", Toast.LENGTH_SHORT).show()
                    tampilSemuaKontak()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Nama dan No HP tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @SuppressLint("Range")
    private fun tampilkanDialogSearch() {
        val dialognya = AlertDialog.Builder(this)
        val SearchBinding = SearchContactBinding.inflate(layoutInflater)
        val nama = SearchBinding.etNamaEdit.text.toString()

        dialognya.setView(SearchBinding.root)

        dialognya
            .setCancelable(false)
            .setPositiveButton("Cari") { dialog, _ ->
                val nama = SearchBinding.etNamaEdit.text.toString()
                cariKontak(nama)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun TampilkanDialogEdit(kontak: Kontak) {
        val dialognya = AlertDialog.Builder(this)
        val editBinding = EditContactBinding.inflate(layoutInflater)

        dialognya.setView(editBinding.root)

        editBinding.prevnama.text = kontak.nama
        editBinding.prevnohp.text = kontak.nohp

        dialognya
            .setCancelable(false)
            .setPositiveButton("Simpan") { dialog, _ ->
                val nama = editBinding.etNamaEdit.text.toString()
                val nohp = editBinding.etnoHPEdit.text.toString()
                if (nama.isNotEmpty() && nohp.isNotEmpty()) {
                    myDB.execSQL("UPDATE kontak SET nohp = '$nohp', nama = '$nama' WHERE nama = '${kontak.nama}'")
                    tampilSemuaKontak()
                    Toast.makeText(this, "Berhasil mengubah kontak", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Nama dan No HP tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun TampilkanDialogHapus(kontak: Kontak) {
        val dialognya = AlertDialog.Builder(this)
        dialognya
            .setTitle("Hapus Kontak")
            .setMessage("Apakah Anda yakin ingin menghapus kontak ${kontak.nama}?")
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, _ ->
                myDB.execSQL("DELETE FROM kontak WHERE nama = '${kontak.nama}'")
                tampilSemuaKontak()
                Toast.makeText(this, "Berhasil menghapus kontak", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun tampilSemuaKontak() {
        val cursor = myDB.rawQuery("SELECT nama, nohp FROM kontak", null)
        if (cursor.count > 0) {
            val list = mutableListOf<Kontak>()
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val DBNama = cursor.getString(0)
                val DBnoHP = cursor.getString(1)
                list.add(Kontak(DBNama, DBnoHP))
                cursor.moveToNext()
            }
            mAdapter.setList(list)
        } else {
            Toast.makeText(this, "Tidak ada kontak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cariKontak(nama: String) {
        if (nama.isNotEmpty()) {
            val cursor = myDB.rawQuery("SELECT nama, nohp FROM kontak WHERE nama like '%$nama%'", null)
            if (cursor.count > 0) {
                val list = mutableListOf<Kontak>()
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val DBNama = cursor.getString(0)
                    val DBnoHP = cursor.getString(1)
                    list.add(Kontak(DBNama, DBnoHP))
                    cursor.moveToNext()
                }
                mAdapter.setList(list)
                Toast.makeText(this, "Ditemukan ${cursor.count} kontak", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Kontak tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }
}