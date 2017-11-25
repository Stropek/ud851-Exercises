package com.example.android.waitlist.data

import android.provider.BaseColumns

class WaitlistContract {
    class WaitlistEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "waitlist"
            val COLUMN_GUEST_NAME = "guestName"
            val COLUMN_PARTY_SIZE = "partySize"
            val COLUMN_TIMESTAMP = "timestamp"
        }
    }

}
