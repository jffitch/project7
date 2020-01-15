package com.mathgeniusguide.project7

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        // hide back arrow for these tabs
        appBarConfiguration = AppBarConfiguration(setOf(R.id.topNews, R.id.mostPopular, R.id.politicsNews))

        // set up tabs with Nav Controller
        tabs.setupWithNavController(navController)
        // set up action bar with Nav Controller
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // add menu to action bar
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return item.onNavDestinationSelected(navController)
                || super.onOptionsItemSelected(item)
    }

    // for fragments without action bar
    fun hideActionBar() {
        supportActionBar?.hide()
    }

    // for fragments with action bar
    fun showActionBar() {
        supportActionBar?.show()
    }

    // for fragments without tabs on bottom
    fun hideBottomNavigationView() {
        tabs.visibility = View.GONE
    }

    // for fragments with tabs on bottom
    fun showBottomNavigationView() {
        tabs.visibility = View.VISIBLE
    }
}