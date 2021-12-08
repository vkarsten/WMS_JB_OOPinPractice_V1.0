package data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {
    static Admin admin = new Admin("admin", "admin");
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    static PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStream() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void resetOutput() {
        System.setOut(originalOut);
    }

    @Test
    void greet() {
        admin.greet();
        assertEquals("Hello, admin!\nWelcome to the Admin Panel.\nWith higher authority comes higher responsibility.\n", outContent.toString());
    }
}