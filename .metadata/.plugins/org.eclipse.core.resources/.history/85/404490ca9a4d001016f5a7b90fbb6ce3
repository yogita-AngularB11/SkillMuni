package test0905;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class ThirteenTestCase {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        WebDriver driver = new ChromeDriver(options);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        boolean case1 = false;
        int[][] allCategoryAnswers = {
            {3, 3, 4, 1, 2, 4, 3, 3, 3, 3}, //Communication
            {2, 3, 3, 3, 3, 3, 3, 3, 3, 3}, //Compliance Ready 
            {2, 3, 2, 3, 3, 3, 2, 3, 3, 3}, //Customer Focus 
            {3, 2, 4, 3, 3, 3, 2, 3, 3, 2}, //Data Accuracy
            {2, 1, 3, 3, 1, 2, 3, 3, 2, 3}, //Digital Fluency 
            {3, 3, 3, 3, 3, 2, 2, 2, 3, 3}, //Domain Knowledge 
            {3, 2, 3, 3, 2, 3, 2, 3, 3, 3}, //Empathy and EQ 
            {3, 3, 3, 2, 3, 3, 2, 3, 2, 3}  //Problem Solving  
        };

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
            List<WebElement> categories = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));
            System.out.println("📚 Total categories found: " + categories.size());

            for (int i = 0; i < categories.size(); i++) {
                categories = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));
                if (i >= categories.size()) continue;

                WebElement category = categories.get(i);
                String categoryTitle = category.getText().trim();
                System.out.println("➡️ Clicking on category #" + (i + 1) + " - Title: " + categoryTitle);
                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", category);
                category.click();

                try {
                    List<WebElement> letsGoBtns = driver.findElements(By.xpath("//button[contains(text(),\"Let's Go\")]"));
                    if (!letsGoBtns.isEmpty()) {
                        System.out.println("📝 Pre-assessment detected. Starting...");
                        letsGoBtns.get(0).click();
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='question-text']")));

                        List<WebElement> allQuestions = driver.findElements(By.xpath("//div[contains(@class,'question-card')]"));
                        System.out.println("❓ Total questions found: " + allQuestions.size());

                        int[] answers = allCategoryAnswers[i];
                        for (int q = 1; q <= allQuestions.size(); q++) {
                            int optionToSelect = case1 ? 1 : (q <= answers.length ? answers[q - 1] : 1);
                            String relativeXPath = "(//div[contains(@class,'question-card')])[" + q + "]//label[" + optionToSelect + "]/input";
                            WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(relativeXPath)));

                            js.executeScript("arguments[0].scrollIntoView({block: 'center'});", option);
                            try {
                                option.click();
                            } catch (ElementClickInterceptedException e) {
                                js.executeScript("arguments[0].click();", option);
                            }
                            Thread.sleep(500);
                        }

                        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='submit-btn']")));
                        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", continueBtn);
                        js.executeScript("arguments[0].click();", continueBtn);
                        System.out.println("✅ Clicked on 'Continue' after pre-assessment");
                    }

                    WebElement startStageBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@class='stage-btn']")));
                    js.executeScript("arguments[0].scrollIntoView({block: 'center'});", startStageBtn);
                    startStageBtn.click();
                    System.out.println("🚀 Clicked on 'Start Stage'");
                    Thread.sleep(1500);

                    // Log stage-level summary details
                    WebElement quizzes = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'quiz-summary')]/div[1]")));
                    WebElement totalCredits = driver.findElement(By.xpath("//div[contains(@class,'quiz-summary')]/div[2]"));
                    System.out.println("📊 " + quizzes.getText().trim());
                    System.out.println("🏅 " + totalCredits.getText().trim());

                    List<WebElement> stages = driver.findElements(By.xpath("//div[@class='brief-stage-container']"));
                    for (WebElement stage : stages) {
                        String stageTitle = stage.findElement(By.xpath(".//h3")).getText();
                        List<WebElement> subCards = stage.findElements(By.xpath(".//div[@class='brief-card']"));
                        int totalCreditsInStage = 0, solved = 0, totalBriefs = 0;

                        for (WebElement card : subCards) {
                            String solvedBriefsText = card.findElement(By.xpath(".//div[@class='brief-counter']")).getText();
                            String[] split = solvedBriefsText.replace("SOLVED", "").trim().split("/");
                            if (split.length == 2) {
                                solved += Integer.parseInt(split[0].trim());
                                totalBriefs += Integer.parseInt(split[1].trim());
                            }

                            try {
                                String creditText = card.findElement(By.xpath(".//div[@class='brief-credits']")).getText();
                                int credits = Integer.parseInt(creditText.replaceAll("[^0-9]", ""));
                                totalCreditsInStage += credits;
                            } catch (Exception ignored) {}
                        }

                        System.out.println("📘 Stage: " + stageTitle + " | Briefs: " + solved + "/" + totalBriefs + " | Credits: " + totalCreditsInStage);
                    }

                } catch (Exception e) {
                    System.out.println("✅ No pre-assessment found or already completed for this category.");
                }

                driver.navigate().back();
                Thread.sleep(2000);
            }

        } catch (Exception e) {
            System.out.println("❌ Error occurred: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
