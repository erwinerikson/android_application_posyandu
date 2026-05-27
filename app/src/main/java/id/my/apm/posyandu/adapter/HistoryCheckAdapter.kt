package id.my.apm.posyandu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutRiwayatCheckBinding
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.utils.AppUtils

class HistoryCheckAdapter(private val listCheck: ArrayList<CheckHistory>) : RecyclerView.Adapter<HistoryCheckAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: CheckHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutRiwayatCheckBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listCheck[position]

        holder.tvTgl.text = AppUtils.formatterDateNumber(data.tgl)
        holder.tvName.text = data.nama
        holder.tvTinggi.text = data.tinggi

        holder.tvBerat.setOnClickListener {
            onItemClickCallback?.onItemClicked(data)
        }
    }

    override fun getItemCount(): Int = listCheck.size

    class ListViewHolder(binding: CardLayoutRiwayatCheckBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvTgl = binding.tvClHistoryCheckTgl
        var tvName = binding.tvClHistoryCheckItem
        var tvTinggi = binding.tvClHistoryCheckTinggi
        var tvBerat = binding.tvClHistoryCheckBerat
    }
}