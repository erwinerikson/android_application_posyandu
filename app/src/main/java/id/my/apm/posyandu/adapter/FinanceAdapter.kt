package id.my.apm.posyandu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutFinanceBinding
import id.my.apm.posyandu.model.DataFinance
import id.my.apm.posyandu.utils.AppUtils

class FinanceAdapter(private val listFinance: ArrayList<DataFinance>) : RecyclerView.Adapter<FinanceAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataFinance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutFinanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listFinance[position]

        holder.tvTgl.text = AppUtils.formatterDateNumber(data.tgl)
        holder.tvNominal.text = AppUtils.formatCurrency(data.nominal)
        holder.tvSumber.text = data.sumber

        holder.cvSelect.setOnClickListener {
            onItemClickCallback?.onItemClicked(data)
        }
    }

    override fun getItemCount(): Int = listFinance.size

    class ListViewHolder(binding: CardLayoutFinanceBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvTgl = binding.tvClFinanceTgl
        var tvNominal = binding.tvClFinanceNominal
        var tvSumber = binding.tvClFinanceSumber
        var cvSelect = binding.clFinance
    }
}