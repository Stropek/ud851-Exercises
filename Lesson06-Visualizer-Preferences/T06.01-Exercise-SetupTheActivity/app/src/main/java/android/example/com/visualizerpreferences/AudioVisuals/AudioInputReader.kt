package android.example.com.visualizerpreferences.AudioVisuals

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

import android.content.Context
import android.example.com.visualizerpreferences.R
import android.media.MediaPlayer
import android.media.audiofx.Visualizer
import android.os.Build


/**
 * [AudioInputReader] sets up and tears down the [MediaPlayer] and [Visualizer]
 */

class AudioInputReader(private val mVisualizerView: VisualizerView, private val mContext: Context) {
    private var mPlayer: MediaPlayer? = null
    private var mVisualizer: Visualizer? = null


    init {
        initVisualizer()
    }

    private fun initVisualizer() {
        // Setup media player
        mPlayer = MediaPlayer.create(mContext, R.raw.htmlthesong)
        mPlayer!!.isLooping = true

        // Setup the Visualizer
        // Connect it to the media player
        mVisualizer = Visualizer(mPlayer!!.audioSessionId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mVisualizer!!.measurementMode = Visualizer.MEASUREMENT_MODE_PEAK_RMS
            mVisualizer!!.scalingMode = Visualizer.SCALING_MODE_NORMALIZED
        }

        // Set the size of the byte array returned for visualization
        mVisualizer!!.captureSize = Visualizer.getCaptureSizeRange()[0]
        // Whenever audio data is available, update the visualizer view
        mVisualizer!!.setDataCaptureListener(
                object : Visualizer.OnDataCaptureListener {
                    override fun onWaveFormDataCapture(visualizer: Visualizer,
                                                       bytes: ByteArray, samplingRate: Int) {

                        // Do nothing, we are only interested in the FFT (aka fast Fourier transform)
                    }

                    override fun onFftDataCapture(visualizer: Visualizer,
                                                  bytes: ByteArray, samplingRate: Int) {
                        // If the Visualizer is ready and has data, send that data to the VisualizerView
                        if (mVisualizer != null && mVisualizer!!.enabled) {
                            mVisualizerView.updateFFT(bytes)
                        }

                    }
                },
                Visualizer.getMaxCaptureRate(), false, true)

        // Start everything
        mVisualizer!!.enabled = true
        mPlayer!!.start()
    }

    fun shutdown(isFinishing: Boolean) {

        if (mPlayer != null) {
            mPlayer!!.pause()
            if (isFinishing) {
                mVisualizer!!.release()
                mPlayer!!.release()
                mPlayer = null
                mVisualizer = null
            }
        }

        if (mVisualizer != null) {
            mVisualizer!!.enabled = false
        }
    }

    fun restart() {

        if (mPlayer != null) {
            mPlayer!!.start()
        }

        mVisualizer!!.enabled = true
        mVisualizerView.restart()
    }
}
