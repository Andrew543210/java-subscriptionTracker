public class YearlySubscription extends Subscription {
    private int paymentDay = 1;
    private int paymentMonth = 1;

    public YearlySubscription(String name, double price, String category, int paymentDay, int paymentMonth) {
        super(name, price, category);
        setPaymentDay(paymentDay);
        setPaymentMonth(paymentMonth);
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

    public int getPaymentMonth() {
        return paymentMonth;
    }

    public void setPaymentMonth(int paymentMonth) {
        if (paymentMonth >= 1 && paymentMonth <= 12) {
            this.paymentMonth = paymentMonth;
        } else {
            System.out.println("Місяць списання має бути від 1 до 12.");
        }
    }

    @Override
    public double getMonthlyPrice() {
        return getPrice() / 12;
    }

    @Override
    public int daysUntilPayment(int todayDay, int todayMonth) {
        int days = (paymentMonth - todayMonth) * 30 + (paymentDay - todayDay);
        if (days < 0) {
            days = days + 360;
        }
        return days;
    }

    @Override
    public String getType() {
        return "річна";
    }
}
