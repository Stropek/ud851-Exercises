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

package com.example.android.todolist

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.View
import com.example.android.todolist.data.TaskContract


class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    // Member variables for the adapter and RecyclerView
    private var mAdapter: CustomCursorAdapter? = null
    internal lateinit var mRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the RecyclerView to its corresponding view
        mRecyclerView = findViewById(R.id.recyclerViewTasks) as RecyclerView

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        mRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = CustomCursorAdapter(this)
        mRecyclerView.adapter = mAdapter

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            // Called when a user swipes left or right on a ViewHolder
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                // Here is where you'll implement swipe to delete
            }
        }).attachToRecyclerView(mRecyclerView)

        /*
         Set the Floating Action Button (FAB) to its corresponding View.
         Attach an OnClickListener to it, so that when it's clicked, a new intent will be created
         to launch the AddTaskActivity.
         */
        val fabButton = findViewById(R.id.fab) as FloatingActionButton

        fabButton.setOnClickListener {
            // Create a new intent to start an AddTaskActivity
            val addTaskIntent = Intent(this@MainActivity, AddTaskActivity::class.java)
            startActivity(addTaskIntent)
        }

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        supportLoaderManager.initLoader(TASK_LOADER_ID, null, this)
    }


    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted through an AddTaskActivity,
     * so this restarts the loader to re-query the underlying data for any changes.
     */
    override fun onResume() {
        super.onResume()

        // re-queries for all tasks
        supportLoaderManager.restartLoader(TASK_LOADER_ID, null, this)
    }


    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return task data as a Cursor or null if an error occurs.
     *
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    override fun onCreateLoader(id: Int, loaderArgs: Bundle?): Loader<Cursor> {

        return object : AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            internal var mTaskData: Cursor? = null

            // onStartLoading() is called when a loader first starts loading data
            override fun onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData)
                } else {
                    // Force a new load
                    forceLoad()
                }
            }

            // loadInBackground() performs asynchronous loading of data
            override fun loadInBackground(): Cursor? {
                // Will implement to load data

                try {
                    return contentResolver.query(TaskContract.TaskEntry.CONTENT_URI,
                            null, null, null, TaskContract.TaskEntry.COLUMN_PRIORITY)
                } catch (ex: Exception) {
                    Log.d(TAG, "An exception has occurred: ${ex.message}", ex)
                    ex.printStackTrace()
                    return null
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            override fun deliverResult(data: Cursor?) {
                mTaskData = data
                super.deliverResult(data)
            }
        }

    }


    /**
     * Called when a previously created loader has finished its load.
     *
     * @param loader The Loader that has finished.
     * @param data The data generated by the Loader.
     */
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter!!.swapCursor(data)
    }


    /**
     * Called when a previously created loader is being reset, and thus
     * making its data unavailable.
     * onLoaderReset removes any references this activity had to the loader's data.
     *
     * @param loader The Loader that is being reset.
     */
    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter!!.swapCursor(null)
    }

    companion object {


        // Constants for logging and referring to a unique loader
        private val TAG = MainActivity::class.java.simpleName
        private val TASK_LOADER_ID = 0
    }

}

