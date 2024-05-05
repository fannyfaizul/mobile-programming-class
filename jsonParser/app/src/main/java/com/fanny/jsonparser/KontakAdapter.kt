package com.fanny.jsonparser

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fanny.jsonparser.databinding.ItemKontakBinding

class KontakAdapter : RecyclerView.Adapter<KontakAdapter.ViewHolder>() {

    private val list = ArrayList<Kontak>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<Kontak>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KontakAdapter.ViewHolder {
        return ViewHolder(
            ItemKontakBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: KontakAdapter.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(
        private val binding: ItemKontakBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            data: Kontak
        ) {
            binding.tNama.text = data.nama
            binding.tPhone.text = data.nohp
            binding.tEmail.text = data.email

        }
    }
}