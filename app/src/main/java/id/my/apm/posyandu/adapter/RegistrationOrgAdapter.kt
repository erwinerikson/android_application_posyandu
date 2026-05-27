package id.my.apm.posyandu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutRegistrationOrgBinding
import id.my.apm.posyandu.model.RegistrationPub

class RegistrationOrgAdapter(
    private val idMin: String,
    private val listRegistration: ArrayList<RegistrationPub>
) : RecyclerView.Adapter<RegistrationOrgAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: RegistrationPub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutRegistrationOrgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listRegistration[position]

        val bayi = data.bayi
        val ibu = data.ibu
        val ibuHamil = data.ibuhamil
        val lansia = data.lansia

        val layanan = if (bayi == "1" && ibu == "1" && ibuHamil == "1" && lansia == "1") {
            "Bayi, Ibu, Ibu Hamil, Lansia"
        } else if (bayi == "1" && ibu == "1" && ibuHamil == "1") {
            "Bayi, Ibu, Ibu Hamil"
        } else if (bayi == "1" && ibu == "1" && lansia == "1") {
            "Bayi, Ibu, Lansia"
        } else if (bayi == "1" && ibuHamil == "1" && lansia == "1") {
            "Bayi, Ibu Hamil, Lansia"
        } else if (ibu == "1" && ibuHamil == "1" && lansia == "1") {
            "Ibu, Ibu Hamil, Lansia"
        } else if (bayi == "1" && ibu == "1") {
            "Bayi, Ibu"
        } else if (bayi == "1" && ibuHamil == "1") {
            "Bayi, Ibu Hamil"
        } else if (bayi == "1" && lansia == "1") {
            "Bayi, Lansia"
        } else if (ibu == "1" && ibuHamil == "1") {
            "Ibu, Ibu Hamil"
        } else if (ibu == "1" && lansia == "1") {
            "Ibu, Lansia"
        } else if (ibuHamil == "1" && lansia == "1") {
            "Ibu Hamil, Lansia"
        } else if (bayi == "1") {
            "Bayi"
        } else if (ibu == "1") {
            "Ibu"
        } else if (ibuHamil == "1") {
            "Ibu Hamil"
        } else if (lansia == "1") {
            "Lansia"
        } else {
            ""
        }

        val antrian = (Integer.parseInt(data.id) - Integer.parseInt(idMin)) + 1

        holder.tvAntrian.text = antrian.toString()
        holder.tvLayanan.text = layanan
        holder.tvName.text = data.nama

        holder.cvSelect.setOnClickListener {
            onItemClickCallback?.onItemClicked(data)
        }
    }

    override fun getItemCount(): Int = listRegistration.size

    class ListViewHolder(binding: CardLayoutRegistrationOrgBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvAntrian = binding.tvClRegistrationOrgAntrian
        var tvLayanan = binding.tvClRegistrationOrgLayanan
        var tvName = binding.tvClRegistrationOrgNama
        var cvSelect = binding.tlClRegistrationOrg
    }
}