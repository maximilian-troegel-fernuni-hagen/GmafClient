package de.max.troegel.gmaf.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import de.max.troegel.gmaf.R
import de.max.troegel.gmaf.data.model.GmafMediaList
import de.max.troegel.gmaf.data.model.GmafQuery
import de.max.troegel.gmaf.databinding.FragmentHomeViewPagerBinding
import de.max.troegel.gmaf.ui.adapter.FragmentAdapter
import de.max.troegel.gmaf.ui.adapter.RESULTS_PAGE_INDEX
import de.max.troegel.gmaf.ui.adapter.SEARCH_PAGE_INDEX
import de.max.troegel.gmaf.ui.adapter.SETTINGS_PAGE_INDEX

/**
 * Container Fragment that wraps the currently selected Fragment
 */
class HomeViewPagerFragment : Fragment() {

    /**
     * Binding to the layout
     */
    private lateinit var binding: FragmentHomeViewPagerBinding

    private var positionHistory = mutableListOf(SEARCH_PAGE_INDEX)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeViewPagerBinding.inflate(inflater, container, false)
        setupHomePagerAdapter()
        setupTabLayout()
        setupClickHandling()
        setupToolbar()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val count: Int = positionHistory.size
                    if (count == 1) {
                        requireActivity().finish()
                    } else {
                        val previousFragment = positionHistory[positionHistory.size - 2]
                        positionHistory.removeAt(positionHistory.size - 1)
                        binding.viewPager.currentItem = previousFragment
                        binding.viewPager.adapter?.notifyItemChanged(previousFragment)
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun setupHomePagerAdapter() {
        binding.viewPager.adapter = FragmentAdapter(this)
    }

    private fun setupTabLayout() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()
    }

    private fun setupClickHandling() {
        val viewPager = binding.viewPager
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (positionHistory.contains(position)) {
                    positionHistory.remove(position)
                }
                positionHistory.add(position)
                super.onPageSelected(position)
            }
        })
    }

    /**
     * Configure the Toolbar
     */
    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    /**
     * Get TabLayout icons.
     */
    private fun getTabIcon(position: Int): Int {
        return when (position) {
            SEARCH_PAGE_INDEX -> R.drawable.tab_selector_search
            RESULTS_PAGE_INDEX -> R.drawable.tab_selector_results
            SETTINGS_PAGE_INDEX -> R.drawable.tab_selector_settings
            else -> throw IndexOutOfBoundsException()
        }
    }

    /**
     * Get TabLayout titles.
     */
    private fun getTabTitle(position: Int): String {
        return when (position) {
            SEARCH_PAGE_INDEX -> getString(R.string.fragemt_search)
            RESULTS_PAGE_INDEX -> getString(R.string.fragemt_results)
            SETTINGS_PAGE_INDEX -> getString(R.string.fragemt_settings)
            else -> throw IndexOutOfBoundsException()
        }
    }

    fun switchToResultFragment() {
        if (binding.viewPager.currentItem != RESULTS_PAGE_INDEX) {
            binding.viewPager.currentItem = RESULTS_PAGE_INDEX
            binding.viewPager.post { binding.viewPager.adapter?.notifyItemChanged(RESULTS_PAGE_INDEX) }
        }
    }
}
