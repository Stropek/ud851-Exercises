package com.example.android.waitlist

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.android.waitlist.data.WaitlistContract


class GuestListAdapter
/**
 * Constructor using the context and the db cursor
 * @param context the calling context/activity
 * @param cursor the db cursor with waitlist data to display
 */
(private val mContext: Context, private var mCursor: Cursor?) : RecyclerView.Adapter<GuestListAdapter.GuestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        // Get the RecyclerView item layout
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.guest_list_item, parent, false)
        return GuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor!!.moveToPosition(position))
            return  // bail if returned null

        // Update the view holder with the information needed to display
        val name = mCursor!!.getString(mCursor!!.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME))
        val partySize = mCursor!!.getInt(mCursor!!.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE))
        val id = mCursor!!.getLong(mCursor!!.getColumnIndex(WaitlistContract.WaitlistEntry._ID))

        // Display the guest name
        holder.nameTextView.text = name
        // Display the party count
        holder.partySizeTextView.text = partySize.toString()
        holder.itemView.tag = id
    }


    override fun getItemCount(): Int {
        return mCursor!!.count
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    fun swapCursor(newCursor: Cursor?) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor!!.close()
        mCursor = newCursor
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged()
        }
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