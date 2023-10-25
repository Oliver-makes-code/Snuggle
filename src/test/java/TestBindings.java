import builtin_types.reflect.annotations.Rename;
import builtin_types.reflect.annotations.SnuggleType;
import builtin_types.reflect.annotations.SnuggleWhitelist;
import builtin_types.reflect.annotations.Unsigned;
import org.junit.jupiter.api.Assertions;

import java.util.HexFormat;

@SnuggleType(name = "Test")
@SnuggleWhitelist
public class TestBindings {

    public static void assertTrue(boolean condition) {
        Assertions.assertTrue(condition);
    }

    public static void assertFalse(boolean condition) {
        Assertions.assertFalse(condition);
    }

    public static void assertEquals(byte expected, byte actual) {
        Assertions.assertEquals(expected, actual);
    }

    @Rename("assertEquals")
    public static void assertEqualsU(@Unsigned byte expected, @Unsigned byte actual) {
        Assertions.assertEquals(expected, actual);
    }

    public static void assertEquals(short expected, short actual) {
        Assertions.assertEquals(expected, actual);
    }

    public static void assertEquals(int expected, int actual) {
        Assertions.assertEquals(expected, actual);
    }

    @Rename("assertEquals")
    public static void assertEqualsU(@Unsigned int expected, @Unsigned int actual) {
        Assertions.assertEquals(expected, actual);
    }

    public static void assertEquals(long expected, long actual) {
        Assertions.assertEquals(expected, actual);
    }

    @Rename("assertEquals")
    public static void assertEqualsU(@Unsigned long expected, @Unsigned long actual) {
        Assertions.assertEquals(expected, actual);
    }

    public static void assertEquals(double expected, double actual) {
        Assertions.assertEquals(expected, actual);
    }

    // todo: (curve25519) only for byte[]
    public static void assertArrayEquals(Object a, Object b) {
        Assertions.assertArrayEquals((byte[]) a, (byte[]) b);
    }

    // todo: (curve25519) make this return a byte[]???
    public static Object hex(String s) {
        return HexFormat.of().parseHex(s);
    }
}