package id.my.apm.posyandu.adapter.elderly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutSelectBabyPubBinding
import id.my.apm.posyandu.model.ElderlyPub

class ElderlyPubSelectAdapter(private val listElderlyPub: ArrayList<ElderlyPub>) : RecyclerView.Adapter<ElderlyPubSelectAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutSelectBabyPubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, nama) = listElderlyPub[position]

        holder.tvName.text = nama

        holder.cvSelect.setOnClickListener {
            onItemClickCallback?.onItemClicked(id)
        }
    }

    override fun getItemCount(): Int = listElderlyPub.size

    class ListViewHolder(binding: CardLayoutSelectBabyPubBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvName = binding.tvCvSelectPubBayiNama
        var cvSelect = binding.cvSelectPubBayi
    }
}