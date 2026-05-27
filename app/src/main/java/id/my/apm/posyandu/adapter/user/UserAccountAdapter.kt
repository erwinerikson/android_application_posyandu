package id.my.apm.posyandu.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.my.apm.posyandu.databinding.CardLayoutUserAccountBinding
import id.my.apm.posyandu.model.User
import id.my.apm.posyandu.utils.AppUtils.showToast

class UserAccountAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<UserAccountAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = CardLayoutUserAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listUser[position]

        holder.tvName.text = data.nama
        holder.tvAlamat.text = data.alamat

        holder.tlSelect.setOnClickListener {
            if (data.id == "1") {
                holder.itemView.context.showToast("Akun tidak bisa di ubah!")
            } else {
                onItemClickCallback?.onItemClicked(data)
            }
        }
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(binding: CardLayoutUserAccountBinding) : RecyclerView.ViewHolder(binding.root) {
        var tvName = binding.tvClUserAccountNama
        var tvAlamat = binding.tvClUserAccountAlamat
        var tlSelect = binding.tlClUserAccount
    }
}