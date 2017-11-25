package com.example.android.waitlist

import android.provider.BaseColumns

import com.example.android.waitlist.data.WaitlistContract

import org.junit.Test

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ContractClassUnitTest {

    @Test
    @Throws(Exception::class)
    fun inner_class_exists() {
        val innerClasses = WaitlistContract::class.java.declaredClasses
        assertEquals("There should be 1 Inner class inside the contract class", 1, innerClasses.size.toLong())
    }

    @Test
    @Throws(Exception::class)
    fun inner_class_type_correct() {
        val innerClasses = WaitlistContract::class.java.declaredClasses
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.size.toLong())
        val entryClass = innerClasses[0]
        assertTrue("Inner class should implement the BaseColumns interface", BaseColumns::class.java.isAssignableFrom(entryClass))
        assertTrue("Inner class should be final", Modifier.isFinal(entryClass.modifiers))
        assertTrue("Inner class should be static", Modifier.isStatic(entryClass.modifiers))
    }

    @Test
    @Throws(Exception::class)
    fun inner_class_members_correct() {
        val innerClasses = WaitlistContract::class.java.declaredClasses
        assertEquals("Cannot find inner class to complete unit test", 1, innerClasses.size.toLong())
        val entryClass = innerClasses[0]
        val allFields = entryClass.declaredFields
        assertEquals("There should be exactly 4 String members in the inner class", 4, allFields.size.toLong())
        for (field in allFields) {
            assertTrue("All members in the contract class should be Strings", field.type == String::class.java)
            assertTrue("All members in the contract class should be final", Modifier.isFinal(field.modifiers))
            assertTrue("All members in the contract class should be static", Modifier.isStatic(field.modifiers))
        }
    }

}