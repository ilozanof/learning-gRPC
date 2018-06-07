package com.ilozanof.learning.proto.addressBook;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.ilozanof.learning.proto.addressBook.AddressBookProtos.*;

/**
 * Testing class for Google protobuffer.
 * It uses the "src/test/resources/addressbook.proto" file
 */
public class AddressBookProtoTest {

    // Variables to define the output files location:
    private String basedir = System.getProperty("user.dir");
    private Path pathFile = Paths.get(basedir + "/src/test/resources/addressBookFile.txt");

    /**
     * Initilization. Nothing fancy here...
     */
    @Before
    public void init() {
        System.out.println("basedir: " + basedir);
    }

    /**
     * We test the "Serialization" part
     */
    @Test
    public void testSavingAddressBook() {
        try {
            // We create a Person instance...
            Person person1 = Person.newBuilder()
                    .setId(1234)
                    .setName("John Smith")
                    .setEmail("jsmith@example.com")
                    .addPhones(
                            AddressBookProtos.Person.PhoneNumber.newBuilder()
                                    .setNumber("342-23-23")
                                    .setType(AddressBookProtos.Person.PhoneType.HOME)
                    ).build();

            // We create a Person instance...
            Person person2 = Person.newBuilder()
                    .setId(1234)
                    .setName("Mary Jane")
                    .setEmail("jane@example.com")
                    .addPhones(
                            AddressBookProtos.Person.PhoneNumber.newBuilder()
                                    .setNumber("222-23-23")
                                    .setType(AddressBookProtos.Person.PhoneType.HOME)
                    ).build();

            AddressBook addressBook = AddressBook.newBuilder()
                    .addPeople(person1)
                    .addPeople(person2)
                    .build();

            // We save the AddressBook to a File on Disk...
            addressBook.writeTo(Files.newOutputStream(pathFile));

        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * We test the "DesSerialization" part
     */
    @Test
    public void testLoadingAddressBook() {
        try {
            // First we save some content in a file...
            testSavingAddressBook();
            // Now er read and "Deserialize" the file content...
            AddressBook addressBook = AddressBook.parseFrom(Files.newInputStream(pathFile));
            assertNotNull(addressBook);

        } catch (IOException ioe) {
            throw new RuntimeException((ioe));
        }
    }
}
