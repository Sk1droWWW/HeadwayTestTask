package com.example.headwayTestTask.ui;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.headwayTestTask.databinding.LastVisitedFragmentBinding


class LastVisitedFragment : Fragment() {

    private lateinit var binding: LastVisitedFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = LastVisitedFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
