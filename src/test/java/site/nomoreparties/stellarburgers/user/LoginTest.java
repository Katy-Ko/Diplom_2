package site.nomoreparties.stellarburgers.user;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

@Story("POST /api/auth/login - авторизация пользвоателя")
public class LoginTest {
    private UserClient userClient;
    private User user;
    private String token;

    @Before
    public void setUp(){
        userClient = new UserClient();
        user = UserGenerator.getRandom();
        userClient.create(user);
    }

    @Test
    @DisplayName("Успешная авторизация существующего пользователя")
    public void validCredentialsLetUserLogin(){

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        boolean isUserLoggedIn = loginResponse.extract().path("success");
        int statusCode = loginResponse.extract().statusCode();
        Assert.assertTrue(isUserLoggedIn);
        Assert.assertEquals(200, statusCode);
        token = loginResponse.extract().path("accessToken");
    }

    @After
    public void cleanUp(){
        if(token != null) {
            userClient.delete(token);
        }
    }

}
