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

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri

import com.example.android.todolist.data.TaskContract.TaskEntry.TABLE_NAME

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


    // Implement insert to handle requests to insert a single new row of data
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // Get access to the task database (to write new data to)
        val db = mTaskDbHelper!!.writableDatabase

        // Write URI matching code to identify the match for the tasks directory
        val match = sUriMatcher.match(uri)
        val returnUri: Uri // URI to be returned

        when (match) {
            TASKS -> {
                // Insert new values into the database
                // Inserting values into tasks table
                val id = db.insert(TABLE_NAME, null, values)
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TaskContract.TaskEntry.CONTENT_URI, id)
                } else {
                    throw android.database.SQLException("Failed to insert row into " + uri)
                }
            }
        // Set the value for the returnedUri and write the default case for unknown URI's
        // Default case throws an UnsupportedOperationException
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        context!!.contentResolver.notifyChange(uri, null)

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri
    }


    // Implement query to handle requests for data by URI
    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        val readDb = mTaskDbHelper?.readableDatabase
        var cursor: Cursor? = null

        when(sUriMatcher.match(uri)) {
            TASKS -> {
                cursor = readDb?.query(
                        TaskContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
            }
            else -> throw UnsupportedOperationException("Unknown uri: $uri")
        }

        cursor?.setNotificationUri(context.contentResolver, uri)

        return cursor
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
