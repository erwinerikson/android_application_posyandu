package id.my.apm.posyandu.adapter.baby

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutSelectBabyPubBinding
import id.my.apm.posyandu.model.BabyPub
import id.my.apm.posyandu.utils.AppUtils.showToast

class BabyPubSelectAdapter(private val listBabyPub: ArrayList<BabyPub>) : RecyclerView.Adapter<BabyPubSelectAdapter.ListViewHolder>() {
    /*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutSelectBabyPubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {}

    override fun getItemCount(): Int = listBabyPub.size

    class ListViewHolder(binding: CardLayoutSelectBabyPubBinding) : RecyclerView.ViewHolder(binding.root) {}
     */
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutSelectBabyPubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (id, _, nama) = listBabyPub[position]

        holder.tvName.text = nama

        holder.cvSelect.setOnClickListener {
            onItemClickCallback?.onItemClicked(id)
            //holder.itemView.context.showToast("Ini di klik")
        }
    }

    override fun getItemCount(): Int = listBabyPub.size

    class ListViewHolder(binding: CardLayoutSelectBabyPubBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvName = binding.tvCvSelectPubBayiNama
        var cvSelect = binding.cvSelectPubBayi
    }
}