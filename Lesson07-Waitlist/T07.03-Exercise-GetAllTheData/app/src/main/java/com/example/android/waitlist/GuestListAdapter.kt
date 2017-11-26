package com.example.android.waitlist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Constructor using the context and the db cursor
 *
 * @param context the calling context/activity
 */
class GuestListAdapter(private val mContext: Context, private var mCount: Int): RecyclerView.Adapter<GuestListAdapter.GuestViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        // Get the RecyclerView item layout
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.guest_list_item, parent, false)
        return GuestViewHolder(view)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return mCount
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