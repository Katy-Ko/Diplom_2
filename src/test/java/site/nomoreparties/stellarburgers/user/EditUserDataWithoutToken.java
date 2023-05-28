package site.nomoreparties.stellarburgers.user;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


@Story("PATCH /api/auth/user - изменение данных пользователя")
public class EditUserDataWithoutToken {

    private static UserClient userClient;
    private static User user;
    private static  User newUser;
    private static String token;


    @BeforeClass
    public static void setUp(){
        userClient = new UserClient();
        user = UserGenerator.getDefaultCredentials();
        ValidatableResponse createResponse = userClient.create(user);
        token = createResponse.extract().path("accessToken");
        newUser = UserGenerator.getRandom();
    }

    @Test
    @DisplayName("Невозможность изменить данные пользователя без авторизации")
    public void checkEditUserWithoutToken_returnsError(){
        ValidatableResponse editResponse = userClient.editDataWithoutToken((UserCredentials.from(newUser)));
        int statusCode = editResponse.extract().statusCode();
        String detailMessage = editResponse.extract().path("message");
        Assert.assertEquals(401, statusCode);
        Assert.assertEquals("You should be authorised", detailMessage);
    }

    @After
    public void cleanUp() {
        if (token != null) {
            userClient.delete(token);
        }
    }
}
