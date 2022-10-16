import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var polynomialReader = new PolynomialReader("Please enter polynomial's coefficients,\nexample: 1 -2 0 4, this numbers represent this statement: X^3 + (-2)*X^2 + 4");

        Polynomial a;
        Polynomial b;
        try(var scanner = new Scanner(System.in)) {
            System.out.println("Reading 1st polynomial");
            a = polynomialReader.read(scanner);
            System.out.println("Reading 2nd polynomial");
            b = polynomialReader.read(scanner);
        }

        try (
            var outWriter = new BufferedWriter(new FileWriter("output.results.txt"))
        ) {
            outWriter.append("result: ");
            outWriter.append(a.multiply(b).toString());
            outWriter.append("\n");
        } catch (IOException exception) {
            System.err.println("something bad happen related to output.results.txt file");
            System.err.printf("result: %s%n", a.multiply(b));
        }
    }
}
