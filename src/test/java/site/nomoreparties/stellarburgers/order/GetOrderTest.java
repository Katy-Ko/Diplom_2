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

@Story("GET /api/orders - получение списка заказов конкретного пользователя")
public class GetOrderTest {

    private OrderClient orderClient;
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
    @DisplayName("Получение заказов авторизованного пользователя")
    public void checkGetOrdersAfterAuthorization(){
        ValidatableResponse getOrdersResponse = orderClient.getOrders(token);
        int statusCode = getOrdersResponse.extract().statusCode();
        boolean isListReceived = getOrdersResponse.extract().path("success");
        Assert.assertEquals(200, statusCode);
        Assert.assertTrue(isListReceived);
    }

    @Test
    @DisplayName("Невозможность получить список заказов без авторизации")
    public void checkGetOrdersWithoutToken(){
        ValidatableResponse getOrdersResponse = orderClient.getOrdersWithoutToken();
        int errorStatusCode = getOrdersResponse.extract().statusCode();
        String detailMessage = getOrdersResponse.extract().path("message");
        Assert.assertEquals(401, errorStatusCode);
        Assert.assertEquals("You should be authorised", detailMessage);
    }

    @After
    public void cleanUp(){
        if(token != null) {
            userClient.delete(token);
        }
    }
}
