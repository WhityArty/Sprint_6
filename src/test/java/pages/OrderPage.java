package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class OrderPage {
    private final WebDriver driver;

    // Локаторы для страницы заказа
    //Имя
    private final By nameField = By.xpath(".//input[@placeholder='* Имя']");
    //Фамилия
    private final By lastNameField = By.xpath(".//input[@placeholder='* Фамилия']");
    //Адрес
    private final By addressField = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");
    //Станция метро
    private final By metroField = By.xpath(".//input[@placeholder='* Станция метро']");
    //Телефон
    private final By phoneField = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");
    //Далее
    private final By nextButton = By.xpath(".//button[text()='Далее']");
    //Когда привезти самокат
    private final By dateField = By.xpath(".//input[@placeholder='* Когда привезти самокат']");
    //Период аренды
    private final By rentalPeriod = By.className("Dropdown-placeholder");
    //Цвет Черный жемчуг
    private final By colorBlack = By.id("black");
    //Цвет Серая безысходность
    private final By colorGrey = By.id("grey");
    //Комментарий
    private final By commentField = By.xpath(".//input[@placeholder='Комментарий для курьера']");

    //Локатор для кнопки "Заказать" на втором шаге
    private final By orderButton = By.xpath(".//div[contains(@class, 'Order_Buttons')]//button[text()='Заказать']");

    private final By confirmButton = By.xpath(".//button[text()='Да']");
    private final By successMessage = By.xpath(".//div[contains(text(), 'Заказ оформлен')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    public void fillFirstStep(String name, String lastName, String address, String metro, String phone) {
        driver.findElement(nameField).sendKeys(name);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(addressField).sendKeys(address);
        driver.findElement(phoneField).sendKeys(phone);

        //к выбору метро
        fillMetroField();

        // Нажимаем кнопку "Далее"
        driver.findElement(nextButton).click();
    }

    private void fillMetroField() {
        try {
            WebElement metroInput = driver.findElement(metroField);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", metroInput);
            metroInput.click();

            // Ждем появления списка
            Thread.sleep(2000);

            // Выбираем первую доступную станцию метро
            WebElement firstMetroOption = driver.findElement(By.xpath("//button[contains(@class, 'select-search__option')]"));
            firstMetroOption.click();

            System.out.println("Выбрана первая доступная станция метро");
        } catch (Exception e) {
            System.out.println("Выбор метро не сработал: " + e.getMessage());
        }
    }

    public void fillSecondStep(String date, String period, String color, String comment) {
        // Ждем загрузки второго шага
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(dateField));

        // Заполняем дату
        WebElement dateInput = driver.findElement(dateField);
        dateInput.clear();
        dateInput.sendKeys(date);
        dateInput.sendKeys(org.openqa.selenium.Keys.ENTER);

        // Выбираем период аренды
        WebElement periodDropdown = driver.findElement(rentalPeriod);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", periodDropdown);
        periodDropdown.click();

        // Ждем появления вариантов периода и выбираем нужный
        By periodOption = By.xpath("//div[@class='Dropdown-menu']/div[text()='" + period + "']");
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(periodOption));
        driver.findElement(periodOption).click();

        // Выбираем цвет
        if ("black".equals(color)) {
            WebElement blackCheckbox = driver.findElement(colorBlack);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", blackCheckbox);
            blackCheckbox.click();
        } else {
            WebElement greyCheckbox = driver.findElement(colorGrey);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", greyCheckbox);
            greyCheckbox.click();
        }

        // Заполняем комментарий
        if (comment != null && !comment.isEmpty()) {
            driver.findElement(commentField).sendKeys(comment);
        }

        // Нажимаем кнопку "ЗАКАЗАТЬ"
        clickOrderButton();
    }

    private void clickOrderButton() {
        try {
            // Ждем пока кнопка станет кликабельной
            WebElement orderBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(orderButton));

            // Скроллим к кнопке
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", orderBtn);

            try {
                orderBtn.click();
                System.out.println("Кнопка 'Заказать' нажата обычным кликом");
                return;
            } catch (Exception e) {
                System.out.println("Обычный клик на кнопку 'Заказать' не сработал: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Ошибка при нажатии кнопки 'Заказать': " + e.getMessage());
            throw e;
        }
    }

    public void confirmOrder() {
        try {
            // Ждем появления модального окна подтверждения
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[contains(@class, 'Order_Modal')]")));

            // Нажимаем кнопку "Да" в модальном окне
            WebElement confirmBtn = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(confirmButton));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", confirmBtn);
            confirmBtn.click();

            System.out.println("Кнопка 'Да' в модальном окне нажата");

        } catch (Exception e) {
            System.out.println("Ошибка при подтверждении заказа: " + e.getMessage());
            throw e;
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            // Ждем появления сообщения об успехе
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfElementLocated(successMessage));

            WebElement message = driver.findElement(successMessage);
            boolean isDisplayed = message.isDisplayed();
            System.out.println("Сообщение об успешном заказе отображается: " + isDisplayed);

            if (isDisplayed) {
                System.out.println("Текст сообщения: " + message.getText());
            }

            return isDisplayed;
        } catch (Exception e) {
            System.out.println("Сообщение об успешном заказе не появилось: " + e.getMessage());
            return false;
        }
    }
}