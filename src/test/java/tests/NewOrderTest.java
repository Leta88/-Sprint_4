package tests;

import ru.yandex.pageobject.MainPage;
import ru.yandex.pageobject.OrderPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;


@RunWith(Parameterized.class)
public class NewOrderTest extends BaseUITest{

    private final int orderButtonVersion;
    private final String name;
    private final String surname;
    private final String address;
    private final String metroStation;
    private final String mobile;
    private final int addDaysToCurrentDate;
    private final String duration;
    private final String color;
    private final String comment;


    public NewOrderTest (int orderButtonVersion, String name, String surname, String address, String metroStation, String mobile, int addDaysToCurrentDate, String duration, String color, String comment){
        this.orderButtonVersion = orderButtonVersion;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metroStation = metroStation;
        this.mobile = mobile;
        this.addDaysToCurrentDate = addDaysToCurrentDate;
        this.duration = duration;
        this.color = color;
        this.comment = comment;
    }

    @Parameterized.Parameters
    public static Object[][] getSumData() {
        return new Object[][] {
                // параметры:
                //Какую используем кнопку для начала заказа (верхняя или нижняя), Имя, Фамилия, Адрес, Метро, Номер телефона, Сколько прибавить дней к текущей дате для даты заказа, Длительность заказа, Цвет (можно пустой), Комментарий (можно пустой)

                { 1, "Петя", "Иванов", "улица Ленина дом 2, кв 3", "Бульвар Рокоссовского",  "+79095554433", 5, "двое суток", "чёрный жемчуг", "" },
                { 2, "Маша", "Кузнецова", "красная площадь, дом 1", "Сокольники", "89003332211", 1, "четверо суток", "", "Не звоните пожалуйста" },
        };
    }


    @Test
    public void CreateNewOrder() {

        String deliveryData = LocalDate.now().plusDays(addDaysToCurrentDate).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        MainPage mainPage = new MainPage(driver);
        mainPage.openMainPage();
        mainPage.confirmCookiesUsage();
        mainPage.clickNewOrder(orderButtonVersion);

        OrderPage orderPage = new OrderPage(driver);
        orderPage.fillOrderFormOnFirstPage(name, surname, address, metroStation, mobile);
        orderPage.clickNext();

        orderPage.fillOrderFormOnSecondPage(deliveryData, duration, color, comment);
        orderPage.clickOrder();
        orderPage.clickConfirmOrder();

        String confirmationText = orderPage.getOrderConfirmationText();
        Pattern pattern = Pattern.compile("^Номер заказа:\\s*\\d+\\.\\s*Запишите его:\\s*пригодится, чтобы отслеживать статус", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(confirmationText);

        assertTrue("Отсутствует сообщение о подтверждении заказа в формате: 'Номер заказа: <число>. Запишите его: пригодится, чтобы отслеживать статус'",
                matcher.find());
    }
}
