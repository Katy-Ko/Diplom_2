package site.nomoreparties.stellarburgers.order;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.user.User;
import site.nomoreparties.stellarburgers.user.UserClient;
import site.nomoreparties.stellarburgers.user.UserGenerator;


@Story("POST /api/orders - создание заказа")
public class CreateOrderTest {

    private OrderClient orderClient;
    private Order order;
    private User user;
    private UserClient userClient;
    private String token;

    @Before
    public void setUp(){
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        ValidatableResponse createResponse = userClient.create(user);
        token = createResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Cоздание заказа без авторизации")
    public void checkCreatingNewOrderWithoutAuth(){
        order = OrderGenerator.getIngredientsIds();
        ValidatableResponse createOrderResponse = orderClient.createOrderWithoutToken(order);
        int statusCode = createOrderResponse.extract().statusCode();
        boolean isOrderCreated = createOrderResponse.extract().path("success");
        Assert.assertEquals(200, statusCode);
        Assert.assertTrue(isOrderCreated);
    }

    @Test
    @DisplayName("Cоздание заказа с авторизацией")
    public void checkCreatingNewOrder() {
        order = OrderGenerator.getIngredientsIds();
        ValidatableResponse createOrderResponse = orderClient.createOrder(token, order);
        int statusCode = createOrderResponse.extract().statusCode();
        boolean isOrderCreated = createOrderResponse.extract().path("success");
        Assert.assertEquals(200, statusCode);
        Assert.assertTrue(isOrderCreated);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void checkCreatingNewOrderWithoutIngredients(){
        order = OrderGenerator.getNoIngredients();
        ValidatableResponse createOrderResponse = orderClient.createOrder(token, order);
        int errorStatusCode = createOrderResponse.extract().statusCode();
        String detailMessage = createOrderResponse.extract().path("message");
        Assert.assertEquals(400, errorStatusCode);
        Assert.assertEquals("Ingredient ids must be provided", detailMessage);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void checkInvalidIngredientsHashReturnsError(){
        order = OrderGenerator.getInvalidIngredientsIds();
        ValidatableResponse createOrderResponse = orderClient.createOrder(token, order);
        int errorStatusCode = createOrderResponse.extract().statusCode();
        String detailMessage = createOrderResponse.extract().path("message");
        Assert.assertEquals(400, errorStatusCode);
        Assert.assertEquals("One or more ids provided are incorrect", detailMessage);
    }

    @After
    public void cleanUp(){
        if(token != null) {
            userClient.delete(token);
        }
    }

}