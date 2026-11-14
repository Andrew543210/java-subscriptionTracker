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
}
