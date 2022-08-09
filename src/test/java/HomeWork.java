import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class HomeWork {

    private static String login;
    private static String password;

    private org.apache.logging.log4j.Logger logger = LogManager.getLogger(Logger.class);

    private static WebDriver driver;

    @BeforeAll
    public static void setUp(){

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        login = properties.getProperty("login");
        password = properties.getProperty("password");
    }

    @AfterAll
    public static void setDown(){
        if ( driver != null)
            driver.quit();
    }

    //задание 1
    @Test
    public void testOtusSearching(){
        String URL = "https://duckduckgo.com";
        String search = "отус";
        By field = By.xpath("//*[@id=\"search_form_input_homepage\"]");
        By button = By.xpath("//*[@id='search_button_homepage']");
        By result = By.xpath("//a[@href=\"https://otus.ru/\" and @data-testid=\"result-title-a\"]/span");

        //убрала открытие Chrome в headless режиме

        //ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless");
        //driver = new ChromeDriver(options);

        driver.get(URL);

        enterToTextArea(getElement(field), search);
        getElement(button).click();

        List<WebElement> myElements = driver.findElements(result);
        String firstElement = myElements.get(0).getText();
        Assert.assertEquals(firstElement, "Онлайн‑курсы для профессионалов, дистанционное обучение современным ...");
        System.out.println(firstElement);

    }
    private void enterToTextArea(WebElement element, String text){
        element.clear();
        element.sendKeys(text);
    }

    //задание 2
    @Test
    public void testImageOpening() {
        String URL = "https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/index.html?_ga=2.181802926.889871791.1632394818-2083132868.1632394818";
        By image = By.xpath("//a[@class='image-zoom']");
        By popup = By.xpath("//img[@id = 'fullResImage']");
        String actual = "https://demo.w3layouts.com/demos_new/template_demo/03-10-2020/photoflash-liberty-demo_Free/685659620/web/assets/images/p1.jpg";

        driver.get(URL);
        driver.manage().window().fullscreen();

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));

        webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(popup));
        JavascriptExecutor je = (JavascriptExecutor)driver;

        List<WebElement> images = driver.findElements(image);
        WebElement imageElement = images.get(0);
        je.executeScript("arguments[0].scrollIntoView()", imageElement);
        je.executeScript("arguments[0].click()", imageElement);

        WebElement popupImage = getElement(popup);

        if(popupImage.isEnabled() && popupImage.isDisplayed()) {
            Assert.assertEquals(popupImage.getAttribute("src"), actual);
        }
    }

    protected WebElement getElement(By locator){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //задание 3
    @Test
    public void testOtusAuth(){
        String URL = "https://otus.ru";
        By userName = By.xpath("//p[@class = 'header2-menu__item-text header2-menu__item-text__username']");
        String actual = "Татьяна";

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(URL);
        auth();

        String userNameText = getElement(userName).getText();
        Assert.assertEquals(userNameText, actual);
        logger.info(driver.manage().getCookies());
    }
    private void auth(){
        driver.findElement(By.cssSelector("button.header2__auth")).click();
        WebElement form = driver.findElement(By.xpath("//form[@action = '/login/']"));
        form.findElement(By.xpath(".//input[@name = 'email']")).sendKeys(login);
        form.findElement(By.xpath(".//input[@name = 'password']")).sendKeys(password);
        form.findElement(By.xpath(".//button[@type = 'submit']")).submit();
    }
}
