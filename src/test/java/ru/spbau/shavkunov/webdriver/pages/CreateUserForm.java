package ru.spbau.shavkunov.webdriver.pages;

import org.openqa.selenium.WebDriver;
import ru.spbau.shavkunov.webdriver.elements.Button;
import ru.spbau.shavkunov.webdriver.elements.TextField;

public class CreateUserForm {
    private TextField loginField;
    private TextField passwordField;
    private TextField passwordConfirmField;
    private Button okButton;

    public CreateUserForm(WebDriver webDriver) {
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
