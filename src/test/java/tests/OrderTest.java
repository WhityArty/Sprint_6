package tests;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pages.MainPage;
import pages.OrderPage;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderTest extends BaseTest {

    private static Stream<Arguments> provideOrderTestData() {
        return Stream.of(
                Arguments.of(
                        "top",
                        "Иван",
                        "Петров",
                        "ул. Ленина, д. 1",
                        "Любая станция",
                        "+79991234567",
                        "25.10.2025",
                        "трое суток",
                        "black",
                        "Тестовый комментарий"
                ),
                Arguments.of(
                        "bottom",
                        "Мария",
                        "Сидорова",
                        "пр. Мира, д. 10",
                        "Любая станция",
                        "+79997654321",
                        "29.10.2025",
                        "пятеро суток",
                        "grey",
                        "Другой комментарий"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideOrderTestData")
    public void orderScooterPositiveTest(String entryPoint, String name, String lastName,
                                         String address, String metro, String phone,
                                         String date, String period, String color,
                                         String comment) {

        String browser = isChrome() ? "Chrome" : "Firefox";
        System.out.println("Браузер: " + browser);

        MainPage mainPage = new MainPage(driver);
        OrderPage orderPage = new OrderPage(driver);

        mainPage.closeCookieBanner();

        // Выбираем точку входа
        if ("top".equals(entryPoint)) {
            mainPage.clickOrderButtonTop();
        } else {
            mainPage.clickOrderButtonBottom();
        }

        // Заполняем первую страницу заказа
        orderPage.fillFirstStep(name, lastName, address, metro, phone);

        // Заполняем вторую страницу заказа
        orderPage.fillSecondStep(date, period, color, comment);

        // Подтверждаем заказ
        orderPage.confirmOrder();

        // Проверяем успешное оформление
        boolean success = orderPage.isSuccessMessageDisplayed();

        try {
            // Единая проверка для всех браузеров
            assertTrue(success, "Заказ не был оформлен успешно. Сообщение об успешном оформлении не появилось.");
            System.out.println("ТЕСТ ПРОЙДЕН: Заказ успешно оформлен в " + browser);

        } catch (AssertionError e) {
            // Детализируем ошибку с информацией о браузере
            String errorDetails = getErrorDetails(browser);
            System.out.println("ТЕСТ НЕ ПРОЙДЕН: " + errorDetails);
            throw e;
        }
    }

    private String getErrorDetails(String browser) {
        if ("Chrome".equals(browser)) {
            return "В Chrome обнаружен баг: невозможно оформить заказ";
        } else {
            return "В Firefox заказ не оформляется";
        }
    }
}