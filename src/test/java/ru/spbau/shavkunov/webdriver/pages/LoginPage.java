package ru.spbau.shavkunov.webdriver.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.spbau.shavkunov.webdriver.elements.Button;
import ru.spbau.shavkunov.webdriver.elements.ElementFactory;
import ru.spbau.shavkunov.webdriver.elements.TextField;

public class LoginPage {
    private TextField loginField;
    private TextField passwordField;
    private Button loginButton;

    public LoginPage(WebDriver webDriver) {
        loginField = new TextField(webDriver, "id_l.L.login");
        passwordField = new TextField(webDriver, "id_l.L.password");
        loginButton = new Button(webDriver, "id_l.L.loginButton");
    }

    public TextField getLoginField() {
        return loginField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}
