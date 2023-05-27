package site.nomoreparties.stellarburgers.user;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

@Story("POST /api/auth/register - создание пользователя")
public class CreateUserTest {

    private static UserClient userClient;
    private User user;
    private String token;

    @BeforeClass
    public static void setUp(){
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Cоздание уникального пользователя")
    public void checkCreatingOfNewUser(){
        user = UserGenerator.getRandom();
        ValidatableResponse createResponse = userClient.create(user);

        int statusCode = createResponse.extract().statusCode();
        boolean isUserCreated = createResponse.extract().path("success");
        Assert.assertEquals(200, statusCode);
        Assert.assertTrue(isUserCreated);

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        boolean isUserLoggedIn = loginResponse.extract().path("success");
        Assert.assertTrue(isUserLoggedIn);
        token = loginResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Cоздание пользователя, который уже зарегистрирован")
    public void checkSameCredentialsReturnError(){
        user = UserGenerator.getDefaultCredentials();
        userClient.create(user);
        ValidatableResponse createNewResponse = userClient.create(user);

        int errorStatusCode = createNewResponse.extract().statusCode();
        String detailMessage = createNewResponse.extract().path("message");
        Assert.assertEquals(403, errorStatusCode);
        Assert.assertEquals("User already exists", detailMessage);

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        boolean isUserLoggedIn = loginResponse.extract().path("success");
        Assert.assertTrue(isUserLoggedIn);
        token = loginResponse.extract().path("accessToken");
    }

    @After
    public void cleanUp(){
        if(token != null) {
            userClient.delete(token);
        }
    }


}
