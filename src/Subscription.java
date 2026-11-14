public class Subscription {
    private String name = "Без назви";
    private double price;
    private String category = "Інше";
    private int usesPerMonth;

    public Subscription(String name, double price, String category) {
        this(name, price, category, 0);
    }

    public Subscription(String name, double price, String category, int usesPerMonth) {
        setName(name);
        setPrice(price);
        setCategory(category);
        setUsesPerMonth(usesPerMonth);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Назва підписки не може бути порожньою.");
        } else {
            this.name = name.trim();
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            System.out.println("Ціна не може бути від'ємною.");
        } else {
            this.price = price;
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            System.out.println("Категорія не може бути порожньою.");
        } else {
            this.category = category.trim();
        }
    }

    public int getUsesPerMonth() {
        return usesPerMonth;
    }

    public void setUsesPerMonth(int usesPerMonth) {
        if (usesPerMonth < 0) {
            System.out.println("Кількість використань не може бути від'ємною.");
        } else {
            this.usesPerMonth = usesPerMonth;
        }
    }

    public double getMonthlyPrice() {
        return price;
    }

    public double getPricePerUse() {
        if (usesPerMonth == 0) {
            return getMonthlyPrice();
        }
        return getMonthlyPrice() / usesPerMonth;
    }

    public String getType() {
        return "підписка";
    }

    public int daysUntilPayment(int todayDay, int todayMonth) {
        return 0;
    }
}
