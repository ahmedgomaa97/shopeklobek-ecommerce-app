package com.stash.shopeklobek.ui.authentication.completeAction



import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.stash.shopeklobek.R
import com.stash.shopeklobek.databinding.FragmentCompleteActionBinding

import com.stash.shopeklobek.ui.BaseFragment

class CompleteActionFragment : BaseFragment<FragmentCompleteActionBinding>(
    FragmentCompleteActionBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loogin_to_nav_register)
        }
        binding.btnalogin.setOnClickListener {
            findNavController().navigate(R.id.action_loogin_to_nav_login)
        }
    }
}