package com.example.androidtestapplication

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.androidtestapplication.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    lateinit var binding : FragmentFirstBinding
    private lateinit var prefs: SharedPreferences
    private var page = 1
    private val APP_PREFERENCES_PAGE = "page"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFirstBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = activity?.getSharedPreferences("pageCounter", Context.MODE_PRIVATE)!!
        val fragmentArray = ArrayList<Int>()
        if(prefs.contains(APP_PREFERENCES_PAGE)){
            savedState(fragmentArray)
        }else{
            binding.minus.visibility = View.GONE
            fragmentArray.add(page)
            binding.viewPager2.adapter = PagerAdapter(requireActivity(), fragmentArray)
        }

        binding.plus.setOnClickListener {
            addPage(fragmentArray)
        }

        binding.minus.setOnClickListener {
            deletePage(fragmentArray)
        }
    }

    private fun savedState(arrayList: ArrayList<Int>){
        page = prefs.getInt(APP_PREFERENCES_PAGE, 0)
        for(item in 1..page){
            arrayList.add(item)
        }
        if(arrayList.size == 1){
            binding.minus.visibility = View.GONE
        }
        binding.viewPager2.adapter = PagerAdapter(requireActivity(), arrayList)
        if(arguments != null){
            val position = requireArguments().getInt("position")
            binding.viewPager2.post {
                binding.viewPager2.setCurrentItem(position - 1, true)
            }
        }else {
            binding.viewPager2.post {
                binding.viewPager2.setCurrentItem(page - 1, true)
            }
        }
    }

    private fun addPage(arrayList: ArrayList<Int>){
        binding.minus.visibility = View.VISIBLE
        page++
        arrayList.add(page)
        binding.viewPager2.adapter = PagerAdapter(requireActivity(), arrayList)
        binding.viewPager2.post {
            binding.viewPager2.setCurrentItem(page - 1, true)
        }
    }

    private fun deletePage(arrayList: ArrayList<Int>){
        if (arrayList.size > 1) {
            page--
            val notificationManager: NotificationManager = activity?.getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(arrayList[arrayList.size - 1])
            arrayList.remove(arrayList[arrayList.size - 1])
            binding.viewPager2.adapter = PagerAdapter(requireActivity(), arrayList)
            binding.viewPager2.post {
                binding.viewPager2.setCurrentItem(page - 1, true)
            }
        }
        if (arrayList.size == 1){
            binding.minus.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        val editor = prefs.edit()
        editor.putInt(APP_PREFERENCES_PAGE, page).apply()
    }
}
