package test0905;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TenthTestCase {
    public static void main(String[] args) {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        WebDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        boolean case1 = true;
        int[] answers = {3, 2, 4, 1, 1, 2, 3, 1, 4, 2};

        try {
            driver.get("https://www.skillmuni.in/Skillmuni_Prod/login");
            driver.manage().window().maximize();
            System.out.println("Title => " + driver.getTitle());
            System.out.println("URL => " + driver.getCurrentUrl());

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userId"))).sendKeys("healthAppTest");
            driver.findElement(By.id("password")).sendKeys("healthAppTest");
            driver.findElement(By.xpath("//button[@class='login-btn']")).click();

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/Skillmuni_Prod/learning-zone']"))).click();
            System.out.println("Clicked Knowledge Knook -> See All");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'play-card-content')]")));
            List<WebElement> cards = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));
            System.out.println("📚 Total topic cards found: " + cards.size());

            for (int i = 0; i < cards.size(); i++) {
                cards = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));
                if (i >= cards.size()) continue;

                WebElement card = cards.get(i);
                System.out.println("➡️ Clicking on card #" + (i + 1));
                card.click();

                try {
                    WebElement letsGoBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),\"Let's Go\")]")));
                    System.out.println("📝 Pre-assessment screen detected. Clicking 'Let's Go!'");
                    letsGoBtn.click();

                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='question-text']")));
                    List<WebElement> allQuestions = driver.findElements(By.xpath("//div[contains(@class,'question-card')]"));
                    System.out.println("❓ Total questions found: " + allQuestions.size());

                    for (int q = 1; q <= allQuestions.size(); q++) {
                        try {
                            int optionToSelect = case1 ? 1 : (q <= answers.length ? answers[q - 1] : 1);

                            String relativeXPath = "(//div[contains(@class,'question-card')])[" + q + "]//label[" + optionToSelect + "]/input";
//                            System.out.println("👉 Trying XPath: " + relativeXPath);

                            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(relativeXPath)));
                            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", option);
                            wait.until(ExpectedConditions.elementToBeClickable(option));

                            try {
                                option.click();
                            } catch (ElementClickInterceptedException e) {
                                System.out.println("⚠️ Click intercepted, trying JS click");
                                js.executeScript("arguments[0].click();", option);
                            }

                            System.out.println("✅ Answered question #" + q + " with option " + optionToSelect);
                            Thread.sleep(500);
                        } catch (Exception ex) {
                            System.out.println("❌ Could not answer question #" + q + " → " + ex.getMessage());
                        }
                    }

                    try {
                        By continueBtnLocator = By.xpath("//button[@class='submit-btn']");
                        wait.until(ExpectedConditions.elementToBeClickable(continueBtnLocator));
                        WebElement continueBtn = driver.findElement(continueBtnLocator);
                        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", continueBtn);
                        js.executeScript("arguments[0].click();", continueBtn);
                        System.out.println("✅ Clicked on 'Continue' button");
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        System.out.println("❌ Could not click 'Continue' → " + e.getMessage());
                    }

                    // ✅ Skill level and credit detection
                    try {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='level-highlight']")));

                        WebElement levelElement = driver.findElement(By.xpath("//span[@class='level-highlight']"));
                        String levelText = levelElement.getText().trim().toLowerCase();

                        if (levelText.contains("beginner")) {
                            System.out.println("📘 User is at BEGINNER level");
                        } else if (levelText.contains("intermediate")) {
                            System.out.println("📗 User is at INTERMEDIATE level");
                        } else if (levelText.contains("advance")) {
                            System.out.println("📙 User is at ADVANCED level");
                        } else {
                            System.out.println("❓ Skill level not identified: " + levelText);
                        }

                        WebElement creditElement = driver.findElement(By.xpath("//span[@class='score-text']"));
                        String credits = creditElement.getText().trim();
                        System.out.println("🏅 " + credits);

                        // ✅ Optionally click "Start Stage"
                        WebElement startStageBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='stage-btn']")));
                        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", startStageBtn);
                        startStageBtn.click();
                        System.out.println("🚀 Clicked on 'Start Stage'");
                    } catch (Exception ex) {
                        System.out.println("❌ Failed to detect skill level or credits → " + ex.getMessage());
                    }

                } catch (Exception e) {
                    System.out.println("✅ No pre-assessment found or already completed for this card.");
                }

                driver.navigate().back();
                Thread.sleep(1500);
            }

        } catch (Exception e) {
            System.out.println("🚫 Error occurred: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
