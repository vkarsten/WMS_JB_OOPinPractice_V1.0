package data;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class GuestTest {
    static Guest guest = new Guest("guest");
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    static PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStream() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void greet() {
        guest.greet();
        assertEquals("Hello, guest!\nWelcome to our Warehouse Database.\nIf you don't find what you are looking for," +
        "\nplease ask one of our staff members to assist you.\n", outContent.toString());
    }

    @Test
    void bye() {
        guest.bye(null);
        assertEquals("Thank you for your visit, guest!\n", outContent.toString());
    }

    @Test
    void authenticate() {
        assertFalse(guest.authenticate("password"));
    }

    @AfterAll
    public static void resetOutput() {
        System.setOut(originalOut);
    }
}