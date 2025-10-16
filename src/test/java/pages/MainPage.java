package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class MainPage {
    private final WebDriver driver;

    // Кнопка "Заказать" в верхней части страницы
    private final By orderButtonTop = By.xpath("//button[@class='Button_Button__ra12g' and text()='Заказать']");

    // Кнопка "Заказать" в нижней части страницы
    private final By orderButtonBottom = By.xpath("//button[@class='Button_Button__ra12g Button_Middle__1CSJM' and text()='Заказать']");

    // Кнопка принятия cookie
    private final By cookieButton = By.id("rcc-confirm-button");

    // Вопросы в разделе FAQ (динамический локатор)
    // Используется: accordion__heading-{index}

    // Ответы в разделе FAQ (динамический локатор)
    // Используется: accordion__panel-{index}

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickOrderButtonTop() {
        driver.findElement(orderButtonTop).click();
    }

    public void clickOrderButtonBottom() {
        WebElement element = driver.findElement(orderButtonBottom);
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public void closeCookieBanner() {
        try {
            WebElement cookie = driver.findElement(cookieButton);
            if (cookie.isDisplayed()) {
                cookie.click();
            }
        } catch (Exception e) {
            // Баннер с куки может отсутствовать
        }
    }

    public void expandFaqQuestion(int index) {
        WebElement element = driver.findElement(By.id("accordion__heading-" + index));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", element);
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public String getFaqAnswerText(int index) {
        WebElement element = driver.findElement(By.id("accordion__panel-" + index));
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOf(element));
        return element.getText();
    }
}