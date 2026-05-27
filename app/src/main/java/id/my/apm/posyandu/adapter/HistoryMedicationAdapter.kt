package id.my.apm.posyandu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutRiwayatObatBinding
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.utils.AppUtils

class HistoryMedicationAdapter(private val listMedication: ArrayList<MedicationHistory>) : RecyclerView.Adapter<HistoryMedicationAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutRiwayatObatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, tgl, nama, qty) = listMedication[position]

        holder.tvTgl.text = AppUtils.formatterDateNumber(tgl)
        holder.tvName.text = nama
        holder.tvQty.text = qty
    }

    override fun getItemCount(): Int = listMedication.size

    class ListViewHolder(binding: CardLayoutRiwayatObatBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvTgl = binding.tvClHistoryObatTgl
        var tvName = binding.tvClHistoryObatNama
        var tvQty = binding.tvClHistoryObatQty
    }
}