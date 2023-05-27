package site.nomoreparties.stellarburgers.user;

import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@Story("POST /api/auth/register - создание пользователя")
@RunWith(Parameterized.class)
public class CreateUserWithoutParamsTest {

    private static UserClient userClient;
    private final User user;
    private final int expectedStatusCode;
    private final String expectedMessage;

    public CreateUserWithoutParamsTest(User user, int expectedStatusCode, String expectedMessage) {
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getUserDetails() {
        return new Object[][] {
                {new User(null, "000555000", "Joe Black"), 403, "Email, password and name are required fields"},
                {new User("noname@bk.ru", null, "Joe Black"), 403, "Email, password and name are required fields"},
                {new User("noname@bk.ru", "000555000", null), 403, "Email, password and name are required fields"},
        };
    }

    @BeforeClass
    public static void setUp(){
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Невозможность создания пользователя без обязательного параметра")
    public void createCourierWithoutRequiredParamsReturnsError(){

        ValidatableResponse createResponse = userClient.create(user);

        int statusCode = createResponse.extract().statusCode();
        String detailMessage = createResponse.extract().path("message");
        Assert.assertEquals(expectedStatusCode, statusCode);
        Assert.assertEquals(expectedMessage, detailMessage);
    }

}
