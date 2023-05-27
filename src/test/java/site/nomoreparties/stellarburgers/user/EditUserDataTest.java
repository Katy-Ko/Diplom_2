package site.nomoreparties.stellarburgers.user;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@Story("PATCH /api/auth/user - изменение данных пользователя")
@RunWith(Parameterized.class)
public class EditUserDataTest {
    private static UserClient userClient;
    private final User userForEdit;
    private User userForLogin;
    private String token;

    public EditUserDataTest(User userForEdit){
        this.userForEdit = userForEdit;
    }

    @Parameterized.Parameters
    public static Object[][] getUserData() {
        return new Object[][] {
                {new User("newEmail@bk.ru", "000555000", "Алексей - Царь гусей")},
                {new User("foo-foo@bk.ru", "newPassword", "Алексей - Царь гусей")},
                {new User("foo-foo@bk.ru", "000555000", "Новое имя")},
                {new User("newEmail@bk.ru", "newPassword", "Новое имя")},
        };
    }

    @Before
    public void setUp(){
        userClient = new UserClient();
        userForLogin = UserGenerator.getDefaultCredentials();
        ValidatableResponse createResponse = userClient.create(userForLogin);
        token = createResponse.extract().path("accessToken");
    }

    @Test
    @DisplayName("Возможность изменить любые данные пользователя")
    public void checkAbilityEditUserData(){
        userClient.login(UserCredentials.from(userForLogin));

        ValidatableResponse editResponse = userClient.editData(token, UserCredentials.from(userForEdit));
        int statusCode = editResponse.extract().statusCode();
        boolean isDataChanged = editResponse.extract().path("success");
        Assert.assertEquals(200, statusCode);
        Assert.assertTrue(isDataChanged);

        ValidatableResponse newLoginResponse = userClient.login(UserCredentials.from(userForEdit));
        boolean isUserLoggedIn = newLoginResponse.extract().path("success");
        int statusCodeAfterChange = newLoginResponse.extract().statusCode();
        Assert.assertTrue(isUserLoggedIn);
        Assert.assertEquals(200, statusCodeAfterChange);
        token = newLoginResponse.extract().path("accessToken");
    }

    @After
    public void cleanUp() {
        if (token != null) {
            userClient.delete(token);
        }
    }
}
