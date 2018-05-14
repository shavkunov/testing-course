package ru.spbau.shavkunov.webdriver;

import com.sun.istack.internal.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.spbau.shavkunov.webdriver.pages.LoginPage;
import ru.spbau.shavkunov.webdriver.pages.UsersPage;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class YoutrackLoginFieldTest {
    private static WebDriver webDriver;
    private HashSet<String> previousLogins;
    private UsersPage usersPage;

    @Before
    public void saveOldUsers() {
        final @NotNull String login = "root";
        final @NotNull String password = "1";

        webDriver = new ChromeDriver();
        String root = "http://localhost:8080";
        webDriver.get(root);

        WebDriverWait wait = new WebDriverWait(webDriver, 5);

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.getLoginField().insertText(login);
        loginPage.getPasswordField().insertText(password);
        loginPage.getLoginButton().click();

        wait.until(ExpectedConditions.urlContains("/dashboard"));

        usersPage = new UsersPage(webDriver);
        usersPage.loadUsers();
        List<String> users = usersPage.getUsersInTable();

        previousLogins = new HashSet<>();
        previousLogins.addAll(users);
    }

    @After
    public void deleteNewUsers() throws NoDeleteButtonException {
        List<String> users = usersPage.getUsersInTable();

        for (String user : users) {
            if (!previousLogins.contains(user)) {
                usersPage.deleteUser(user);
            }
        }

        webDriver.quit();
    }

    private void simpleUser(String login) {
        assertFalse(previousLogins.contains(login));

        usersPage.createUser(login, "123");
        usersPage.loadUsers();

        assertFalse(previousLogins.contains(login));
        assertTrue(usersPage.getUsersInTable().contains(login));
    }

    // positive tests
    @Test
    public void simpleUserTest() {
        simpleUser("testing");
    }

    @Test
    public void twoUsers() {
        usersPage.createUser("test1", "123");
        usersPage.loadUsers();
        usersPage.createUser("test2", "1234");
        usersPage.loadUsers();
        List<String> users = usersPage.getUsersInTable();

        assertTrue(users.contains("test1"));
        assertTrue(users.contains("test2"));
    }

    @Test
    public void checkRootUser() {
        assertTrue(previousLogins.contains("root"));
    }

    @Test
    public void cyrillicUser() {
        simpleUser("Михаил");
    }

    @Test
    public void number() {
        simpleUser("1234");
    }

    @Test
    public void longLogin() {
        String longLogin = String.join("", Collections.nCopies(30, "1"));
        simpleUser(longLogin);
    }

    //negative tests
    //@Test(expected = Exception.class)
    public void doubleSameUser() throws Exception {
        String login = "testing";

        usersPage.createUser(login, "123");
        usersPage.loadUsers();
        usersPage.createUser(login, "123");
        //usersPage.loadUsers();
    }
}