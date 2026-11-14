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
