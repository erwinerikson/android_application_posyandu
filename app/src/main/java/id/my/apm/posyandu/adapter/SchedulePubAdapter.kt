package id.my.apm.posyandu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutSchedulePubBinding
import id.my.apm.posyandu.model.SchedulePub
import id.my.apm.posyandu.utils.AppUtils

class SchedulePubAdapter(private val listSchedule: ArrayList<SchedulePub>) : RecyclerView.Adapter<SchedulePubAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutSchedulePubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (_, tgl, ket, status) = listSchedule[position]

        val stt = when (status) {
            "1" -> {
                "Berlangsung"
            }
            "2" -> {
                "Selesai"
            }
            else -> {
                "Akan Datang"
            }
        }

        holder.tvTgl.text = AppUtils.formatterDateNumber(tgl)
        holder.tvKet.text = ket
        holder.tvStatus.text = stt
    }

    override fun getItemCount(): Int = listSchedule.size

    class ListViewHolder(binding: CardLayoutSchedulePubBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvTgl = binding.tvClSchedulePubTgl
        var tvKet = binding.tvClSchedulePubKet
        var tvStatus = binding.tvClSchedulePubStatus
    }
}