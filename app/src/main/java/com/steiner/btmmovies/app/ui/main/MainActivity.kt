package com.steiner.btmmovies.app.ui.main

import android.content.Intent
import android.os.Bundle
import com.steiner.btmmovies.app.BtmMoviesApp
import com.steiner.btmmovies.app.databinding.ActivityMainBinding
import com.steiner.btmmovies.app.di.account.AccountSubComponent
import com.steiner.btmmovies.app.ui.entrance.EntranceActivity
import com.steiner.btmmovies.core.ui.BaseActivity

/**
 *
 */
class MainActivity : BaseActivity() {

    lateinit var accountSubComponent: AccountSubComponent

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val accountManager = (applicationContext as BtmMoviesApp).appComponent
            .accountManager()

        if (!accountManager.isRegistered()) {
            super.onCreate(savedInstanceState)

            startActivity(Intent(this, EntranceActivity::class.java))
            finish()
        } else {
            if (!accountManager.isLoggedIn()) {
                accountManager.login()
            }

            accountSubComponent = (applicationContext as BtmMoviesApp).appComponent
                .accountManager().accountComponent!!
            accountSubComponent.inject(this)

            super.onCreate(savedInstanceState)

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
        }
    }
}
