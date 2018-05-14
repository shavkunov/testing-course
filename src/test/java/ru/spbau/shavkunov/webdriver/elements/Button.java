package ru.spbau.shavkunov.webdriver.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Button {
    private WebElement element;

    public Button(WebDriver webDriver, String elementId) {
        this.element = ElementFactory.getElementById(webDriver, elementId);
    }

    public void click() {
        element.click();
    }
}
