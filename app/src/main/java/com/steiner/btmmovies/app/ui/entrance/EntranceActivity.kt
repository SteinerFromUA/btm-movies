package com.steiner.btmmovies.app.ui.entrance

import android.content.Intent
import android.os.Bundle
import com.steiner.btmmovies.app.BtmMoviesApp
import com.steiner.btmmovies.app.databinding.ActivityEntranceBinding
import com.steiner.btmmovies.app.di.guest.GuestSubComponent
import com.steiner.btmmovies.app.ui.main.MainActivity
import com.steiner.btmmovies.core.ui.BaseActivity

/**
 *
 */
class EntranceActivity : BaseActivity() {

    lateinit var guestSubComponent: GuestSubComponent

    private lateinit var binding: ActivityEntranceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val accountManager = (applicationContext as BtmMoviesApp).appComponent
            .accountManager()

        if (accountManager.isRegistered()) {
            if (!accountManager.isLoggedIn()) {
                accountManager.login()
            }
            super.onCreate(savedInstanceState)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            guestSubComponent = (applicationContext as BtmMoviesApp).appComponent
                .guestSubComponentFactory().create()
            guestSubComponent.inject(this)

            super.onCreate(savedInstanceState)

            binding = ActivityEntranceBinding.inflate(layoutInflater)
            setContentView(binding.root)
        }
    }
}
