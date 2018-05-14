package ru.spbau.shavkunov.webdriver;

import com.sun.istack.internal.NotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.spbau.shavkunov.webdriver.elements.Button;
import ru.spbau.shavkunov.webdriver.pages.LoginPage;
import ru.spbau.shavkunov.webdriver.pages.User;
import ru.spbau.shavkunov.webdriver.pages.UsersPage;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

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

        webDriver.quit();
    }

    @Test
    public void simple() {
        String login = "testing";

        assertFalse(previousLogins.contains(login));

        usersPage.createUser(login, "123");
        usersPage.loadUsers();

        assertFalse(previousLogins.contains(login));

        User user = usersPage.getUserFromTable(login);

        assertNotNull(user);
        assertEquals(login, user.getLogin());
    }
}