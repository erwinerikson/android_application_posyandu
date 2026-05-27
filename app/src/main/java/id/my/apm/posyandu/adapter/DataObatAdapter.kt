package id.my.apm.posyandu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutDataObatBinding
import id.my.apm.posyandu.model.DataMedication
import id.my.apm.posyandu.utils.AppUtils

class DataObatAdapter(private val listMedication: ArrayList<DataMedication>) : RecyclerView.Adapter<DataObatAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataMedication)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutDataObatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listMedication[position]

        holder.tvStok.text = data.stok
        holder.tvName.text = data.nama
        holder.tvHarga.text = AppUtils.formatCurrency(data.harga)

        holder.cvSelect.setOnClickListener {
            onItemClickCallback?.onItemClicked(data)
        }
    }

    override fun getItemCount(): Int = listMedication.size

    class ListViewHolder(binding: CardLayoutDataObatBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvStok = binding.tvClDataObatStok
        var tvName = binding.tvClDataObatNama
        var tvHarga = binding.tvClDataObatHarga
        var cvSelect = binding.tlClDataObat
    }
}