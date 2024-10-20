package ru.yandex.pageobject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderPage {
    protected WebDriver driver;

    //Поле ввода даты заказа
    private final By deliveryDateInput = By.xpath(".//input[contains(@placeholder, 'Когда привезти самокат')]");
    //Подтверждение даты на календаре
    private final By deliveryDateDataPicker = By.xpath(".//div[contains(@class, 'datepicker__day--selected')]");
    //Поле ввода имени
    private final By nameInput = By.xpath(".//input[@placeholder = '* Имя']");
    //Поле ввода Фамилии
    private final By surnameInput = By.xpath(".//input[@placeholder = '* Фамилия']");
    //Поле ввода адреса
    private final By addressInput = By.xpath(".//input[starts-with(@placeholder, '* Адрес')]");
    //Выпадающий список со станциями метро
    private final By metroDropdown = By.xpath(".//input[@placeholder = '* Станция метро']");
    //Элемент выпадающего списка со станциями метро - конкретнная станция
    private final By mobileInput = By.xpath(".//input[starts-with(@placeholder, '* Телефон')]");
    //Кнопка перехода на сторую страницу полей заказа
    private final By nextButton = By.xpath(".//div[contains(@class, 'Order_Content')]//button[contains(@class, 'Button_Button')]");
    //Выпадающий список с длительностью аренды
    private final By deliveryDurationDropdown = By.xpath(".//span[@class = 'Dropdown-arrow']");
    //Поле ввода комменнтария
    private final By commentInput = By.xpath(".//input[starts-with(@placeholder, 'Комментарий')]");
    //Кнопка совершения заказа "Заказать"
    private final By orderButton = By.xpath(".//div[contains(@class, 'Order_Buttons')]//button[contains(@class, 'Button_Button') and text()='Заказать']");
    //Кнопка подтверждения заказа "Да"
    private final By confirmOrderButton = By.xpath(".//div[contains(@class, 'Order_Buttons')]//button[contains(@class, 'Button_Button') and text()='Да']");
    //Элемент с текстом подтверждения сделанного заказа
    private final By orderConfirmationText = By.xpath(".//div[starts-with(@class, 'Order_Text')]");


    public static final String METRO_STATION_PATTERN = ".//div[starts-with(@class, 'Order_Text') and text()= '%s']";
    public static final String ORDER_DURATION_PATTERN = ".//div[contains(@class, 'Dropdown-menu')]//div[text()='%s']";
    public static final String COLOR_PATTERN = ".//div[text()='Цвет самоката']/following-sibling::label[input[starts-with(@class, 'Checkbox_Input')] and text()='%s']";

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    private void setTextField (By inputLocator, String text){
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(inputLocator));
        driver.findElement(inputLocator).clear();
        driver.findElement(inputLocator).sendKeys(text);
    }

    public void setName (String name) {
        setTextField(nameInput, name);
    }

    public void setSurname (String surname) {
        setTextField(surnameInput, surname);
    }

    public void setAddress (String address) {
        setTextField(addressInput, address);
    }

    public void setMetro (String metro) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(metroDropdown));
        driver.findElement(metroDropdown).click();
        String metroStationLocator = String.format(METRO_STATION_PATTERN, metro);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(metroStationLocator)));
        driver.findElement(By.xpath(metroStationLocator)).click();
    }

    public void setMobile (String mobile) {
        setTextField(mobileInput, mobile);
    }

    public void clickNext() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(nextButton));
        driver.findElement(nextButton).click();
    }

    public void selectDeliveryDate(String date){
        setTextField(deliveryDateInput, date);
        driver.findElement(deliveryDateDataPicker).click();
    }

    public void selectDeliveryDuration (String duration) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(deliveryDurationDropdown));
        driver.findElement(deliveryDurationDropdown).click();
        String durationLocator = String.format(ORDER_DURATION_PATTERN, duration);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(durationLocator)));
        driver.findElement(By.xpath(durationLocator)).click();
    }

    public void selectColor(String color) {
        if (!color.equals("")) {
            String colorLocator = String.format(COLOR_PATTERN, color);
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(colorLocator)));
            driver.findElement(By.xpath(colorLocator)).click();
        }
    }

    public void setCommment (String comment) {
        setTextField(commentInput, comment);
    }

    public void clickOrder() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(orderButton));
        driver.findElement(orderButton).click();
    }

    public void clickConfirmOrder() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(confirmOrderButton));
        driver.findElement(confirmOrderButton).click();
    }

    public String getOrderConfirmationText() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(orderConfirmationText));
        return driver.findElement(orderConfirmationText).getText();
    }

    public void fillOrderFormOnFirstPage(String name, String surname, String address, String metroStation, String mobile) {
        setName(name);
        setSurname(surname);
        setAddress(address);
        setMetro(metroStation);
        setMobile(mobile);
    }

    public void fillOrderFormOnSecondPage(String data, String duration, String color, String comment) {
        selectDeliveryDate(data);
        selectDeliveryDuration(duration);
        selectColor(color);
        setCommment(comment);
    }
}
