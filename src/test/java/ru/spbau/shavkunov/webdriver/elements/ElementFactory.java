package ru.spbau.shavkunov.webdriver.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ElementFactory {
    public static WebElement getElementById(WebDriver webDriver, String elementId) {
        return webDriver.findElement(By.id(elementId));
    }
}