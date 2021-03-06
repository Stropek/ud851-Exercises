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
package com.example.android.asynctaskloader

//import android.app.LoaderManager
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView

import com.example.android.asynctaskloader.utilities.NetworkUtils

import java.io.IOException
import java.net.URL

class MainActivity : LoaderManager.LoaderCallbacks<String>, AppCompatActivity() {
    override fun onLoaderReset(loader: Loader<String>?) {
    }

    override fun onLoadFinished(loader: Loader<String>?, data: String?) {
        mLoadingIndicator?.visibility = View.INVISIBLE

        if (data != null && data != "") {
            showJsonDataView()
            mSearchResultsTextView!!.text = data
        } else {
            showErrorMessage()
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> =
            WeatherAsyncTaskLoader(args, mLoadingIndicator, this@MainActivity)

    class WeatherAsyncTaskLoader(val args: Bundle?, val mLoadingIndicator: ProgressBar?, context: Context)
        : AsyncTaskLoader<String>(context) {

        override fun loadInBackground(): String? {
            val locationQuery = args?.getString(MainActivity.SEARCH_QUERY_URL_EXTRA)
            if (locationQuery.isNullOrBlank()) {
                return null
            }

            return try {
                val weatherRequestUrl = NetworkUtils.buildUrl(locationQuery)
                NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        override fun onStartLoading() {
            super.onStartLoading()
            if (args == null)
                return

            mLoadingIndicator?.visibility = View.VISIBLE
            forceLoad()
        }
    }

    private var mSearchBoxEditText: EditText? = null

    private var mUrlDisplayTextView: TextView? = null
    private var mSearchResultsTextView: TextView? = null

    private var mErrorMessageDisplay: TextView? = null

    private var mLoadingIndicator: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSearchBoxEditText = findViewById(R.id.et_search_box) as EditText

        mUrlDisplayTextView = findViewById(R.id.tv_url_display) as TextView
        mSearchResultsTextView = findViewById(R.id.tv_github_search_results_json) as TextView

        mErrorMessageDisplay = findViewById(R.id.tv_error_message_display) as TextView

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator) as ProgressBar

        if (savedInstanceState != null) {
            val queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA)
            mUrlDisplayTextView?.text = queryUrl;
        }

        supportLoaderManager.initLoader(GITHUB_SEARCH_LOADER, null, this@MainActivity)
    }

    /**
     * This method retrieves the search text from the EditText, constructs the
     * URL (using [NetworkUtils]) for the github repository you'd like to find, displays
     * that URL in a TextView, and finally request that an AsyncTaskLoader performs the GET request.
     */
    private fun makeGithubSearchQuery() {
        val githubQuery = mSearchBoxEditText!!.text.toString()

        if (TextUtils.isEmpty(githubQuery)) {
            mUrlDisplayTextView?.text = "No query entered, nothing to search for."
            return
        }

        val githubSearchUrl = NetworkUtils.buildUrl(githubQuery)
        mUrlDisplayTextView!!.text = githubSearchUrl!!.toString()

        val queryBundle = Bundle()
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, githubSearchUrl.toString())

        val githubSearchLoader = supportLoaderManager.getLoader<Loader<String>>(GITHUB_SEARCH_LOADER)
        if (githubSearchLoader == null) {
            supportLoaderManager.initLoader(GITHUB_SEARCH_LOADER, queryBundle, this@MainActivity)
        } else {
            supportLoaderManager.restartLoader(GITHUB_SEARCH_LOADER, queryBundle, this@MainActivity)
        }
    }

    /**
     * This method will make the View for the JSON data visible and
     * hide the error message.
     *
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private fun showJsonDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay!!.visibility = View.INVISIBLE
        /* Then, make sure the JSON data is visible */
        mSearchResultsTextView!!.visibility = View.VISIBLE
    }

    /**
     * This method will make the error message visible and hide the JSON
     * View.
     *
     *
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private fun showErrorMessage() {
        /* First, hide the currently visible data */
        mSearchResultsTextView!!.visibility = View.INVISIBLE
        /* Then, show the error */
        mErrorMessageDisplay!!.visibility = View.VISIBLE
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val queryUrl = mUrlDisplayTextView!!.text.toString()
        outState.putString(SEARCH_QUERY_URL_EXTRA, queryUrl)
    }

    companion object {
        /* A constant to save and restore the URL that is being displayed */
        private val SEARCH_QUERY_URL_EXTRA = "query"
        private val GITHUB_SEARCH_LOADER = 22
    }
}