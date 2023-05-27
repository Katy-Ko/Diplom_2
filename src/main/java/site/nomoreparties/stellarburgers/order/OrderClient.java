package site.nomoreparties.stellarburgers.order;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.RestClient;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    private static final String GET_AND_CREATE_ORDER_PATH = "/api/orders";

    public ValidatableResponse createOrderWithoutToken(Order order){
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(GET_AND_CREATE_ORDER_PATH)
                .then();
    }

    public ValidatableResponse createOrder(String token, Order order){
        return given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .body(order)
                .when()
                .post(GET_AND_CREATE_ORDER_PATH)
                .then();
    }

    public ValidatableResponse getOrders(String token){
        return given()
                .spec(getBaseSpec())
                .header("authorization", token)
                .get(GET_AND_CREATE_ORDER_PATH)
                .then();
    }


    public ValidatableResponse getOrdersWithoutToken(){
        return given()
                .spec(getBaseSpec())
                .get(GET_AND_CREATE_ORDER_PATH)
                .then();
    }

}
