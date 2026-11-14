public class SubscriptionManager {
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
        for (int i = 0; i < count; i++) {
            printSubscription(i + 1, subscriptions[i], todayDay, todayMonth);
        }
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
        int number = 1;
        for (int i = 0; i < count; i++) {
            if (subscriptions[i].getCategory().equals(category)) {
                printSubscription(number, subscriptions[i], todayDay, todayMonth);
                number++;
            }
        }
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
                System.out.printf("%s — ти не користуєшся, а платиш %.2f грн на місяць%n",
                        subscription.getName(), subscription.getMonthlyPrice());
                monthlySavings += subscription.getMonthlyPrice();
                adviceCount++;
            } else if (subscription.getUsesPerMonth() < 5) {
                System.out.printf("Варто подумати про відмову: %s — лише %d використання на місяць, %.2f грн за одне%n",
                        subscription.getName(), subscription.getUsesPerMonth(), subscription.getPricePerUse());
                monthlySavings += subscription.getMonthlyPrice();
                adviceCount++;
            } else if (subscription.getPricePerUse() > 50) {
                System.out.printf("Варто подумати про відмову: %s — %.2f грн за одне використання%n",
                        subscription.getName(), subscription.getPricePerUse());
                monthlySavings += subscription.getMonthlyPrice();
                adviceCount++;
            }
        }
        if (adviceCount == 0) {
            System.out.println("Чудово! Усі твої підписки окупаються, так тримати.");
        } else {
            System.out.printf("Можлива економія: %.2f грн на місяць, %.2f грн на рік%n",
                    monthlySavings, monthlySavings * 12);
        }
    }

    private void printSubscription(int number, Subscription subscription, int todayDay, int todayMonth) {
        System.out.printf("%d. %s (%s, %s): %.2f грн, %.2f грн/міс, до списання %d дн., %.2f грн за раз%n",
                number,
                subscription.getName(),
                subscription.getType(),
                subscription.getCategory(),
                subscription.getPrice(),
                subscription.getMonthlyPrice(),
                subscription.daysUntilPayment(todayDay, todayMonth),
                subscription.getPricePerUse());
    }
}
