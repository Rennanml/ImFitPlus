package br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.R
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.adapter.LogAdapter
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.controller.UserController
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.databinding.ActivityLogBinding
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.LogItem
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.model.UserEntity
import br.edu.ifsp.scl.ads.prdm.sc3039005.imfitplus.persistence.LogRepository

class LogActivity : AppCompatActivity() {
    private val lbm: ActivityLogBinding by lazy {
        ActivityLogBinding.inflate(layoutInflater)
    }

    private lateinit var data: MutableList<UserEntity>;
    private val userController: UserController by lazy {
        UserController(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(lbm.root)

        setSupportActionBar(lbm.toolbarIn.toolbar)
        supportActionBar?.subtitle = "Histórico"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        data = userController.retrieveUsers()
        val adapter = LogAdapter(this, data)
        lbm.logLv.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.log_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_action_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}