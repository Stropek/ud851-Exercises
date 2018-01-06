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
package com.example.android.background.sync

import android.os.AsyncTask
import com.firebase.jobdispatcher.JobParameters
import com.firebase.jobdispatcher.JobService
import java.util.*

class WaterReminderFirebaseJobService: JobService() {
    private var mBackgroundTask: AsyncTask<*, *, *>? = null

    override fun onStartJob(job: JobParameters): Boolean {
        mBackgroundTask = object: AsyncTask<Object, Object, Object>() {
            override fun doInBackground(params: Array<Object>): Object? {
                ReminderTasks.executeTask(this@WaterReminderFirebaseJobService, ReminderTasks.ACTION_CHARGING_REMINDER)
                return null
            }

            override fun onPostExecute(result: Object?) {
                jobFinished(job, false)
            }
        }
        mBackgroundTask?.execute()
        return true
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        if (mBackgroundTask != null) {
            mBackgroundTask?.cancel(true)
        }

        return true
    }
}
