package com.example.spacebook.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.spacebook.R


class FeedFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    // TODO: create feed

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        view.findViewById<Toolbar>(R.id.toolbar).setOnMenuItemClickListener(this)
        return view
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logout -> {
                // TODO: log out
            }
            else -> return false
        }
        return true
    }
}
