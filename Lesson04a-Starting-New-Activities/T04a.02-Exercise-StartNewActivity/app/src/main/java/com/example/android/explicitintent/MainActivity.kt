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
package com.example.android.explicitintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    /* Fields that will store our EditText and Button */
    private var mNameEntry: EditText? = null
    private var mDoSomethingCoolButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
         * Using findViewById, we get a reference to our Button from xml. This allows us to
         * do things like set the onClickListener which determines what happens when the button
         * is clicked.
         */
        mDoSomethingCoolButton = findViewById(R.id.b_do_something_cool) as Button
        mNameEntry = findViewById(R.id.et_text_entry) as EditText

        /* Setting an OnClickListener allows us to do something when this button is clicked. */
        mDoSomethingCoolButton!!.setOnClickListener(
        {
            val context = this@MainActivity

            val destinationActivity = ChildActivity::class.java

            val intent = Intent(context, destinationActivity)

            startActivity(intent)
        })
    }
}

