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
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*
         * DataBindUtil.setContentView replaces our normal call of setContent view.
         * DataBindingUtil also created our ActivityMainBinding that we will eventually use to
         * display all of our data.
         */
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val fakeBoardingInfo = FakeDataUtils.generateFakeBoardingPassInfo()
        displayBoardingPassInfo(fakeBoardingInfo)
    }

    private fun displayBoardingPassInfo(info: BoardingPassInfo) {

        mBinding.textViewPassengerName.text = info.passengerName

        mBinding.ltFlightInfo!!.textViewOriginAirport.text = info.originCode
        mBinding.ltFlightInfo!!.textViewFlightCode.text = info.flightCode
        mBinding.ltFlightInfo!!.textViewDestinationAirport.text = info.destCode

        val formatter = SimpleDateFormat(getString(R.string.timeFormat), Locale.getDefault())
        val boardingTime = formatter.format(info.boardingTime)
        val departureTime = formatter.format(info.departureTime)
        val arrivalTime = formatter.format(info.arrivalTime)

        mBinding.textViewBoardingTime.text = boardingTime
        mBinding.textViewDepartureTime.text = departureTime
        mBinding.textViewArrivalTime.text = arrivalTime

        val totalMinutesUntilBoarding = info.minutesUntilBoarding
        val hoursUntilBoarding = TimeUnit.MINUTES.toHours(totalMinutesUntilBoarding)
        val minutesLessHoursUntilBoarding = totalMinutesUntilBoarding - TimeUnit.HOURS.toMinutes(hoursUntilBoarding)

        val hoursAndMinutesUntilBoarding = getString(R.string.countDownFormat,
                hoursUntilBoarding,
                minutesLessHoursUntilBoarding)

        mBinding.textViewBoardingInCountdown.text = hoursAndMinutesUntilBoarding

        mBinding.ltBoardingTable!!.textViewTerminal.text = info.departureTerminal
        mBinding.ltBoardingTable!!.textViewGate.text = info.departureGate
        mBinding.ltBoardingTable!!.textViewSeat.text = info.seatNumber
    }
}

