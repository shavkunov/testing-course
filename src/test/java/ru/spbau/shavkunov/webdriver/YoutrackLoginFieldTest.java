package ru.spbau.shavkunov.webdriver;

import com.sun.istack.internal.NotNull;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.spbau.shavkunov.webdriver.elements.Button;
import ru.spbau.shavkunov.webdriver.pages.LoginPage;
import ru.spbau.shavkunov.webdriver.pages.User;
import ru.spbau.shavkunov.webdriver.pages.UsersPage;

import java.util.HashSet;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class YoutrackLoginFieldTest {
    private static WebDriver webDriver = new ChromeDriver();
    private static String root = "http://localhost:8080";
    private UsersPage usersPage;
    private HashSet<String> previousLogins;

    @BeforeClass
    public static void loginAsRoot() {
        final @NotNull String login = "root";
        final @NotNull String password = "1";

        webDriver.get(root);

        LoginPage loginPage = new LoginPage(webDriver);
        loginPage.getLoginField().insertText(login);
        loginPage.getPasswordField().insertText(password);
        loginPage.getLoginButton().click();
    }

    @AfterClass
    public static void quit() {
        webDriver.quit();
    }

    @Before
    public void saveOldUsers() {
        usersPage = new UsersPage(webDriver, root);
        List<User> users = usersPage.getUsersInTable();

        previousLogins = new HashSet<>();
        for (User user : users) {
            previousLogins.add(user.getLogin());
        }
    }

    @After
    public void deleteNewUsers() throws NoDeleteButtonException {
        List<User> users = usersPage.getUsersInTable();

        for (User user : users) {
            if (!previousLogins.contains(user.getLogin())) {
                Button deleteButton = user.getDeleteUserButton();
                if (deleteButton == null) {
                    throw new NoDeleteButtonException();
                }

                user.getDeleteUserButton().click();
                webDriver.switchTo().alert().accept();
            }
        }
    }

    @Test
    public void simple() {
        String login = "testing";

        assertFalse(previousLogins.contains(login));

        usersPage.createUser(login, "123");

        assertFalse(previousLogins.contains(login));

        User user = usersPage.getUserFromTable(login);

        assertNotNull(user);
        assertEquals(login, user.getLogin());
    }
}