package ru.spbau.shavkunov.webdriver.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TextField {
    private WebElement element;

    public TextField(WebDriver webDriver, String elementId) {
        this.element = ElementFactory.getElementById(webDriver, elementId);
    }

    public void insertText(String text) {
        element.sendKeys(text);
    }
}