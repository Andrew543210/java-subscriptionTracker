public class SubscriptionManager {
    private static final String TABLE_LINE =
            "────┼──────────────────┼──────────┼───────────┼──────────┼──────────┼─────────────┼────────────";
    private static final String TABLE_BOTTOM =
            "────┴──────────────────┴──────────┴───────────┴──────────┴──────────┴─────────────┴────────────";

    private Subscription[] subscriptions = new Subscription[20];
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void addSubscription(String name, double price, String category) {
        addSubscription(name, price, category, 1);
    }

    public void addSubscription(String name, double price, String category, int paymentDay) {
        addToList(new MonthlySubscription(name, price, category, paymentDay));
    }

    public void addSubscription(String name, double price, String category, int paymentDay, int paymentMonth) {
        addToList(new YearlySubscription(name, price, category, paymentDay, paymentMonth));
    }

    private void addToList(Subscription subscription) {
        if (count == subscriptions.length) {
            System.out.println("Список заповнений: можна зберігати не більше " + subscriptions.length + " підписок.");
        } else {
            subscriptions[count] = subscription;
            count++;
            System.out.println("Підписку «" + subscription.getName() + "» додано.");
        }
    }

    public Subscription findByName(String name) {
        int index = findIndex(name);
        if (index == -1) {
            return null;
        }
        return subscriptions[index];
    }

    public void removeSubscription(String name) {
        int index = findIndex(name);
        if (index == -1) {
            System.out.println("Підписку «" + name + "» не знайдено.");
            return;
        }
        String removedName = subscriptions[index].getName();
        for (int i = index; i < count - 1; i++) {
            subscriptions[i] = subscriptions[i + 1];
        }
        subscriptions[count - 1] = null;
        count--;
        System.out.println("Підписку «" + removedName + "» видалено.");
    }

    private int findIndex(String name) {
        for (int i = 0; i < count; i++) {
            if (subscriptions[i].getName().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public void loadDemoData() {
        if (count + 6 > subscriptions.length) {
            System.out.println("Не вистачає місця для демо-даних: потрібно 6 вільних місць.");
            return;
        }
        addSubscription("Spotify", 169.0, "Музика", 12);
        setLastAddedUses(28);
        addSubscription("Netflix", 250.0, "Відео", 5);
        setLastAddedUses(4);
        addSubscription("YouTube Premium", 149.0, "Відео");
        setLastAddedUses(20);
        addSubscription("iCloud", 1188.0, "Хмара", 15, 3);
        setLastAddedUses(30);
        addSubscription("Спортзал", 700.0, "Спорт", 3);
        addSubscription("Google One", 1200.0, "Хмара", 20, 9);
        setLastAddedUses(1);
        System.out.println("Демо-дані завантажено: 6 підписок.");
    }

    private void setLastAddedUses(int usesPerMonth) {
        subscriptions[count - 1].setUsesPerMonth(usesPerMonth);
    }

    public void printAll(int todayDay, int todayMonth) {
        if (count == 0) {
            System.out.println("Список підписок порожній.");
            return;
        }
        printTableHeader();
        for (int i = 0; i < count; i++) {
            printTableRow(i + 1, subscriptions[i], todayDay, todayMonth);
        }
        printTableFooter();
    }

    public void printAll(int todayDay, int todayMonth, String category) {
        int found = 0;
        for (int i = 0; i < count; i++) {
            if (subscriptions[i].getCategory().equals(category)) {
                found++;
            }
        }
        if (found == 0) {
            System.out.println("У категорії «" + category + "» підписок немає.");
            return;
        }
        printTableHeader();
        int number = 1;
        for (int i = 0; i < count; i++) {
            if (subscriptions[i].getCategory().equals(category)) {
                printTableRow(number, subscriptions[i], todayDay, todayMonth);
                number++;
            }
        }
        printTableFooter();
    }

    public double getTotalMonthly() {
        double total = 0;
        for (int i = 0; i < count; i++) {
            total += subscriptions[i].getMonthlyPrice();
        }
        return total;
    }

    public Subscription findNearestPayment(int todayDay, int todayMonth) {
        if (count == 0) {
            return null;
        }
        Subscription nearest = subscriptions[0];
        for (int i = 1; i < count; i++) {
            int days = subscriptions[i].daysUntilPayment(todayDay, todayMonth);
            int nearestDays = nearest.daysUntilPayment(todayDay, todayMonth);
            if (days < nearestDays) {
                nearest = subscriptions[i];
            }
        }
        return nearest;
    }

    public void printAdvice() {
        if (count == 0) {
            System.out.println("Список підписок порожній, тож відмовлятися поки немає від чого.");
            return;
        }
        double monthlySavings = 0;
        int adviceCount = 0;
        for (int i = 0; i < count; i++) {
            Subscription subscription = subscriptions[i];
            if (subscription.getUsesPerMonth() == 0) {
                String advice = String.format("%s — ти не користуєшся, а платиш %.2f грн на місяць",
                        subscription.getName(), subscription.getMonthlyPrice());
                System.out.println(Main.colorize(advice, Main.RED));
                monthlySavings += subscription.getMonthlyPrice();
                adviceCount++;
            } else if (subscription.getUsesPerMonth() < 5) {
                String advice = String.format("Варто подумати про відмову: %s — лише %d використання на місяць, %.2f грн за одне",
                        subscription.getName(), subscription.getUsesPerMonth(), subscription.getPricePerUse());
                System.out.println(Main.colorize(advice, Main.RED));
                monthlySavings += subscription.getMonthlyPrice();
                adviceCount++;
            } else if (subscription.getPricePerUse() > 50) {
                String advice = String.format("Варто подумати про відмову: %s — %.2f грн за одне використання",
                        subscription.getName(), subscription.getPricePerUse());
                System.out.println(Main.colorize(advice, Main.RED));
                monthlySavings += subscription.getMonthlyPrice();
                adviceCount++;
            }
        }
        if (adviceCount == 0) {
            System.out.println(Main.colorize("Чудово! Усі твої підписки окупаються, так тримати.", Main.GREEN));
        } else {
            System.out.println(Main.SEPARATOR);
            String savings = String.format("Можлива економія: %.2f грн на місяць, %.2f грн на рік",
                    monthlySavings, monthlySavings * 12);
            System.out.println(Main.colorize(savings, Main.GREEN));
        }
    }

    private void printTableHeader() {
        System.out.printf("%-3s │ %-16s │ %-8s │ %-9s │ %8s │ %8s │ %-11s │ %11s%n",
                "№", "Назва", "Тип", "Категорія", "Ціна", "/міс", "До списання", "Ціна за раз");
        System.out.println(TABLE_LINE);
    }

    private void printTableRow(int number, Subscription subscription, int todayDay, int todayMonth) {
        int days = subscription.daysUntilPayment(todayDay, todayMonth);
        String daysText = String.format("%-11s", formatDays(days));
        System.out.printf("%-3d │ %-16.16s │ %-8s │ %-9s │ %8.2f │ %8.2f │ %s │ %11.2f%n",
                number,
                subscription.getName(),
                subscription.getType(),
                subscription.getCategory(),
                subscription.getPrice(),
                subscription.getMonthlyPrice(),
                Main.colorize(daysText, Main.colorForDays(days)),
                subscription.getPricePerUse());
    }

    private void printTableFooter() {
        System.out.println(TABLE_BOTTOM);
        System.out.println("Усі суми вказано в грн.");
    }

    private String formatDays(int days) {
        if (days == 0) {
            return "сьогодні";
        }
        return days + " дн.";
    }
}
