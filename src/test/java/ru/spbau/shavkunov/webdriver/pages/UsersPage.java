package ru.spbau.shavkunov.webdriver.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.spbau.shavkunov.webdriver.NoDeleteButtonException;
import ru.spbau.shavkunov.webdriver.elements.Button;

import java.util.List;
import java.util.stream.Collectors;

public class UsersPage {
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

    private void creatingUserRoutine(String login, String password) {
        new Button(webDriver, "id_l.U.createNewUser").click();

        CreateUserForm form = new CreateUserForm(webDriver);
        form.getLoginField().insertText(login);
        form.getPasswordField().insertText(password);
        form.getPasswordConfirmField().insertText(password);
        form.getOkButton().click();
    }

    public void createUser(String login, String password) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        creatingUserRoutine(login, password);
        wait.until(ExpectedConditions.urlContains("/editUser"));
    }

    public void createUserWithError(String login, String password) {
        waitingError(login, password, ".message.error");
    }

    public void createUserWithTextFieldError(String login, String password) {
        waitingError(login, password, ".error-bulb2");
    }

    private void waitingError(String login, String password, String expectedElement) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        creatingUserRoutine(login, password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(expectedElement)));

        Button cancelButton = new Button(webDriver, "id_l.U.cr.createUserCancel");
        cancelButton.click();
    }

    public void deleteUser(String login) throws NoDeleteButtonException {
        WebElement userRow = getRows().stream()
                                   .filter(row -> row.findElement(By.cssSelector("*[cn='l.U.usersList.UserLogin.editUser']"))
                                           .getText().equals(login))
                                   .findFirst()
                                   .orElse(null);

        if (userRow == null) {
            throw new NoDeleteButtonException();
        }

        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        WebElement deleteButton = userRow.findElement(By.cssSelector("*[cn='l.U.usersList.deleteUser']"));
        deleteButton.click();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = webDriver.switchTo().alert();
        alert.accept();
        wait.until(ExpectedConditions.stalenessOf(userRow));
    }

    private List<WebElement> getRows() {
        WebElement rows = webDriver.findElement(By.cssSelector(".table.users-table tbody"));

        return rows.findElements(By.tagName("tr"));
    }

    public List<String> getUsersInTable() {
        return getRows().stream()
                        .map(row -> row.findElement(By.cssSelector("*[cn='l.U.usersList.UserLogin.editUser']")))
                        .map(WebElement::getText)
                        .collect(Collectors.toList());
    }
}
