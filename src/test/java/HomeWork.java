import com.codeborne.selenide.selector.ByAttribute;
import com.google.common.util.concurrent.ClosingFuture;
import helpers.BaseUItest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class HomeWork extends BaseUItest {

    String login = "nemykinats@elfin.tech";
    String password = "FmAftPJdVFGh";

    //задание 1
    @Test
    public void test1(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        driver = new ChromeDriver(options);
        driver.get("https://duckduckgo.com");
        enterToTextArea(driver.findElement(By.xpath("//*[@id=\"search_form_input_homepage\"]")), "отус");
        driver.findElement(By.xpath("//*[@id='search_button_homepage']")).click();
        String result = driver.findElement(By.xpath("//a[@href=\"https://otus.ru/\" and @data-testid=\"result-title-a\"]/span")).getText();
        Assert.assertEquals(result, "Онлайн‑курсы для профессионалов, дистанционное обучение современным ...");
        System.out.println(result);
    }
    private void enterToTextArea(WebElement element, String text){
        element.clear();
        element.sendKeys(text);
    }

    //задание 2
    @Test
    public void test2(){
        driver.get("https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818");
        driver.manage().window().fullscreen();
        String parentHandler = driver.getWindowHandle();

        By image = By.xpath("//img[@src = \"assets/images/p2.jpg\"]");

        JavascriptExecutor je = (JavascriptExecutor)driver;
        je.executeScript("arguments[0].scrollIntoView()", getElement(image));
        je.executeScript("arguments[0].click()", getElement(image));

        Set<String> handles = driver.getWindowHandles(); // не работает
        System.out.println(handles);

        String subWindowHandler = null;

        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()){
            subWindowHandler = iterator.next();
        }
        driver.switchTo().window(subWindowHandler);

        Assert.assertNotEquals(parentHandler, subWindowHandler); // пока тест фейлится всегда
    }

    protected WebElement getElement(By locator){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //задание 3
    @Test
    public void test3(){
        driver.quit();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://otus.ru");
        auth();
        System.out.println(driver.manage().getCookies());
    }
    private void auth(){
        driver.findElement(By.cssSelector("button.header2__auth")).click();
        driver.findElement(By.xpath("//form[@action = '/login/']//input[@name = 'email']")).sendKeys(login);
        driver.findElement(By.xpath("//form[@action = '/login/']//input[@name = 'password']")).sendKeys(password);
        driver.findElement(By.xpath("//form[@action = '/login/']//button[@type = 'submit']")).submit();
    }
}
