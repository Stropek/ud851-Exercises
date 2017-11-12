package com.example.android.lifecycle

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    /*
     * This TextView will contain a running log of every lifecycle callback method called from this
     * Activity. This TextView can be reset to its default state by clicking the Button labeled
     * "Reset Log"
     */
    private var mLifecycleDisplay: TextView? = null

    /**
     * Called when the activity is first created. This is where you should do all of your normal
     * static set up: create views, bind data to lists, etc.
     *
     * Always followed by onStart().
     *
     * @param savedInstanceState The Activity's previously frozen state, if there was one.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLifecycleDisplay = findViewById(R.id.tv_lifecycle_events_display) as TextView

        /*
         * If savedInstanceState is not null, that means our Activity is not being started for the
         * first time. Even if the savedInstanceState is not null, it is smart to check if the
         * bundle contains the key we are looking for. In our case, the key we are looking for maps
         * to the contents of the TextView that displays our list of callbacks. If the bundle
         * contains that key, we set the contents of the TextView accordingly.
         */
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(LIFECYCLE_CALLBACKS_TEXT_KEY)) {
                val allPreviousLifecycleCallbacks = savedInstanceState
                        .getString(LIFECYCLE_CALLBACKS_TEXT_KEY)
                mLifecycleDisplay!!.text = allPreviousLifecycleCallbacks
            }
        }

        for (callback in mLifecycleCallbacks){
            mLifecycleDisplay?.append(callback)
        }
        mLifecycleCallbacks.clear()

        logAndAppend(ON_CREATE)
    }

    /**
     * Called when the activity is becoming visible to the user.
     *
     * Followed by onResume() if the activity comes to the foreground, or onStop() if it becomes
     * hidden.
     */
    override fun onStart() {
        super.onStart()

        logAndAppend(ON_START)
    }

    /**
     * Called when the activity will start interacting with the user. At this point your activity
     * is at the top of the activity stack, with user input going to it.
     *
     * Always followed by onPause().
     */
    override fun onResume() {
        super.onResume()

        logAndAppend(ON_RESUME)
    }

    /**
     * Called when the system is about to start resuming a previous activity. This is typically
     * used to commit unsaved changes to persistent data, stop animations and other things that may
     * be consuming CPU, etc. Implementations of this method must be very quick because the next
     * activity will not be resumed until this method returns.
     *
     * Followed by either onResume() if the activity returns back to the front, or onStop() if it
     * becomes invisible to the user.
     */
    override fun onPause() {
        super.onPause()

        logAndAppend(ON_PAUSE)
    }

    /**
     * Called when the activity is no longer visible to the user, because another activity has been
     * resumed and is covering this one. This may happen either because a new activity is being
     * started, an existing one is being brought in front of this one, or this one is being
     * destroyed.
     *
     * Followed by either onRestart() if this activity is coming back to interact with the user, or
     * onDestroy() if this activity is going away.
     */
    override fun onStop() {
        super.onStop()
        mLifecycleCallbacks.add(ON_STOP)
        logAndAppend(ON_STOP)
    }

    /**
     * Called after your activity has been stopped, prior to it being started again.
     *
     * Always followed by onStart()
     */
    override fun onRestart() {
        super.onRestart()

        logAndAppend(ON_RESTART)
    }

    /**
     * The final call you receive before your activity is destroyed. This can happen either because
     * the activity is finishing (someone called finish() on it, or because the system is
     * temporarily destroying this instance of the activity to save space. You can distinguish
     * between these two scenarios with the isFinishing() method.
     */
    override fun onDestroy() {
        super.onDestroy()
        mLifecycleCallbacks.add(ON_DESTROY)
        logAndAppend(ON_DESTROY)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logAndAppend(ON_SAVE_INSTANCE_STATE)
        val lifecycleDisplayTextViewContents = mLifecycleDisplay!!.text.toString()
        outState.putString(LIFECYCLE_CALLBACKS_TEXT_KEY, lifecycleDisplayTextViewContents)
    }

    /**
     * Logs to the console and appends the lifecycle method name to the TextView so that you can
     * view the series of method callbacks that are called both from the app and from within
     * Android Studio's Logcat.
     *
     * @param lifecycleEvent The name of the event to be logged.
     */
    private fun logAndAppend(lifecycleEvent: String) {
        Log.d(TAG, "Lifecycle Event: " + lifecycleEvent)

        mLifecycleDisplay!!.append(lifecycleEvent + "\n")
    }

    /**
     * This method resets the contents of the TextView to its default text of "Lifecycle callbacks"
     *
     * @param view The View that was clicked. In this case, it is the Button from our layout.
     */
    fun resetLifecycleDisplay(view: View) {
        mLifecycleDisplay!!.text = "Lifecycle callbacks:\n"
    }

    companion object {
        private var mLifecycleCallbacks : ArrayList<String> = arrayListOf<String>()

        /*
     * This tag will be used for logging. It is best practice to use the class's name using
     * getSimpleName as that will greatly help to identify the location from which your logs are
     * being posted.
     */
        private val TAG = MainActivity::class.java.simpleName

        /*
     * This constant String will be used to store the content of the TextView used to display the
     * list of callbacks. The reason we are storing the contents of the TextView is so that you can
     * see the entire set of callbacks as they are called.
     */
        private val LIFECYCLE_CALLBACKS_TEXT_KEY = "callbacks"

        /* Constant values for the names of each respective lifecycle callback */
        private val ON_CREATE = "onCreate"
        private val ON_START = "onStart"
        private val ON_RESUME = "onResume"
        private val ON_PAUSE = "onPause"
        private val ON_STOP = "onStop"
        private val ON_RESTART = "onRestart"
        private val ON_DESTROY = "onDestroy"
        private val ON_SAVE_INSTANCE_STATE = "onSaveInstanceState"
    }
}
