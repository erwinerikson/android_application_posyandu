package id.my.apm.posyandu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutDataPeriksaBinding
import id.my.apm.posyandu.model.DataCheck
import id.my.apm.posyandu.utils.AppUtils

class DataPeriksaAdapter(private val listCheck: ArrayList<DataCheck>) : RecyclerView.Adapter<DataPeriksaAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutDataPeriksaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listCheck[position]

        holder.tvName.text = data.nama
        holder.tvHarga.text = AppUtils.formatCurrency(data.harga)

        holder.cvSelect.setOnClickListener {
            onItemClickCallback?.onItemClicked(data)
        }
    }

    override fun getItemCount(): Int = listCheck.size

    class ListViewHolder(binding: CardLayoutDataPeriksaBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvName = binding.tvClDataPeriksaNama
        var tvHarga = binding.tvClDataPeriksaHarga
        var cvSelect = binding.tlClDataPeriksa
    }
}