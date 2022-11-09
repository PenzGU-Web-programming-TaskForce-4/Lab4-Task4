import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * Это многочлен
 */
public final class Polynomial {

    /**
     * Коэффициенты многочлена
     */
    private final List<Integer> coefficients;

    /**
     * Конструктор многочлена из списка
     *
     * @param coefficients Коэффициенты многочлена
     */
    public Polynomial(List<Integer> coefficients) {
        this.coefficients = coefficients.isEmpty() ? List.of(0) : coefficients;
    }

    /**
     * Пустой конструктор для многочлена равного 0
     */
    public Polynomial() {
        this(List.of(0));
    }

    /**
     * Удобный конструктор многочлена из неизвестного количества входящих коэффициентов
     *
     * @param coefficients Коэффициенты многочлена
     */
    public Polynomial(Integer... coefficients) {
        this(List.of(coefficients));
    }

    /**
     * Фабричный метод для многочлена равного 0
     *
     * @return "Пустой" многочлен
     */
    public static Polynomial zero() {
        return new Polynomial();
    }

    /**
     * Выдаёт коэффициент при слагаемом многочлена по степени его X'а
     *
     * @param exp степень x'а в многочлене
     * @return Коэффициент по его степени или возвращает 0, если такой коэффициент не "записан", т.к. это и означает,
     * что он равен 0
     */
    public Integer getCoefficient(int exp) {
        if (maxExp() - exp > coefficients.size() - 1) return 0;
        return coefficients.get(maxExp() - exp);
    }

    /**
     * Возвращает максимальную степень слагаемого в многочлене
     *
     * @return Максимальную степень многочлена
     */
    public int maxExp() {
        return coefficients.size() - 1;
    }

    /**
     * Находит произведение этого многочлена с одним слагаемым многочлена
     *
     * @param coefficient коэффициент элемента многочлена
     * @param exp         степень X'а элемента многочлена
     * @return Результат умножения этого многочлена на элемент многочлена
     */
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

    /**
     * Находит произведение этого и поступившего многочлена
     *
     * @param other второй множитель
     * @return Результат перемножения
     */
    public Polynomial multiply(Polynomial other) {
        Polynomial product = Polynomial.zero();
        for (int exp = 0; exp < maxExp(); exp++) {
            Polynomial term = this.multiply(other.getCoefficient(exp), exp);
            product = product.plus(term);
        }
        return product;
    }

    /**
     * Находит сумму этого и поступившего многочлена
     *
     * @param other второе слагаемое
     * @return Сумму многочленов
     */
    public Polynomial plus(Polynomial other) {
        if (other.equals(Polynomial.zero())) return this;

        var resultSize = Math.max(coefficients.size(), other.maxExp() + 1);
        var resultCoefficients = new ArrayList<Integer>(resultSize);
        for (int i = 0; i < resultSize; i++) resultCoefficients.add(0);

        int resultI = resultSize - 1;
        for (int i = coefficients.size() - 1; i >= 0; i--) { // set `this` coefficients
            resultCoefficients.set(resultI--, coefficients.get(i));
        }

        resultI = resultSize - 1;
        for (int i = other.coefficients.size() - 1; i >= 0; i--) { // add `other` coefficients
            resultCoefficients.set(resultI, resultCoefficients.get(resultI--) + other.coefficients.get(i));
        }

        return new Polynomial(resultCoefficients);
    }

    /**
     * Возвращает псевдоматематическое отображение многочлена
     *
     * @return Математическое отображение многочлена
     */
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

    /**
     * Возвращает, идентичен ли этот многочлен какому-то объекту, если этот многочлен и другой многочлен имеют
     * одинаковый список коэффициентов или у этого и другого многочлена коэффициенты равны 0, то это значит, что оба
     * наших многочлена равны 0 => многочлены равны
     *
     * @param o какой-то объект
     * @return boolean, Равны ли мы
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var other = (Polynomial) o;
        return coefficients.equals(other.coefficients) ||
                coefficients.stream().allMatch((it) -> it == 0) &&
                        other.coefficients.stream().allMatch((it) -> it == 0);
    }

    /**
     * Возвращает хэшкод единственного поля
     *
     * @return Хэшкод единственного поля
     */
    @Override
    public int hashCode() {
        return Objects.hash(coefficients);
    }

    /**
     * Итератор, позволяющий пройтись пошагово по цепочке действий меж многочленами, где выражение вида p1 + p2 * p3,
     * где pn - многочлен должно будет создано таким образом:
     * <pre>{@code new CalculationsIterator(
     *     List.of(p2, p3, p1),
     *     List.of(Polynomial::multiply, Polynomial::plus)
     * );}</pre>
     * А для более сложных операций этот итератор не создавался
     *
     * @see "PolynomialTest::CalculationsIteratorTest"
     */
    public static final class CalculationsIterator implements Iterator<Polynomial> {

        private int currentI = 0;
        private Polynomial current = null;
        private final List<Polynomial> args;
        private final List<BinaryOperator<Polynomial>> operations;

        /**
         * Конструктор просо передающий аргументы и операции
         *
         * @param args       слагаемые и множители по которым будут произведены операции
         * @param operations операции
         * @throws AssertionError если количество операций != количеству аргументов + 1
         */
        public CalculationsIterator(List<Polynomial> args, List<BinaryOperator<Polynomial>> operations) {
            assert operations.size() == args.size() - 1;
            this.args = args;
            this.operations = operations;
        }

        /**
         * Говорит, остались ли ещё операции для выполнения
         *
         * @return boolean, о том, остались ли ещё операции для выполнения
         */
        @Override
        public boolean hasNext() {
            return currentI < operations.size();
        }

        /**
         * Производит операцию и возвращает результат
         *
         * @return возвращает результат по одной операции
         */
        @Override
        public Polynomial next() {
            if (current == null) current = args.get(currentI);
            return current = operations.get(currentI).apply(current, args.get(++currentI));
        }
    }
}
