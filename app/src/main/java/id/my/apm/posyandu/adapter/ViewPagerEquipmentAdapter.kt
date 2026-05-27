package id.my.apm.posyandu.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.my.apm.posyandu.ui.medication.CheckFragment
import id.my.apm.posyandu.ui.medication.ImmunizationFragment
import id.my.apm.posyandu.ui.medication.MedicationFragment
import id.my.apm.posyandu.ui.medication.MidwifeFragment

class ViewPagerEquipmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragmentList = listOf(
        MedicationFragment(),
        ImmunizationFragment(),
        CheckFragment(),
        MidwifeFragment()
    )

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MedicationFragment()
            1 -> ImmunizationFragment()
            2 -> CheckFragment()
            3 -> MidwifeFragment()
            else -> MedicationFragment()
        }
    }
}