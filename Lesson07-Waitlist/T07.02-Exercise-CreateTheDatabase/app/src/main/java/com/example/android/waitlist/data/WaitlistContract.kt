package com.example.android.waitlist.data

import android.provider.BaseColumns

open class KBaseColumns  {
    val ID = "_id"
    val COUNT = "_count"
}

class WaitlistContract {

    class WaitlistEntry : BaseColumns {
        companion object : KBaseColumns() {
            val TABLE_NAME = "waitlist"
            val COLUMN_GUEST_NAME = "guestName"
            val COLUMN_PARTY_SIZE = "partySize"
            val COLUMN_TIMESTAMP = "timestamp"
        }
    }
}
