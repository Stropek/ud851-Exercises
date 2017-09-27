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
package com.example.android.datafrominternet

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView

import com.example.android.datafrominternet.utilities.NetworkUtils

import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var mSearchBoxEditText: EditText? = null

    private var mUrlDisplayTextView: TextView? = null

    private var mSearchResultsTextView: TextView? = null

    @Override
    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSearchBoxEditText = findViewById(R.id.et_search_box) as EditText

        mUrlDisplayTextView = findViewById(R.id.tv_url_display) as TextView
        mSearchResultsTextView = findViewById(R.id.tv_github_search_results_json) as TextView
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using [NetworkUtils]) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
     * our [GithubQueryTask]
     */
    private fun makeGithubSearchQuery() {
        val githubQuery = mSearchBoxEditText!!.getText().toString()
        val githubSearchUrl = NetworkUtils.buildUrl(githubQuery)
        mUrlDisplayTextView!!.setText(githubSearchUrl.toString())
        // COMPLETED (4) Create a new GithubQueryTask and call its execute method, passing in the url to query
        GithubQueryTask().execute(githubSearchUrl)
    }

    // COMPLETED (1) Create a class called GithubQueryTask that extends AsyncTask<URL, Void, String>
    inner class GithubQueryTask : AsyncTask<URL, Void, String>() {

        // COMPLETED (2) Override the doInBackground method to perform the query. Return the results. (Hint: You've already written the code to perform the query)
        @Override
        protected fun doInBackground(vararg params: URL): String? {
            val searchUrl = params[0]
            var githubSearchResults: String? = null
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return githubSearchResults
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected fun onPostExecute(githubSearchResults: String?) {
            if (githubSearchResults != null && !githubSearchResults.equals("")) {
                mSearchResultsTextView!!.setText(githubSearchResults)
            }
        }
    }

    @Override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.main, menu)
        return true
    }

    @Override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemThatWasClickedId = item.getItemId()
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
