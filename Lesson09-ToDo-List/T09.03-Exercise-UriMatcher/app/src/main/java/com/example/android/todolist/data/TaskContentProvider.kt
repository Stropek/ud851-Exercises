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
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

// Verify that TaskContentProvider extends from ContentProvider and implements required methods
class TaskContentProvider : ContentProvider() {

    companion object {
        val TASKS = 100
        val TASK_WITH_ID = 101

        var sUriMatcher = buildUriMatcher()

        fun buildUriMatcher(): UriMatcher {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

            uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_TASKS, TASKS)
            uriMatcher.addURI(TaskContract.AUTHORITY, "${TaskContract.PATH_TASKS}/#", TASK_WITH_ID)

            return uriMatcher
        }
    }

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

        throw UnsupportedOperationException("Not yet implemented")
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

}
