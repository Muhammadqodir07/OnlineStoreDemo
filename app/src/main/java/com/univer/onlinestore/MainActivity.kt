package com.univer.onlinestore

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.univer.onlinestore.data.model.LoggedInUser
import com.univer.onlinestore.data.model.ProductFilter
import com.univer.onlinestore.ui.product.list.ProductListViewModel
import com.univer.onlinestore.ui.product.list.filter.FilterDialogFragment
import com.univer.onlinestore.ui.product.list.filter.FilterDialogListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FilterDialogListener {

    private lateinit var displayName: String
    private var isMenuVisible = true
    val productListViewModel: ProductListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayName = intent.getStringExtra("DISPLAY_NAME").toString()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_products -> {
                    if (!isMenuVisible) {
                        navController.popBackStack()
                        isMenuVisible = true
                    }
                }

                R.id.navigation_cart -> {
                    if (isMenuVisible) {
                        navController.navigate(R.id.action_productListFragment_to_CartFragment)
                        isMenuVisible = false
                    }
                }
            }
            invalidateOptionsMenu()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        menu?.setGroupVisible(0, isMenuVisible)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_show_profile -> {
                showProfileDialog()
                return true
            }

            R.id.action_filter -> {
                showFilterDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showProfileDialog() {
        val dialogFragment = ProfileDialog(LoggedInUser(userId = "", displayName = displayName))
        dialogFragment.show(supportFragmentManager, "customDialog")
    }

    private fun showFilterDialog() {
        val filterDialog = FilterDialogFragment.newInstance()
        filterDialog.show(supportFragmentManager, "filterDialog")
    }

    override fun onFiltersApplied(filter: ProductFilter) {
        productListViewModel.applyFilter(filter)
    }
}