package com.scullyapps.spoonsbottleup.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.fragments.FridgeFragment
import com.scullyapps.spoonsbottleup.fragments.GeneralSettingsFragment

class SettingsPagerAdapter(private val mContext: Context, fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {

    var currentFragmentKey : Int = 0
    private var fragmentRefMap = HashMap<Int, Fragment>()

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        var mFragment : Fragment = GeneralSettingsFragment()

        when (position) {
            KEY_GENERAL_FRAGMENT -> mFragment = GeneralSettingsFragment()
            KEY_FRIDGE_FRAGMENT  -> mFragment = FridgeFragment()
        }

        // update our current key
        currentFragmentKey = position

        // add to our references, so we can retrieve later
        fragmentRefMap[currentFragmentKey] = mFragment

        return mFragment
    }

    fun hasFragment(key : Int) : Boolean {
        return fragmentRefMap.containsKey(key)
    }

    fun getFragment(key : Int) : Fragment? {
        return fragmentRefMap[key]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)

        // destroy reference to fragment if not in use
        fragmentRefMap.remove(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_text_1, R.string.tab_text_3)

        const val KEY_GENERAL_FRAGMENT = 0
        const val KEY_FRIDGE_FRAGMENT = 1
    }
}