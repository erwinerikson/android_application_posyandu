package id.my.apm.posyandu.adapter.pregnant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutRiwayatImunPregnantBinding
import id.my.apm.posyandu.model.PregnantImmunization
import id.my.apm.posyandu.utils.AppUtils

class PregnantImmunizationAdapter(private val listImmunization: ArrayList<PregnantImmunization>) : RecyclerView.Adapter<PregnantImmunizationAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutRiwayatImunPregnantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, tgl, nama) = listImmunization[position]

        holder.tvTgl.text = AppUtils.formatterDateNumber(tgl)
        holder.tvName.text = nama
    }

    override fun getItemCount(): Int = listImmunization.size

    class ListViewHolder(binding: CardLayoutRiwayatImunPregnantBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvTgl = binding.tvClRiwayatImunPregnantTgl
        var tvName = binding.tvClRiwayatImunPregnantNama
    }
}