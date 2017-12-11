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

package com.example.android.todolist.data

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.net.Uri

// Verify that TaskContentProvider extends from ContentProvider and implements required methods
class TaskContentProvider : ContentProvider() {

    // Member variable for a TaskDbHelper that's initialized in the onCreate() method
    private var mTaskDbHelper: TaskDbHelper? = null

    /* onCreate() is where you should initialize anything you’ll need to setup
    your underlying data source.
    In this case, you’re working with a SQLite database, so you’ll need to
    initialize a DbHelper to gain access to it.
     */
    override fun onCreate(): Boolean {
        // Complete onCreate() and initialize a TaskDbhelper on startup
        // [Hint] Declare the DbHelper as a global variable

        val context = context
        mTaskDbHelper = TaskDbHelper(context)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = mTaskDbHelper!!.writableDatabase

        val returnUri = when(sUriMatcher.match(uri)) {
            TASKS -> {
                val newId = db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values)
                if (newId > 0) {
                    ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, newId)
                } else {
                    throw SQLException("Failed to insert row into: $uri")
                }
            }
            else -> throw UnsupportedOperationException("Unknown URI: $uri")
        }

        context.contentResolver.notifyChange(uri, null)

        return returnUri
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {

        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {

        throw UnsupportedOperationException("Not yet implemented")
    }

    companion object {

        // Define final integer constants for the directory of tasks and a single item.
        // It's convention to use 100, 200, 300, etc for directories,
        // and related ints (101, 102, ..) for items in that directory.
        val TASKS = 100
        val TASK_WITH_ID = 101

        // CDeclare a static variable for the Uri matcher that you construct
        private val sUriMatcher = buildUriMatcher()

        // Define a static buildUriMatcher method that associates URI's with their int match
        /**
         * Initialize a new matcher object without any matches,
         * then use .addURI(String authority, String path, int match) to add matches
         */
        fun buildUriMatcher(): UriMatcher {

            // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
            uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS, TASKS)
            uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS + "/#", TASK_WITH_ID)

            return uriMatcher
        }
    }
}
