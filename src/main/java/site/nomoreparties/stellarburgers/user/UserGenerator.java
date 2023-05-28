package site.nomoreparties.stellarburgers.user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {

    public static User getDefaultCredentials(){
        final String email = "foo-foo@bk.ru";
        final String password = "000555000";
        final String name = "Алексей - Царь гусей";
        return new User(email, password, name);
    }

    public static User getRandom(){
        final String email = RandomStringUtils.randomAlphabetic(10) + "@bk.ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }
}
