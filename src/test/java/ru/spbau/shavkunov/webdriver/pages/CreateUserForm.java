package ru.spbau.shavkunov.webdriver.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.spbau.shavkunov.webdriver.elements.Button;
import ru.spbau.shavkunov.webdriver.elements.TextField;

public class CreateUserForm {
    private TextField loginField;
    private TextField passwordField;
    private TextField passwordConfirmField;
    private Button okButton;

    public CreateUserForm(WebDriver webDriver) {
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_l.U.cr.login")));

        loginField = new TextField(webDriver, "id_l.U.cr.login");
        passwordField = new TextField(webDriver, "id_l.U.cr.password");
        passwordConfirmField = new TextField(webDriver, "id_l.U.cr.confirmPassword");
        okButton = new Button(webDriver, "id_l.U.cr.createUserOk");
    }

    public TextField getLoginField() {
        return loginField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public TextField getPasswordConfirmField() {
        return passwordConfirmField;
    }

    public Button getOkButton() {
        return okButton;
    }
}
