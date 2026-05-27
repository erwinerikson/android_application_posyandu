package id.my.apm.posyandu.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.my.apm.posyandu.ui.finance.FinanceDetailsFragment
import id.my.apm.posyandu.ui.finance.FinanceInFragment
import id.my.apm.posyandu.ui.finance.FinanceOutFragment

class ViewPagerFinanceAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragmentList = listOf(
        FinanceInFragment(),
        FinanceOutFragment(),
        FinanceDetailsFragment()
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}