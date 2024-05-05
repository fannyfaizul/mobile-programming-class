package com.fanny.listview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fanny.listview.databinding.ItemUserBinding

class KontakAdapter : RecyclerView.Adapter<KontakAdapter.ViewHolder>() {

    private var onEdit: (Kontak) -> Unit = {}
    private var onDelete: (Kontak) -> Unit = {}
    private val list = ArrayList<Kontak>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Kontak>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addItem(data: Kontak){
        this.list.add(data)
        notifyDataSetChanged()
    }

    fun setOnEdit(onEdit: (Kontak) -> Unit){
        this.onEdit = onEdit
    }

    fun setOnDelete(onDelete: (Kontak) -> Unit){
        this.onDelete = onDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KontakAdapter.ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: KontakAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], onEdit, onDelete)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(
        private val binding: ItemUserBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: Kontak,
            onEdit: (Kontak) -> Unit,
            onDelete: (Kontak) -> Unit
        ) {
            binding.tNama.text = data.nama + "\t" + data.nohp

            binding.btnEdit.setOnClickListener {
                onEdit.invoke(data)
            }

            binding.btnDelete.setOnClickListener {
                onDelete.invoke(data)
            }
        }
    }
}