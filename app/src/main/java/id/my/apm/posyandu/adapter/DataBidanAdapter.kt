package id.my.apm.posyandu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutDataPeriksaBinding
import id.my.apm.posyandu.model.DataMidwife

class DataBidanAdapter(private val listMidwife: ArrayList<DataMidwife>) : RecyclerView.Adapter<DataBidanAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataMidwife)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutDataPeriksaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listMidwife[position]

        holder.tvName.text = data.nama
        holder.tvHarga.text = data.id
        holder.tvHarga.textAlignment = View.TEXT_ALIGNMENT_CENTER

        holder.cvSelect.setOnClickListener {
            onItemClickCallback?.onItemClicked(data)
        }
    }

    override fun getItemCount(): Int = listMidwife.size

    class ListViewHolder(binding: CardLayoutDataPeriksaBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvName = binding.tvClDataPeriksaNama
        var tvHarga = binding.tvClDataPeriksaHarga
        var cvSelect = binding.tlClDataPeriksa
    }
}