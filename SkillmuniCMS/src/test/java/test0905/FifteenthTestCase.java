package test0905;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FifteenthTestCase {
    public static void main(String[] args) throws Exception {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.skillmuni.in/Skillmuni_Prod/login");
        driver.manage().window().maximize();
        Thread.sleep(2000);

        // Login
        driver.findElement(By.id("userId")).sendKeys("Ayaan_HC");
        driver.findElement(By.id("password")).sendKeys("Ayaan_HC");
        driver.findElement(By.className("login-btn")).click();
        Thread.sleep(3000);

        // Navigate to Knowledge Knook -> See All
        driver.findElement(By.xpath("//a[@href='/Skillmuni_Prod/learning-zone']")).click();
        System.out.println("✅ Clicked Knowledge Knook -> See All");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'play-card-content')]")));
        List<WebElement> categoryElements = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));

        for (int i = 0; i < categoryElements.size(); i++) {
            List<WebElement> categories = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));
            if (categories.size() <= i) continue;

            WebElement category = categories.get(i);
            String categoryName = category.getText().trim().split("\n")[0];
            System.out.println("\n▶️ Category " + (i + 1) + ": " + categoryName);
            category.click();
            Thread.sleep(3000);

            try {
                By allCardsLocator = By.xpath("//div[contains(@class, 'cards-container')]/div[contains(@class, 'card')]");
                wait.until(ExpectedConditions.visibilityOfElementLocated(allCardsLocator));
                List<WebElement> cards = driver.findElements(allCardsLocator);

                for (WebElement card : cards) {
                    try {
                        String title = card.findElement(By.xpath(".//p[contains(@class,'card-title')]")).getText().trim();
                        String stage = card.findElement(By.xpath(".//span[contains(@class,'stage-text')]")).getText().trim();
                        System.out.println("📘 Sub-Category: " + title);
                        System.out.println("🧩 Stage: " + stage);

                        card.click();

                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='drag-track']")));
                        System.out.println("✅ 'Drag right to Dive In' is visible");

                        js.executeScript("window.scrollTo(0, 0);");
                        Thread.sleep(1000);
                        for (int s = 0; s < 15; s++) {
                            js.executeScript("window.scrollBy(0, 300);");
                            Thread.sleep(1000);
                        }

                        List<WebElement> briefs = driver.findElements(By.xpath("//div[contains(@class,'scroll-item')]"));
                        System.out.println("📦 Total briefs: " + briefs.size());

                        for (int j = 0; j < briefs.size(); j++) {
                            WebElement brief = briefs.get(j);
                            boolean hasVideo = !brief.findElements(By.xpath(".//iframe[contains(@src,'youtube.com/embed/')]")).isEmpty();
                            boolean isSolved = !brief.findElements(By.xpath(".//button[contains(text(),'You did it!')] | .//div[contains(@class,'drag-text') and contains(text(),'Drag right to Dive In')]")).isEmpty();

                            System.out.println("Brief #" + (j + 1) + " " + (hasVideo ? "🎥 has Video" : "🖼️ No Video") + " - " + (isSolved ? "✅ Solved" : "⏳ Not Solved"));

                            if (hasVideo) {
                                try {
                                	  // Switch to the first YouTube iframe (assuming there's only one per brief)
                                    WebElement iframe = driver.findElement(By.cssSelector("iframe[src*='youtube.com/embed']"));
                                    driver.switchTo().frame(iframe);

                                    // Click the center play button
                                    WebElement playBtn = driver.findElement(By.cssSelector("button[aria-label='Play']"));
                                    playBtn.click();

                                    Thread.sleep(5000); // wait to ensure video starts playing

                                    // Check if Pause button is now available (meaning video is playing)
                                    boolean isPlaying = driver.findElements(By.cssSelector("button[aria-label='Pause']")).size() > 0;

                                    if (isPlaying) {
                                        System.out.println("✅ Video is playing!");
                                    } else {
                                        System.out.println("❌ Video did not play.");
                                    }
                                    driver.switchTo().defaultContent();
                                } catch (Exception e) {
                                    System.out.println("⚠️ Could not check video state: " + e.getMessage());
                                    driver.switchTo().defaultContent();
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("⚠️ Error in subcategory: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("⚠️ Category issue: " + e.getMessage());
            }

            driver.navigate().back();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'play-card-content')]")));
            Thread.sleep(2000);
        }

        System.out.println("\n✅ Test case completed.");
        driver.quit();
    }
}
