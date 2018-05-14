package ru.spbau.shavkunov.webdriver.pages;

import ru.spbau.shavkunov.webdriver.elements.Button;

public class User {
    private String login;
    private Button deleteUserButton;

    public User(String login, Button deleteUser) {
        this.login = login;
        this.deleteUserButton = deleteUser;
    }

    public String getLogin() {
        return login;
    }

    public Button getDeleteUserButton() {
        return deleteUserButton;
    }
}
