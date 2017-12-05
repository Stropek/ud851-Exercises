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

package com.udacity.example.quizexample

import android.content.ContentResolver
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView

import com.udacity.example.droidtermsprovider.DroidTermsExampleContract

/**
 * Gets the data from the ContentProvider and shows a series of flash cards.
 */

class MainActivity : AppCompatActivity() {

    // The data from the DroidTermsExample content provider
    private var mData: Cursor? = null

    // The current state of the app
    private var mCurrentState: Int = 0

    private var mButton: Button? = null
    private var mWord: TextView? = null
    private var mDefinition: TextView? = null

    // This state is when the word definition is hidden and clicking the button will therefore
    // show the definition
    private val STATE_HIDDEN = 0

    // This state is when the word definition is shown and clicking the button will therefore
    // advance the app to the next word
    private val STATE_SHOWN = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mButton = findViewById(R.id.button_next) as Button

        mWord = findViewById(R.id.text_view_word) as TextView
        mDefinition = findViewById(R.id.text_view_definition) as TextView

        //Run the database operation to get the cursor off of the main thread
        WordFetchTask().execute()

    }

    /**
     * This is called from the layout when the button is clicked and switches between the
     * two app states.
     * @param view The view that was clicked
     */
    fun onButtonClick(view: View) {

        // Either show the definition of the current word, or if the definition is currently
        // showing, move to the next word.
        when (mCurrentState) {
            STATE_HIDDEN -> showDefinition()
            STATE_SHOWN -> nextWord()
        }
    }

    fun nextWord() {

        // Change button text
        mButton!!.text = getString(R.string.show_definition)

        if (mData == null)
            return

        if (mData?.moveToNext() == true || mData?.moveToFirst() == true) {
            val wordColIndex = mData?.getColumnIndex(DroidTermsExampleContract.COLUMN_WORD)

            if (wordColIndex != null)
            {
                mWord?.text = mData?.getString(wordColIndex)
                mDefinition?.text = ""
            }
        }

        mCurrentState = STATE_HIDDEN

    }

    fun showDefinition() {

        // Change button text
        mButton!!.text = getString(R.string.next_word)

        val definitionColIndex = mData?.getColumnIndex(DroidTermsExampleContract.COLUMN_DEFINITION)

        if (definitionColIndex != null)
        {
            mDefinition?.text = mData?.getString(definitionColIndex)
            mWord?.text = ""
        }

        mCurrentState = STATE_SHOWN

    }

    override fun onDestroy() {
        super.onDestroy()

        mData?.close()
    }

    // Use an async task to do the data fetch off of the main thread.
    inner class WordFetchTask : AsyncTask<Void, Void, Cursor>() {

        // Invoked on a background thread
        override fun doInBackground(vararg params: Void?): Cursor? {
            // Make the query to get the data

            // Get the content resolver
            val resolver = contentResolver

            // Call the query method on the resolver with the correct Uri from the contract class
            return resolver.query(DroidTermsExampleContract.CONTENT_URI, null, null, null, null)
        }


        // Invoked on UI thread
        override fun onPostExecute(cursor: Cursor?) {
            super.onPostExecute(cursor)

            if (cursor == null)
                return

            val wordColIndex = cursor.getColumnIndex(DroidTermsExampleContract.COLUMN_WORD)
            val definitionColIndex = cursor.getColumnIndex(DroidTermsExampleContract.COLUMN_DEFINITION)

            if (cursor.moveToNext()) {
                val word = cursor.getString(wordColIndex)
                val definition = cursor.getString(definitionColIndex)

                Log.v("Cursor Example", "$word - $definition")

                mWord?.text = word
                mDefinition?.text = definition
            }

            // Set the data for MainActivity
            mData = cursor

        }
    }

}
