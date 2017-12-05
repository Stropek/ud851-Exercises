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
import android.view.View
import android.widget.Button
import com.udacity.example.droidtermsprovider.DroidTermsExampleContract

/**
 * Gets the data from the ContentProvider and shows a series of flash cards.
 */

class MainActivity : AppCompatActivity() {

    // The current state of the app
    private var mCurrentState: Int = 0

    private var mData: Cursor? = null
    private var mButton: Button? = null

    // This state is when the word definition is hidden and clicking the button will therefore
    // show the definition
    private val STATE_HIDDEN = 0

    // This state is when the word definition is shown and clicking the button will therefore
    // advance the app to the next word
    private val STATE_SHOWN = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the views
        mButton = findViewById(R.id.button_next) as Button

        CursorAsyncTask().execute()
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

        mCurrentState = STATE_HIDDEN

    }

    fun showDefinition() {

        // Change button text
        mButton!!.text = getString(R.string.next_word)

        mCurrentState = STATE_SHOWN

    }

    inner class CursorAsyncTask: AsyncTask<Void, Void, Cursor>() {
        override fun doInBackground(vararg args: Void): Cursor {
            var results = contentResolver.query(DroidTermsExampleContract.CONTENT_URI,
                    null, null, null, null)

            return results
        }

        override fun onPostExecute(result: Cursor?) {
            super.onPostExecute(result)
            mData = result
        }
    }
}
