package test0905;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ThirdTestCase {

    public static void main(String[] args) throws Exception {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        WebDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://www.skillmuni.in/Skillmuni_Prod/skillmuni-login");
        driver.manage().window().maximize();
        Thread.sleep(2000);

        // Login
        driver.findElement(By.id("userId")).sendKeys("healthApp");
        driver.findElement(By.id("password")).sendKeys("healthApp");
        driver.findElement(By.xpath("//button[@class='login-btn']")).click();
        Thread.sleep(3000);

        // Zones to test
        String[][] zones = {
            {"//a[@href='/Skillmuni_Prod/learning-zone']", "Knowledge Knook"},
            {"//a[@href='/Skillmuni_Prod/skill-zone']", "Skill Knook"},
//            {"//a[@href='/Skillmuni_Prod/practice-zone']", "Practice Knook"}
        };

        for (String[] zone : zones) {
            clickEachCategoryInZone(driver, wait, zone[0], zone[1]);
        }

        driver.quit();
    }

    public static void clickEachCategoryInZone(WebDriver driver, WebDriverWait wait, String zoneXPath, String zoneName) throws InterruptedException {
        // Go to the zone
        WebElement seeAllLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(zoneXPath)));
        seeAllLink.click();
        System.out.println("\n✅ Entered " + zoneName);
        Thread.sleep(4000);

        List<WebElement> categories = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));
        System.out.println("📚 [" + zoneName + "] Total categories found: " + categories.size());

        for (int i = 0; i < categories.size(); i++) {
            categories = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));

            WebElement category = categories.get(i);
            String title = category.findElement(By.xpath(".//p[contains(@class,'play-card-title')]")).getText().trim();

            System.out.println("➡️ Clicking on category #" + (i + 1) + ": " + title);

            String urlBeforeClick = driver.getCurrentUrl();

            category.click();
            Thread.sleep(3000);

            String urlAfterClick = driver.getCurrentUrl();

            // Validate that card actually opened a new page
            if (urlBeforeClick.equals(urlAfterClick)) {
                throw new RuntimeException("❌ Category '" + title + "' did not open a new page.");
            } else {
                System.out.println("✅ Opened category page for: " + title);
            }

            driver.navigate().back();
            Thread.sleep(3000);
        }

        // Return to dashboard
        driver.findElement(By.xpath("//a[@href='/Skillmuni_Prod/home']")).click();
        Thread.sleep(3000);
        System.out.println("🔙 Returned to Home Page from " + zoneName);
    }
}
