# SubscriptionTracker — «Куди йдуть гроші»

Консольна програма на Java для відстеження підписок: Spotify, Netflix, iCloud, спортзал тощо.
Вона показує, скільки грошей щомісяця йде на підписки, яке списання найближче і від чого варто відмовитися.

## Навіщо це потрібно

Кожна підписка окремо здається дешевою, але разом набігає помітна сума. Програма допомагає:

- побачити реальні витрати на місяць і на рік (річні підписки перераховуються в місячну суму);
- не пропустити списання: кольори підказують, коли воно вже скоро;
- порахувати ціну одного використання і знайти підписки, які не окупаються;
- оцінити, скільки можна заощадити, якщо від них відмовитися.

## Можливості

1. Додавання місячних і річних підписок.
2. Таблиця всіх підписок.
3. Фільтр за категорією: Музика, Відео, Спорт, Хмара, Інше.
4. Загальна сума на місяць і на рік.
5. Найближче списання.
6. Поради, від чого відмовитися, і можлива економія.
7. Оновлення кількості використань на місяць.
8. Видалення підписки.
9. Демо-дані для швидкого знайомства з програмою.

Кольори в таблиці: червоний — до списання 3 дні або менше, жовтий — до 7 днів, зелений — більше.
Поради відмовитися теж виділено червоним.

## Як запустити

Потрібна JDK 17 або новіша.

### IntelliJ IDEA

1. `File → Open…` і вибрати папку проєкту.
2. Якщо IDEA попросить вказати SDK, вибрати встановлену JDK 17+ (`File → Project Structure → Project → SDK`).
3. Відкрити `src/Main.java` і натиснути зелену кнопку ▶ біля методу `main`.

### Командний рядок

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

У Windows перед запуском варто виконати `chcp 65001`, щоб українські літери та рамки відображалися правильно.
Якщо термінал не підтримує кольори, у класі `Main` встановіть `USE_COLORS = false`.

## Структура проєкту

```
SubscriptionTracker/
├── src/
│   ├── Main.java                 меню, ввід даних, оформлення консолі
│   ├── Subscription.java         базовий клас підписки
│   ├── MonthlySubscription.java  місячна підписка
│   ├── YearlySubscription.java   річна підписка
│   └── SubscriptionManager.java  масив підписок і всі розрахунки
├── .gitignore
└── README.md
```

Дані зберігаються в масиві `Subscription[]` на 20 елементів. Для спрощення місяць вважається таким, що має 30 днів, а рік — 360 днів.

## Де в коді виконано вимоги

| Вимога | Де в коді |
|---|---|
| Зчитування даних (`Scanner`) | `Main`: поле `scanner`, методи `readText`, `readInt`, `readDouble`, `readYesNo`; дата — `LocalDate.now()` у `Main.main` |
| Наслідування | `MonthlySubscription extends Subscription`, `YearlySubscription extends Subscription`; виклик `super(...)` у конструкторах; `@Override` для `getMonthlyPrice`, `daysUntilPayment`, `getType` |
| Перевантаження методів | `Subscription`: два конструктори (з `usesPerMonth` і без); `SubscriptionManager.addSubscription` з 3, 4 і 5 параметрами; `SubscriptionManager.printAll` з 2 і 3 параметрами |
| Інкапсуляція | `private` поля в усіх класах; геттери й сеттери з перевіркою: `Subscription.setName`, `setPrice`, `setUsesPerMonth`, `MonthlySubscription.setPaymentDay`, `YearlySubscription.setPaymentDay`, `setPaymentMonth` |
| Умовні оператори | `if/else`: сеттери, `daysUntilPayment` у підкласах, `Subscription.getPricePerUse`, `SubscriptionManager.printAdvice`, `Main.colorForDays`; `switch`: меню в `Main.main`, `Main.chooseCategory` |
| Цикли | `for`: `SubscriptionManager.getTotalMonthly`, `findNearestPayment`, `removeSubscription` (зсув елементів масиву), `printAll`, `printAdvice`; `while`: головне меню в `Main.main`, повтор запиту в `readText`, `readInt`, `readDouble` |
| Виведення на екран | `System.out.printf`: таблиця в `SubscriptionManager.printTableRow`, суми в `Main.showTotals`; `System.out.println`: меню, рамки, повідомлення |
