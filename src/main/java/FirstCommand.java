/**
 * Первая команда пользователя
 */
public sealed class FirstCommand {

    /**
     * Пользователь ввёл количество многочленов
     */
    static final class PolynomialsCount extends FirstCommand {

        private final int count;

        PolynomialsCount(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Пользователь пожелал выйти
     */
    static final class Cancel extends FirstCommand {
    }
}
