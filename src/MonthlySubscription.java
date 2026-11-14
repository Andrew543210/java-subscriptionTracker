public class MonthlySubscription extends Subscription {
    private int paymentDay = 1;

    public MonthlySubscription(String name, double price, String category, int paymentDay) {
        super(name, price, category);
        setPaymentDay(paymentDay);
    }

    public int getPaymentDay() {
        return paymentDay;
    }

    public void setPaymentDay(int paymentDay) {
        if (paymentDay >= 1 && paymentDay <= 28) {
            this.paymentDay = paymentDay;
        } else {
            System.out.println("День списання має бути від 1 до 28.");
        }
    }

    @Override
    public double getMonthlyPrice() {
        return getPrice();
    }

    @Override
    public int daysUntilPayment(int todayDay, int todayMonth) {
        if (paymentDay >= todayDay) {
            return paymentDay - todayDay;
        }
        return 30 - todayDay + paymentDay;
    }

    @Override
    public String getType() {
        return "місячна";
    }
}
