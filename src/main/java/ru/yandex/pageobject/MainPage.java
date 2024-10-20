package ru.yandex.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MainPage {
    protected WebDriver driver;

    //Кнопка подтверждения использования Cookies
    private By confirmCookiesUsageButton = By.id("rcc-confirm-button");
    //Кнопка Заказать наверху страницы
    private By newOrderButtonFirst = By.xpath(".//div[contains(@class, 'Header_Nav')]//button[contains(@class, 'Button_Button')]");
    //Кнопка Заказать внизу страницы
    private By newOrderButtonSecond = By.xpath(".//div[contains(@class, 'Home_Finish')]//button[contains(@class, 'Button_Button')]");

    private static final String FAQ_QUESTION_PATTERN  = ".//div[contains(@id, 'accordion__heading') and contains(text(), '%s')]";

    public static final String MAIN_PAGE_URL = "https://qa-scooter.praktikum-services.ru/";

    public MainPage(WebDriver driver){
        this.driver = driver;
    }

    public MainPage openMainPage(){
        driver.get(MAIN_PAGE_URL);
        return this;
    }

    public void clickFAQQuestion(String questionMessage){
        String questionLocator = String.format(FAQ_QUESTION_PATTERN, questionMessage);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath(questionLocator)));

        WebElement questionElement = driver.findElement(By.xpath(questionLocator));
        scrollToElement(questionElement);
        questionElement.click();
    }

    private void scrollToElement(WebElement element){
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
    }

    public void confirmCookiesUsage () {
        driver.findElement(confirmCookiesUsageButton).click();
    }

    public void clickNewOrder (int orderButtonVersion) {
        By newOrderButton = null;
        if (orderButtonVersion == 1) {
            newOrderButton = newOrderButtonFirst;
        }
        else if (orderButtonVersion == 2){
            newOrderButton = newOrderButtonSecond;
        }
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(newOrderButton));
        driver.findElement(newOrderButton).click();
    }

    public String getFAQResponse(String questionMessage) {
        String questionLocator = String.format(FAQ_QUESTION_PATTERN, questionMessage);
        String paragraphText = driver.findElement(By.xpath(questionLocator + "/../following-sibling::div[contains(@id, 'accordion__panel')]//p")).getText();
        return paragraphText;
    }

}
