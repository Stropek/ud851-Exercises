package com.example.android.waitlist

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.android.waitlist.data.WaitlistContract

/**
 * Constructor using the context and the db cursor
 * @param context the calling context/activity
 */
class GuestListAdapter (private val mContext: Context, private val cursor: Cursor): RecyclerView.Adapter<GuestListAdapter.GuestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        // Get the RecyclerView item layout
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.guest_list_item, parent, false)
        return GuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        if (!cursor.moveToPosition(position))
            return

        val guestName = cursor.getString(cursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME))
        val partySize = cursor.getInt(cursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE))

        holder.nameTextView.text = guestName
        holder.partySizeTextView.text = partySize.toString()
    }

    override fun getItemCount(): Int {
        return cursor.count
    }


    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    inner class GuestViewHolder
    /**
     * Constructor for our ViewHolder. Within this constructor, we get a reference to our
     * TextViews
     *
     * @param itemView The View that you inflated in
     * [GuestListAdapter.onCreateViewHolder]
     */
    (itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Will display the guest name
        var nameTextView: TextView
        // Will display the party size number
        var partySizeTextView: TextView

        init {
            nameTextView = itemView.findViewById(R.id.name_text_view) as TextView
            partySizeTextView = itemView.findViewById(R.id.party_size_text_view) as TextView
        }

    }
}