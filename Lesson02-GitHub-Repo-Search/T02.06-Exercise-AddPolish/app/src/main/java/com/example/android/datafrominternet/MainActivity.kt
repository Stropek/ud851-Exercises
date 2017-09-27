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

import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView

import com.example.android.datafrominternet.utilities.NetworkUtils
import org.w3c.dom.Text

import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var mSearchBoxEditText: EditText? = null

    private var mUrlDisplayTextView: TextView? = null

    private var mSearchResultsTextView: TextView? = null

    private var mErrorMessageTextView: TextView? = null

    private var mProgressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSearchBoxEditText = findViewById(R.id.et_search_box) as EditText

        mUrlDisplayTextView = findViewById(R.id.tv_url_display) as TextView
        mSearchResultsTextView = findViewById(R.id.tv_github_search_results_json) as TextView

        mErrorMessageTextView = findViewById(R.id.tv_error_message_display) as TextView

        mProgressBar = findViewById(R.id.pb_loading_indicator) as ProgressBar
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using [NetworkUtils]) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using
     * our [GithubQueryTask]
     */
    private fun makeGithubSearchQuery() {
        val githubQuery = mSearchBoxEditText!!.text.toString()
        val githubSearchUrl = NetworkUtils.buildUrl(githubQuery)
        mUrlDisplayTextView!!.text = githubSearchUrl!!.toString()
        GithubQueryTask().execute(githubSearchUrl)
    }

    private fun showJsonDataView() {
        mSearchResultsTextView?.visibility = View.VISIBLE
        mErrorMessageTextView?.visibility = View.INVISIBLE
    }

    private fun showErrorMessage() {
        mSearchResultsTextView?.visibility = View.INVISIBLE
        mErrorMessageTextView?.visibility = View.VISIBLE
    }

    inner class GithubQueryTask : AsyncTask<URL, Void, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            mProgressBar?.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: URL): String? {
            val searchUrl = params[0]
            var githubSearchResults: String? = null
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return githubSearchResults
        }

        override fun onPostExecute(githubSearchResults: String?) {
            mProgressBar?.visibility = View.INVISIBLE
            if (githubSearchResults != null && githubSearchResults != "") {
                showJsonDataView()
                mSearchResultsTextView!!.text = githubSearchResults
            } else {
                showErrorMessage()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemThatWasClickedId = item.itemId
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
