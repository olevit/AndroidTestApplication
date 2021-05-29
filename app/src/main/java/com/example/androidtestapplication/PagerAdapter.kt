package com.example.androidtestapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(fa: FragmentActivity, val listFragment:ArrayList<Int>) :
        FragmentStateAdapter(fa){

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return SlideFragment(listFragment[position])
    }
}
