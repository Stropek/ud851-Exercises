package com.example.android.boardingpass

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

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.boardingpass.databinding.ActivityMainBinding
import com.example.android.boardingpass.utilities.FakeDataUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val bpi = FakeDataUtils.generateFakeBoardingPassInfo()
        displayBoardingPassInfo(bpi)
    }

    private fun displayBoardingPassInfo(info: BoardingPassInfo) {
        mBinding.textViewPassengerName.text = info.passengerName
        mBinding.textViewFlightCode.text = info.flightCode
        mBinding.textViewOriginAirport.text = info.originCode
        mBinding.textViewDestinationAirport.text = info.destCode

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        mBinding.textViewBoardingTime.text = timeFormat.format(info.boardingTime)
        mBinding.textViewDepartureTime.text = timeFormat.format(info.departureTime)
        mBinding.textViewArrivalTime.text = timeFormat.format(info.arrivalTime)

        val hoursUntilBoarding = TimeUnit.MINUTES.toHours(info.minutesUntilBoarding)
        val minutesUntilBoarding = info.minutesUntilBoarding % 60
        mBinding.textViewBoardingInCountdown.text = "$hoursUntilBoarding:$minutesUntilBoarding"

        mBinding.textViewTerminal.text = info.departureTerminal
        mBinding.textViewGate.text = info.departureGate
        mBinding.textViewSeat.text = info.seatNumber
    }
}

