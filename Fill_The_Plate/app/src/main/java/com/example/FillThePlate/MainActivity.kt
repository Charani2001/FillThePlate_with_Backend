package com.example.FillThePlate

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.FillThePlate.databinding.ActivityMainBinding
import com.example.myapplication.FoodDetailsFragment
import com.example.myapplication.GetFoodFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)




        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.nav_open, R.string.nav_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigaionDrawer.setNavigationItemSelectedListener(this)

        // Handling Bottom Navigation
        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_food_details -> openFragment(FoodDetailsFragment())
                R.id.nav_donate -> openFragment(DonateFoods())
                R.id.bottom_home -> openFragment(HomeFragment())
                R.id.nav_get_foods -> openFragment(GetFoodFragment())
                R.id.nav_request_status -> openFragment(RequestStatus())

            }
            true
        }

        // Open default HomeFragment when the activity starts
        openFragment(HomeFragment())


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {


            R.id.bottom_profile -> openFragment(ProfileFragment())
            R.id.bottom_about -> openFragment(About())
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Handle back press for drawer
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()  // Only call super if the drawer is not open
        }
    }

    // Open or replace the current fragment
    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
