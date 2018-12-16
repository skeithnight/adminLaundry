package com.macbook.adminlaundry

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.macbook.adminlaundry.activities.LoginActivity
import com.macbook.adminlaundry.controller.Authorization
import com.macbook.adminlaundry.fragments.ListDataFragment
import com.macbook.adminlaundry.fragments.PesananFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var TAG = "Testing"
    private var tampilDialog: TampilDialog? = null
    private var authorization: Authorization? = null
    //    SharedPreferences
    var mSPLogin: SharedPreferences? = null


    override fun onStart() {
        super.onStart()
        authorization = Authorization(applicationContext)
        tampilDialog = TampilDialog(applicationContext)
        if (!authorization!!.CheckSession()) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_pesanan -> {
                println("pesanan pressed")
                replaceFragment(PesananFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                println("List Data pressed")
                replaceFragment(ListDataFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        replaceFragment(PesananFragment())
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.commit()
    }

}
