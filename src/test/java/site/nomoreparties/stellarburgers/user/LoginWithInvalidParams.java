package site.nomoreparties.stellarburgers.user;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@Story("POST /api/auth/login - авторизация пользвоателя")
@RunWith(Parameterized.class)
public class LoginWithInvalidParams {

    private static UserClient userClient;
    private static User user;
    private final UserCredentials credentials;
    private final int expectedStatusCode;
    private final String expectedMessage;
    private String token;

    public LoginWithInvalidParams(UserCredentials credentials, int expectedStatusCode, String expectedMessage) {
        this.credentials = credentials;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;

    }

    @Parameterized.Parameters
    public static Object[][] getUserDetails() {
        return new Object[][] {
                {new UserCredentials("", "000555000"), 401, "email or password are incorrect"},
                {new UserCredentials("foo-foo@bk.ru", ""), 401, "email or password are incorrect"},
                {new UserCredentials("foo-foq@bk.ru", "000555000"), 401, "email or password are incorrect"},
                {new UserCredentials("foo-foq@bk.ru", "010555000"), 401, "email or password are incorrect"},
        };
    }

    @BeforeClass
    public static void setUp(){
        userClient = new UserClient();
        user = UserGenerator.getDefaultCredentials();
        userClient.create(user);
    }

    @Test
    @DisplayName("Логин с невалидными кредами")
    public void loginWithInvalidParamsReturnsError(){
        ValidatableResponse loginResponse = userClient.login(credentials);
        int statusCode = loginResponse.extract().statusCode();
        String detailMessage = loginResponse.extract().path("message");
        Assert.assertEquals(expectedStatusCode, statusCode);
        Assert.assertEquals(expectedMessage, detailMessage);
    }

    @After
    public void cleanUp(){
        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        token = loginResponse.extract().path("accessToken");
        if(token != null) {
            userClient.delete(token);
        }
    }

}
