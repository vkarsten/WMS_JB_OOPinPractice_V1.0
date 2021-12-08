package data;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
    static Employee em = new Employee("em1", "pw");
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    static PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStream() {
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void authenticate() {
        assertFalse(em.authenticate("wp"));
        assertFalse(em.isAuthenticated);

        assertTrue(em.authenticate("pw"));
        assertTrue(em.isAuthenticated);
    }

    @Test
    void order() {
        em.order("cheap tablet", 3);
        assertEquals("3 cheap tablets successfully ordered.", outContent.toString());
    }

    @Test
    void greet() {
        em.greet();
        assertEquals("Hello, em1!\n" +
                              "If you experience a problem with the system,\nplease contact technical support.\n", outContent.toString());
    }

    @Test
    void bye() {
        List<String> actions = new ArrayList<>();
        actions.add("Done this");
        actions.add("Done that");
        em.bye(actions);
        assertEquals("\nThank you for your visit, em1!\nIn this session you have: \n1. Done this \n2. Done that \n", outContent.toString());
    }

    @AfterAll
    public static void resetOutput() {
        System.setOut(originalOut);
    }
}