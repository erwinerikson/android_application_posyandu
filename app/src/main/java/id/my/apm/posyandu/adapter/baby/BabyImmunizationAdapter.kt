package id.my.apm.posyandu.adapter.baby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutRiwayatImunBabyBinding
import id.my.apm.posyandu.model.ChildImmunization
import id.my.apm.posyandu.utils.AppUtils

class BabyImmunizationAdapter(private val listImmunization: ArrayList<ChildImmunization>) : RecyclerView.Adapter<BabyImmunizationAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutRiwayatImunBabyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, tgl, nama, tinggi, berat) = listImmunization[position]

        holder.tvTgl.text = AppUtils.formatterDateNumber(tgl)
        holder.tvName.text = nama
        holder.tvTinggi.text = tinggi
        holder.tvBerat.text = berat
    }

    override fun getItemCount(): Int = listImmunization.size

    class ListViewHolder(binding: CardLayoutRiwayatImunBabyBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvTgl = binding.tvClRiwayatImunBabyTgl
        var tvName = binding.tvClRiwayatImunBabyNama
        var tvTinggi = binding.tvClRiwayatImunBabyTinggi
        var tvBerat = binding.tvClRiwayatImunBabyBerat
    }
}