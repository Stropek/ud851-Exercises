package com.example.android.explicitintent

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ChildActivity : AppCompatActivity() {
    private var mDisplayText : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child)

        mDisplayText = findViewById(R.id.tv_display) as TextView
    }
}
