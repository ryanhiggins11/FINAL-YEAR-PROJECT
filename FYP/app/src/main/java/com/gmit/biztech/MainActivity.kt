package com.gmit.biztech

import android.R
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout


class MainActivity : AppCompatActivity() {
    private lateinit var mNavigationDrawerItemTitles: Array<String>
    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerList: ListView? = null
    var toolbar: Toolbar? = null
    private var mDrawerTitle: CharSequence? = null
    private var mTitle: CharSequence? = null
    var mDrawerToggle: android.support.v7.app.ActionBarDrawerToggle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDrawerTitle = title
        mTitle = mDrawerTitle
        mNavigationDrawerItemTitles =
            resources.getStringArray(R.array.navigation_drawer_items_array)
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        mDrawerList = findViewById<View>(R.id.left_drawer) as ListView
        setupToolbar()
        val drawerItem = arrayOfNulls<DataModel>(3)
        drawerItem[0] = DataModel(R.drawable.connect, "Connect")
        drawerItem[1] = DataModel(R.drawable.fixtures, "Fixtures")
        drawerItem[2] = DataModel(R.drawable.table, "Table")
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(true)
        val adapter =
            DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem)
        mDrawerList!!.adapter = adapter
        mDrawerList!!.onItemClickListener = DrawerItemClickListener()
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        mDrawerLayout!!.setDrawerListener(mDrawerToggle)
        setupDrawerToggle()
    }

    private inner class DrawerItemClickListener : OnItemClickListener {
        override fun onItemClick(
            parent: AdapterView<*>?,
            view: View,
            position: Int,
            id: Long
        ) {
            selectItem(position)
        }
    }

    private fun selectItem(position: Int) {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ConnectFragment()
            1 -> fragment = FixturesFragment()
            2 -> fragment = TableFragment()
            else -> {
            }
        }
        if (fragment != null) {
            val fragmentManager: FragmentManager = supportFragmentManager
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit()
            mDrawerList!!.setItemChecked(position, true)
            mDrawerList!!.setSelection(position)
            setTitle(mNavigationDrawerItemTitles[position])
            mDrawerLayout!!.closeDrawer(mDrawerList!!)
        } else {
            Log.e("MainActivity", "Error in creating fragment")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (mDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun setTitle(title: CharSequence) {
        mTitle = title
        supportActionBar!!.title = mTitle
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle.syncState()
    }

    fun setupToolbar() {
        toolbar = findViewById<View>(R.id.) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    fun setupDrawerToggle() {
        mDrawerToggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            toolbar,
            R.string.app_name,
            R.string.app_name
        )
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState()
    }
}

