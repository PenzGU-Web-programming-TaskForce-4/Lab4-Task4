import java.util.Arrays;

public class PolynomialReader extends Reader<Polynomial> {

    public PolynomialReader(String invitation) {
        super(invitation, "(-?\\d+)(\\s-?\\d+)*");
    }

    @Override
    protected Polynomial parse(String string) {
        var coefficients = Arrays.stream(string.split("\\s")).map(Integer::valueOf).toList();
        return new Polynomial(coefficients);
    }
}
