import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static final boolean USE_COLORS = true;
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String RESET = "\u001B[0m";
    public static final String SEPARATOR = "────────────────────────────────────────";

    private static final Scanner scanner = new Scanner(System.in);
    private static final SubscriptionManager manager = new SubscriptionManager();
    private static int todayDay;
    private static int todayMonth;

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        todayDay = today.getDayOfMonth();
        todayMonth = today.getMonthValue();

        printWelcome();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Оберіть пункт меню: ", 0, 9);
            System.out.println();
            switch (choice) {
                case 1:
                    askAndAddSubscription();
                    break;
                case 2:
                    showAllSubscriptions();
                    break;
                case 3:
                    showSubscriptionsByCategory();
                    break;
                case 4:
                    showTotals();
                    break;
                case 5:
                    showNearestPayment();
                    break;
                case 6:
                    showAdvice();
                    break;
                case 7:
                    updateUsesPerMonth();
                    break;
                case 8:
                    askAndRemoveSubscription();
                    break;
                case 9:
                    loadDemoData();
                    break;
                case 0:
                    running = false;
                    break;
            }
            if (running) {
                waitForEnter();
            }
        }
        printGoodbye();
    }

    private static void printWelcome() {
        String title = "КУДИ ЙДУТЬ ГРОШІ · трекер підписок";
        String border = "═".repeat(title.length() + 6);
        System.out.println(colorize("╔" + border + "╗", CYAN));
        System.out.println(colorize("║   " + title + "   ║", CYAN));
        System.out.println(colorize("╚" + border + "╝", CYAN));
        System.out.printf("Сьогодні: %02d.%02d%n", todayDay, todayMonth);
    }

    private static void printSectionTitle(String title) {
        String border = "─".repeat(title.length() + 2);
        System.out.println(colorize("┌" + border + "┐", CYAN));
        System.out.println(colorize("│ " + title + " │", CYAN));
        System.out.println(colorize("└" + border + "┘", CYAN));
    }

    private static void printMenu() {
        System.out.println();
        System.out.println(colorize(SEPARATOR, CYAN));
        System.out.println(colorize("МЕНЮ", CYAN));
        System.out.println(colorize(SEPARATOR, CYAN));
        System.out.println("1. Додати підписку");
        System.out.println("2. Показати всі підписки");
        System.out.println("3. Показати підписки за категорією");
        System.out.println("4. Скільки я плачу (місяць / рік)");
        System.out.println("5. Найближче списання");
        System.out.println("6. Від чого відмовитись?");
        System.out.println("7. Оновити кількість використань");
        System.out.println("8. Видалити підписку");
        System.out.println("9. Завантажити демо-дані");
        System.out.println("0. Вихід");
        System.out.println(colorize(SEPARATOR, CYAN));
    }

    private static void askAndAddSubscription() {
        printSectionTitle("НОВА ПІДПИСКА");
        String name = readText("Назва підписки: ");
        String category = chooseCategory();
        int type = readInt("Тип (1 — місячна, 2 — річна): ", 1, 2);
        if (type == 1) {
            double price = readDouble("Ціна за місяць, грн: ", 0);
            boolean hasPaymentDay = readYesNo("Вказати день списання? (так/ні): ");
            if (hasPaymentDay) {
                int paymentDay = readInt("День списання (1–28): ", 1, 28);
                manager.addSubscription(name, price, category, paymentDay);
            } else {
                System.out.println("Списання буде 1-го числа кожного місяця.");
                manager.addSubscription(name, price, category);
            }
        } else {
            double price = readDouble("Ціна за рік, грн: ", 0);
            int paymentDay = readInt("День списання (1–28): ", 1, 28);
            int paymentMonth = readInt("Місяць списання (1–12): ", 1, 12);
            manager.addSubscription(name, price, category, paymentDay, paymentMonth);
        }
        System.out.println("Кількість використань на місяць можна вказати в пункті 7.");
    }

    private static String chooseCategory() {
        System.out.println("Категорії: 1 — Музика, 2 — Відео, 3 — Спорт, 4 — Хмара, 5 — Інше");
        int number = readInt("Номер категорії: ", 1, 5);
        switch (number) {
            case 1:
                return "Музика";
            case 2:
                return "Відео";
            case 3:
                return "Спорт";
            case 4:
                return "Хмара";
            default:
                return "Інше";
        }
    }

    private static void showAllSubscriptions() {
        printSectionTitle("УСІ ПІДПИСКИ");
        manager.printAll(todayDay, todayMonth);
    }

    private static void showSubscriptionsByCategory() {
        printSectionTitle("ПІДПИСКИ ЗА КАТЕГОРІЄЮ");
        String category = chooseCategory();
        System.out.println();
        manager.printAll(todayDay, todayMonth, category);
    }

    private static void showTotals() {
        printSectionTitle("СКІЛЬКИ Я ПЛАЧУ");
        double monthly = manager.getTotalMonthly();
        System.out.printf("На місяць: %.2f грн%n", monthly);
        System.out.printf("На рік:    %.2f грн%n", monthly * 12);
    }

    private static void showNearestPayment() {
        printSectionTitle("НАЙБЛИЖЧЕ СПИСАННЯ");
        Subscription nearest = manager.findNearestPayment(todayDay, todayMonth);
        if (nearest == null) {
            System.out.println("Підписок поки немає.");
            return;
        }
        int days = nearest.daysUntilPayment(todayDay, todayMonth);
        String color = colorForDays(days);
        System.out.printf("Найближче списання: %s, %.2f грн%n", nearest.getName(), nearest.getPrice());
        if (days == 0) {
            System.out.println(colorize("Списання вже сьогодні!", color));
        } else {
            System.out.println(colorize("Залишилось днів: " + days, color));
        }
    }

    private static void showAdvice() {
        printSectionTitle("ВІД ЧОГО ВІДМОВИТИСЬ");
        manager.printAdvice();
    }

    private static void updateUsesPerMonth() {
        printSectionTitle("КІЛЬКІСТЬ ВИКОРИСТАНЬ");
        String name = readText("Назва підписки: ");
        Subscription subscription = manager.findByName(name);
        if (subscription == null) {
            System.out.println("Підписку «" + name + "» не знайдено.");
            return;
        }
        int uses = readInt("Скільки разів на місяць ви нею користуєтесь? ", 0, 1000);
        subscription.setUsesPerMonth(uses);
        System.out.println("Кількість використань для «" + subscription.getName() + "» оновлено: " + uses + ".");
    }

    private static void askAndRemoveSubscription() {
        printSectionTitle("ВИДАЛЕННЯ ПІДПИСКИ");
        String name = readText("Назва підписки для видалення: ");
        manager.removeSubscription(name);
    }

    private static void loadDemoData() {
        printSectionTitle("ДЕМО-ДАНІ");
        manager.loadDemoData();
    }

    private static void waitForEnter() {
        System.out.println();
        System.out.print("Натисніть Enter, щоб продовжити…");
        scanner.nextLine();
    }

    private static void printGoodbye() {
        printSectionTitle("ПІДСУМОК");
        int count = manager.getCount();
        System.out.printf("У тебе %d %s, %.2f грн на місяць.%n",
                count, subscriptionsWord(count), manager.getTotalMonthly());
        System.out.println("До зустрічі!");
    }

    public static String colorize(String text, String color) {
        if (USE_COLORS) {
            return color + text + RESET;
        }
        return text;
    }

    public static String colorForDays(int days) {
        if (days <= 3) {
            return RED;
        }
        if (days <= 7) {
            return YELLOW;
        }
        return GREEN;
    }

    private static String subscriptionsWord(int number) {
        int lastTwoDigits = number % 100;
        int lastDigit = number % 10;
        if (lastTwoDigits >= 11 && lastTwoDigits <= 14) {
            return "підписок";
        }
        if (lastDigit == 1) {
            return "підписка";
        }
        if (lastDigit >= 2 && lastDigit <= 4) {
            return "підписки";
        }
        return "підписок";
    }

    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();
            if (!text.isEmpty()) {
                return text;
            }
            System.out.println("Поле не може бути порожнім. Спробуйте ще раз.");
        }
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            String text = readText(prompt);
            if (isWholeNumber(text)) {
                int value = Integer.parseInt(text);
                if (value >= min && value <= max) {
                    return value;
                }
            }
            System.out.printf("Введіть ціле число від %d до %d.%n", min, max);
        }
    }

    private static double readDouble(String prompt, double min) {
        while (true) {
            String text = readText(prompt).replace(',', '.');
            if (isDecimalNumber(text)) {
                double value = Double.parseDouble(text);
                if (value >= min) {
                    return value;
                }
            }
            System.out.printf("Введіть число не менше %.2f, наприклад 129.99 або 129,99.%n", min);
        }
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            String answer = readText(prompt);
            if (answer.equalsIgnoreCase("так")) {
                return true;
            }
            if (answer.equalsIgnoreCase("ні")) {
                return false;
            }
            System.out.println("Введіть «так» або «ні».");
        }
    }

    private static boolean isWholeNumber(String text) {
        if (text.isEmpty() || text.length() > 9) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            if (symbol < '0' || symbol > '9') {
                return false;
            }
        }
        return true;
    }

    private static boolean isDecimalNumber(String text) {
        if (text.length() > 10) {
            return false;
        }
        int digits = 0;
        int dots = 0;
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            if (symbol >= '0' && symbol <= '9') {
                digits++;
            } else if (symbol == '.') {
                dots++;
            } else {
                return false;
            }
        }
        return digits > 0 && dots <= 1;
    }
}
