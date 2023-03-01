package de.max.troegel.gmaf.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.max.troegel.gmaf.ui.fragment.ResultFragment
import de.max.troegel.gmaf.ui.fragment.SearchFragment
import de.max.troegel.gmaf.ui.fragment.SettingsFragment

/**
 * Fragment adapter that is used for the navigation between the available main fragments
 */
class FragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * The list of available main fragments
     */
    private val fragmentsCreator: Map<Int, () -> Fragment> = mapOf(
        SEARCH_PAGE_INDEX to { SearchFragment() },
        RESULTS_PAGE_INDEX to { ResultFragment() },
        SETTINGS_PAGE_INDEX to { SettingsFragment() }
    )

    override fun getItemCount() = fragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return fragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}

const val SEARCH_PAGE_INDEX = 0
const val RESULTS_PAGE_INDEX = 1
const val SETTINGS_PAGE_INDEX = 2