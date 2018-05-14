package ru.spbau.shavkunov.webdriver.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.spbau.shavkunov.webdriver.elements.Button;

import java.util.List;
import java.util.stream.Collectors;

public class UsersPage {
    private Button createUser;
    private WebDriver webDriver;

    public UsersPage(WebDriver webDriver, String root) {
        this.webDriver = webDriver;

        String usersUrl = root + "/users";
        loadUsersPage(webDriver, usersUrl);

        this.createUser = new Button(webDriver, "id_l.U.createNewUser");
    }

    private void loadUsersPage(WebDriver webDriver, String usersUrl) {
        webDriver.get(usersUrl);

        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.urlToBe(usersUrl));
    }

    public void createUser(String login, String password) {
        createUser.click();

        CreateUserForm form = new CreateUserForm(webDriver);
        form.getLoginField().insertText(login);
        form.getPasswordField().insertText(password);
        form.getPasswordConfirmField().insertText(password);
        form.getOkButton().click();
    }

    public List<User> getUsersInTable() {
        WebElement userTableBody = webDriver.findElement(By.cssSelector(".table.users-table tbody"));

        return userTableBody.findElements(By.tagName("tr")).stream().map(webElement -> {
            WebElement loginElement = webElement.findElement(By.cssSelector("*[cn='l.U.usersList.UserLogin.editUser']"));

            String login = loginElement.getText();
            Button deleteButton;
            try {
                WebElement deleteButtonWebElement = webElement.findElement(
                        By.cssSelector("td:last-child a[cn='l.U.usersList.deleteUser']")
                );

                deleteButton = new Button(deleteButtonWebElement);
            } catch (NoSuchElementException e) {
                deleteButton = null;
            }

            return new User(login, deleteButton);
        }).collect(Collectors.toList());
    }

    public User getUserFromTable(String login) {
        List<User> users = getUsersInTable();

        for (User user : users ) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }

        return null;
    }
}