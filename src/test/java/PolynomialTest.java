import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PolynomialTest {

    @Test
    void testToString() {
        var polynomial = new Polynomial(1, -2, 3, 4);
        assertEquals("X^3 + (-2)*X^2 + 3*X + 4", polynomial.toString());

        var emptyPolynomial = new Polynomial();
        assertEquals("0", emptyPolynomial.toString());
    }

    @Test
    void testMultiplyOnSingle() {
        var polynomial = new Polynomial(1, -2, 3, 4);
        assertEquals(new Polynomial(10, -20, 30, 40, 0), polynomial.multiply(10, 1));
    }

    @Test
    void testMultiplyOnOther() {
        var a = new Polynomial(1, -2, 3, 4);
        var b = new Polynomial(1, -2, 3, 4);
        assertEquals(new Polynomial(1, -4, 10, -4, -7, 24, 16), a.multiply(b));
    }

    @Test
    void testPlus() {
        var a = new Polynomial(1, -2, 3, 4);
        var b = new Polynomial(0, -1, 2, -3, -4);
        assertEquals(Polynomial.zero(), a.plus(b));
    }

    static class CalculationsIteratorTest {

        @Test
        void hasNext() {
            var a = new Polynomial.CalculationsIterator(List.of(Polynomial.zero()), Collections.emptyList());
            assertFalse(a.hasNext());

            assertThrows(
                    AssertionError.class,
                    () -> new Polynomial.CalculationsIterator(Collections.emptyList(), Collections.emptyList())
            );

            var b = new Polynomial.CalculationsIterator(
                    List.of(Polynomial.zero(), Polynomial.zero()),
                    List.of(Polynomial::plus)
            );
            assertTrue(b.hasNext());
        }

        @Test
        void next() {
            var actionsIterator = new Polynomial.CalculationsIterator(
                    List.of(
                            new Polynomial(1, -2, 3, 4),
                            new Polynomial(2),
                            new Polynomial(-2, 4, -6, -8)
                    ),
                    List.of(
                            Polynomial::multiply,
                            Polynomial::plus
                    )
            );
            assertEquals(new Polynomial(2, -4, 6, 8), actionsIterator.next());
            assertEquals(Polynomial.zero(), actionsIterator.next());
        }
    }
}
