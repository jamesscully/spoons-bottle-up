package com.scullyapps.spoonsbottleup.adapters

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.fragments.BottleListFragment
import com.scullyapps.spoonsbottleup.fragments.FridgeFragment
import com.scullyapps.spoonsbottleup.fragments.GeneralSettingsFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        when (position) {
            0 -> return GeneralSettingsFragment()
            1 -> return BottleListFragment()
            2 -> return FridgeFragment()
        }
        return GeneralSettingsFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3)
    }
}