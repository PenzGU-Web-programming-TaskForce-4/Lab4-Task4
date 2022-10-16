import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.IntStream;

public final class Polynomial {

    private final List<Integer> coefficients;

    public Polynomial(List<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    public Polynomial() {
        this(List.of(0));
    }

    public Polynomial(Integer... coefficients) {
        this(List.of(coefficients));
    }

    public static Polynomial zero() {
        return new Polynomial();
    }

    public Integer getCoefficient(int exp) {
        return coefficients.get(maxExp() - exp);
    }

    public int maxExp() {
        return coefficients.size() - 1;
    }

    public Polynomial multiply(int coefficient, int exp) {
        var newCoefficients = new ArrayList<Integer>(coefficients.size() + exp);

        for (var myCoefficient : coefficients) {
            newCoefficients.add(myCoefficient * coefficient);
        }
        for (int i = 0; i < exp; i++) {
            newCoefficients.add(0);
        }

        return new Polynomial(newCoefficients);
    }

    public Polynomial multiply(Polynomial other) {
        return IntStream
                .range(0, other.coefficients.size()) // indices
                .map((i) -> other.maxExp() - i) // indices to exps
                .mapToObj((exp) -> multiply(other.getCoefficient(exp), exp)) // multiply this polynomial per each other's coefficient
                .reduce(Polynomial.zero(), Polynomial::plus); // summing
    }

    public Polynomial plus(Polynomial other) {
        if (other.equals(Polynomial.zero())) return Polynomial.zero();

        var resultSize = Math.max(coefficients.size(), other.maxExp() + 1);
        var resultCoefficients = new ArrayList<Integer>(resultSize);
        for (int i = 0; i < resultSize; i++) resultCoefficients.add(0);
        int resultI;

        resultI = resultSize - 1;
        for (int i = coefficients.size() - 1; i >= 0; i--) { // set `this` coefficients
            resultCoefficients.set(resultI--, coefficients.get(i));
        }

        resultI = resultSize - 1;
        for (int i = other.coefficients.size() - 1; i >= 0; i--) { // add `other` coefficients
            resultCoefficients.set(resultI, resultCoefficients.get(resultI--) + other.coefficients.get(i));
        }

        return new Polynomial(resultCoefficients);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        for (int exp = maxExp(); exp >= 1; exp--) {
            var coefficient = getCoefficient(exp);
            if (coefficient == 0) continue;
            if (coefficient != 1) {
                if (coefficient < 0) {
                    builder.append("(");
                }
                builder.append(coefficient);
                if (coefficient < 0) {
                    builder.append(")");
                }
                builder.append("*");
            }
            builder.append("X");
            if (exp != 1) {
                builder.append("^");
                builder.append(exp);
            }
            builder.append(" + ");
        }
        builder.append(getCoefficient(0));
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polynomial that = (Polynomial) o;
        return
                coefficients.equals(that.coefficients) ||
                coefficients.stream().allMatch((it) -> it == 0) && that.coefficients.stream().allMatch((it) -> it == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coefficients);
    }

    public static final class CalculationsIterator implements Iterator<Polynomial> {

        int currentI = 0;
        Polynomial current = null;
        List<Polynomial> args;
        List<BinaryOperator<Polynomial>> operations;

        public CalculationsIterator(List<Polynomial> args, List<BinaryOperator<Polynomial>> operations) {
            assert operations.size() == args.size() - 1;
            this.args = args;
            this.operations = operations;
        }

        @Override
        public boolean hasNext() {
            return currentI < operations.size();
        }

        @Override
        public Polynomial next() {
            if (current == null) current = args.get(currentI);
            return current = operations.get(currentI).apply(current, args.get(++currentI));
        }
    }
}
