package com.ilozanof.learning.proto.addressBook

import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertNotNull

import java.nio.file.Paths
import java.nio.file.Path

import com.ilozanof.learning.proto.addressBook.AddressBookProtos.Person
import com.ilozanof.learning.proto.addressBook.AddressBookProtos.AddressBook
import java.nio.file.Files

/**
 * We test the Serialization/Deserialization of the AddressBook
 * in Kotlin
 */

class AddressBookProtoTestKT {

    val basedir: String = System.getProperty("user.dir")
    val pathFile: Path = Paths.get("$basedir/src/test/resources/addressBookFileKT.txt")

    /**
     * Initialization. nothing fancy here...
     */
    @Before
    fun init() {
        println("pathFile: $pathFile")
    }

    /**
     * Testing the "Serialization" of the Address Book to a txt File...
     */
    @Test
    fun testSaveAddressBook() {
        // First we crete an instance of Persons, adn we populate
        // the Address Book

        val person1 = Person.newBuilder()
                .setId(1)
                .setName("John Kotlin")
                .setEmail("john@kotlin")
                .build();

        val person2 = Person.newBuilder()
                .setId(1)
                .setName("Jane Kotlin")
                .setEmail("jane@kotlin")
                .build();

        val addressBook = AddressBook.newBuilder()
                .addPeople(person1)
                .addPeople(person2)
                .build()

        // Then we save it to Disk...
        addressBook.writeTo(Files.newOutputStream(pathFile))
    }

    /**
     * Tsting the "Deserialization" of the Address Book from a txt file...
     */
    @Test
    fun testLoadAddressBook() {
        // First we save some data in the file...
        this.testSaveAddressBook()

        // Now we load the data into the object...
        val addressBook = AddressBook.parseFrom(Files.newInputStream(pathFile));
        assertNotNull(addressBook)
    }
}
