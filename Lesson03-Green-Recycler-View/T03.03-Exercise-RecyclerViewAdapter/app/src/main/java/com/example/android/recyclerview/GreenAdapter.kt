/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * We couldn't come up with a good name for this class. Then, we realized
 * that this lesson is about RecyclerView.
 *
 * RecyclerView... Recycling... Saving the planet? Being green? Anyone?
 * #crickets
 *
 * Avoid unnecessary garbage collection by using RecyclerView and ViewHolders.
 *
 * If you don't like our puns, we named this Adapter GreenAdapter because its
 * contents are green.
 */

internal class GreenAdapter() : RecyclerView.Adapter<GreenAdapter.NumberViewHolder>() {

    private var mNumberItems : Int = 0

    constructor(numberOfItems: Int) : this() {
        mNumberItems = numberOfItems
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NumberViewHolder {
        val inflater : LayoutInflater = LayoutInflater.from(parent?.context)
        val itemView = inflater.inflate(R.layout.number_list_item, parent, false)

        return NumberViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NumberViewHolder?, position: Int) {
        holder?.bind(position)
    }

    override fun getItemCount(): Int {
        return mNumberItems
    }

    /**
     * Cache of the children views for a list item.
     */
    internal inner class NumberViewHolder
    /**
     * Constructor for our ViewHolder. Within this constructor, we get a reference to our
     * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
     * onClick method below.
     * @param itemView The View that you inflated in
     * [GreenAdapter.onCreateViewHolder]
     */
    (itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        var listItemNumberView: TextView

        init {

            listItemNumberView = itemView.findViewById(R.id.tv_item_number) as TextView
        }

        /**
         * A method we wrote for convenience. This method will take an integer as input and
         * use that integer to display the appropriate text within a list item.
         * @param listIndex Position of the item in the list
         */
        fun bind(listIndex: Int) {
            listItemNumberView.text = "Position: ${listIndex}"
        }
    }
}
