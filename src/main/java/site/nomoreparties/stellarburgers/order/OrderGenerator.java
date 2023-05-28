package site.nomoreparties.stellarburgers.order;

import site.nomoreparties.stellarburgers.order.Order;

import java.util.List;

public class OrderGenerator {

    public static Order getIngredientsIds(){
        return new Order(List.of("61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa72", "61c0c5a71d1f82001bdaaa6c"));
    }

    public static Order getInvalidIngredientsIds(){
        return new Order(List.of("60d3b41abdacab0026a733c6"));
    }

    public static Order getNoIngredients(){
        return new Order(List.of());
    }

}
