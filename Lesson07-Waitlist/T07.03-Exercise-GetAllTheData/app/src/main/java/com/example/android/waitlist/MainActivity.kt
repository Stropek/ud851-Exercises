package com.example.android.waitlist

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.android.waitlist.data.TestUtil
import com.example.android.waitlist.data.WaitlistContract
import com.example.android.waitlist.data.WaitlistDbHelper


class MainActivity : AppCompatActivity() {

    private var mAdapter: GuestListAdapter? = null

    lateinit var mDb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val waitlistRecyclerView: RecyclerView

        // Set local attributes to corresponding views
        waitlistRecyclerView = this.findViewById(R.id.all_guests_list_view) as RecyclerView

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.layoutManager = LinearLayoutManager(this)

        val waitlistDbHelper = WaitlistDbHelper(this)
        mDb = waitlistDbHelper.writableDatabase

        TestUtil.insertFakeData(mDb)

        val cursor = getAllGuests()

        // Create an adapter for that cursor to display the data
        mAdapter = GuestListAdapter(this, cursor.count)

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.adapter = mAdapter
    }

    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    fun addToWaitlist(view: View) {

    }

    fun getAllGuests(): Cursor {
        return mDb.query(WaitlistContract.WaitlistEntry.TABLE_NAME,
                null, null, null, null, null,
                "order by ${WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP}")
    }
}