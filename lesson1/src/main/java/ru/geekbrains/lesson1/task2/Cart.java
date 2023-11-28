package ru.geekbrains.lesson1.task2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * Корзина
 * @param <T> Еда
 */
public class Cart<T extends Food> {

    /**
     * Товары в магазине
     */
    private final ArrayList<T> foodstuffs;
    private final UMarket market;
    private final Class<T> clazz;

    public Cart(Class<T> clazz, UMarket market) {
        this.clazz = clazz;
        this.market = market;
        foodstuffs = new ArrayList<>();
    }

    public Collection<T> getFoodstuffs() {
        return foodstuffs;
    }

    /**
     * Распечатать список продуктов в корзине
     */
    public void printFoodstuffs() {
        AtomicInteger index = new AtomicInteger(1);
        foodstuffs.forEach(food -> System.out.printf(
                "[%d] %s (Белки: %s Жиры: %s Углеводы: %s)%n",
                index.getAndIncrement(),
                food.getName(),
                food.getProteins() ? "Да" : "Нет",
                food.getFats() ? "Да" : "Нет",
                food.getCarbohydrates() ? "Да" : "Нет"
        ));
    }

    /**
     * Балансировка корзины
     */
    public void cardBalancing() {

        boolean proteins = checkNutrientFlag(Food::getProteins);
        boolean fats = checkNutrientFlag(Food::getFats);
        boolean carbohydrates = checkNutrientFlag(Food::getCarbohydrates);

        if (proteins && fats && carbohydrates) {
            System.out.println("Корзина уже сбалансирована по БЖУ");

        }

        Collection<T> marketFoods = market.getThings(clazz);
        proteins = checkNutrientFlag(proteins, Food::getProteins, marketFoods);
        fats = checkNutrientFlag(fats, Food::getFats, marketFoods);
        carbohydrates = checkNutrientFlag(carbohydrates, Food::getCarbohydrates, marketFoods);

        if (proteins && fats && carbohydrates) {
            System.out.println("Корзина сбалансирована по БЖУ.");
        } else {
            System.out.println("Невозможно сбалансировать корзину по БЖУ.");
        }

    }

    /**
     * Проверка наличия конкретного питательного элемента в корзине
     *
     * @param nutrientCheck список продуктов в корзине
     * @return состояние обновленного флага питательного элемента
     */
    private boolean checkNutrientFlag(Predicate<Food> nutrientCheck) {
        Optional<T> optionalFood = foodstuffs.stream()
                .filter(nutrientCheck)
                .findFirst();
        return optionalFood.isPresent();
    }

    /**
     * Поиск недостающих питательных элементов в корзине и добавление питательно элемента
     * исходя из общего фильтра продуктов
     *
     * @param nutrientFlag  наличие питательного элемента
     * @param nutrientCheck список продуктов в корзине
     * @param foods         доступный список продуктов (исходя из текущего фильтра)
     * @return состояние обновленного флага питательного элемента (скорее всего будет true,
     * false - в случае, если невозможно найти продукт с нужным питательным элементом, в таком
     * случае, невозможно сбалансировать корзину по БЖУ
     */
    private boolean checkNutrientFlag(boolean nutrientFlag, Predicate<Food> nutrientCheck, Collection<T> foods) {
        if (!nutrientFlag) {
            Optional<T> optionalFood = foods.stream()
                    .filter(nutrientCheck)
                    .findFirst();
            optionalFood.ifPresent(foodstuffs::add);
            return optionalFood.isPresent();
        }
        return true;
    }

//    public void cardBalancing() {
//        boolean proteins = foodstuffs.stream().anyMatch(Food::getProteins);
//        boolean fats = foodstuffs.stream().anyMatch(Food::getFats);
//        boolean carbohydrates = foodstuffs.stream().anyMatch(Food::getCarbohydrates);
//
//        if (proteins && fats && carbohydrates) {
//            System.out.println("Корзина уже сбалансирована по БЖУ.");
//            return;
//        }
//
//        Set<String> missingComponents = new HashSet<>();
//
//        if (!proteins) missingComponents.add("белки");
//        if (!fats) missingComponents.add("жиры");
//        if (!carbohydrates) missingComponents.add("углеводы");
//
//        market.getThings(Food.class).stream()
//                .filter(thing -> missingComponents.contains("белки") && thing.getProteins()
//                        || missingComponents.contains("жиры") && thing.getFats()
//                        || missingComponents.contains("углеводы") && thing.getCarbohydrates())
//                .forEach(thing -> {
//                    if (missingComponents.contains("белки") && thing.getProteins()) {
//                        foodstuffs.add((T) thing);
//                        missingComponents.remove("белки");
//                    }
//                    if (missingComponents.contains("жиры") && thing.getFats()) {
//                        foodstuffs.add((T) thing);
//                        missingComponents.remove("жиры");
//                    }
//                    if (missingComponents.contains("углеводы") && thing.getCarbohydrates()) {
//                        foodstuffs.add((T) thing);
//                        missingComponents.remove("углеводы");
//                    }
//                });
//
//        if (missingComponents.isEmpty()) {
//            System.out.println("Корзина сбалансирована по БЖУ.");
//        } else {
//            System.out.println("Невозможно сбалансировать корзину по следующим компонентам: " + missingComponents);
//        }
}