package com.stash.shopeklobek.utils

import android.app.Activity
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationBarView
import java.lang.ref.WeakReference

object NavigationExtension {

    fun NavigationBarView.setupWithNavController2(
        navController: NavController
    ) {
        setOnItemSelectedListener { item ->
            item.isChecked = true

            NavigationUI.onNavDestinationSelected(
                item,
                navController
            )
        }
        val weakReference = WeakReference(this)

        navController.addOnDestinationChangedListener(
            object : NavController.OnDestinationChangedListener {
                override fun onDestinationChanged(
                    controller: NavController,
                    destination: NavDestination,
                    arguments: Bundle?
                ) {
                    val view = weakReference.get()
                    if (view == null) {
                        navController.removeOnDestinationChangedListener(this)
                        return
                    }
                }
            })
    }

    internal fun NavDestination.matchDestination(@IdRes destId: Int): Boolean =
        hierarchy.any { it.id == destId }


    fun FragmentContainerView.findNavController2(activity:FragmentActivity):NavController{
        val navHostFragment = activity.supportFragmentManager.findFragmentById(this.id) as NavHostFragment
        return navHostFragment.navController
    }


}