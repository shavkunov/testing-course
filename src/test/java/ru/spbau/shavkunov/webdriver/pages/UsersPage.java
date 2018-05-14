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
    private static final By createUserButtonSelector = By.id("id_l.U.createNewUser");
    private WebDriver webDriver;

    public UsersPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void loadUsers() {
        WebDriverWait wait = new WebDriverWait(webDriver, 5);

        WebElement settingsDropdown =
                webDriver.findElement(By.cssSelector(".ring-menu__item__i.ring-font-icon.ring-font-icon_cog"));
        settingsDropdown.click();
        By dropdownOptionsSelector = By.cssSelector(".ring-dropdown__item.ring-link");

        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptionsSelector));

        List<WebElement> dropdownOptions = webDriver.findElements(dropdownOptionsSelector);
        WebElement usersDropdown =
                dropdownOptions.stream()
                        .filter(webElement -> webElement.getAttribute("href").endsWith("/users"))
                        .findFirst()
                        .get();
        usersDropdown.click();

        wait.until(ExpectedConditions.urlContains("/users"));
    }

    public void createUser(String login, String password) {
        WebElement createUserButton = webDriver.findElement(createUserButtonSelector);
        createUserButton.click();

        WebDriverWait wait = new WebDriverWait(webDriver, 5);

        CreateUserForm form = new CreateUserForm(webDriver);
        form.getLoginField().insertText(login);
        form.getPasswordField().insertText(password);
        form.getPasswordConfirmField().insertText(password);
        form.getOkButton().click();

        wait.until(ExpectedConditions.urlContains("/editUser"));
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
