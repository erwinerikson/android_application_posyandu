package id.my.apm.posyandu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutOrganizerBinding
import id.my.apm.posyandu.model.RegistrationPub

class OrganizerAdapter(
    private val idMin: String,
    private val listOrganizer: ArrayList<RegistrationPub>
) : RecyclerView.Adapter<OrganizerAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: RegistrationPub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutOrganizerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listOrganizer[position]

        val antrian = (Integer.parseInt(data.id) - Integer.parseInt(idMin)) + 1
        val status = when (data.status) {
            "1" -> {
                "Proses"
            }
            "2" -> {
                "Selesai"
            }
            else -> {
                "Antri"
            }
        }

        holder.tvAntrian.text = antrian.toString()
        holder.tvName.text = data.nama
        holder.tvStatus.text = status

        holder.cvSelect.setOnClickListener {
            onItemClickCallback?.onItemClicked(data)
        }
    }

    override fun getItemCount(): Int = listOrganizer.size

    class ListViewHolder(binding: CardLayoutOrganizerBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvAntrian = binding.tvClOrganizerAntri
        var tvName = binding.tvClOrganizerNama
        var tvStatus = binding.tvClOrganizerStatus
        var cvSelect = binding.clOrganizer
    }
}