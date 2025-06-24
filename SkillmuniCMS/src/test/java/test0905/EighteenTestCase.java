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

public class EighteenTestCase {
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

		driver.findElement(By.id("userId")).sendKeys("Ayaan_HC");
		driver.findElement(By.id("password")).sendKeys("Ayaan_HC");
		driver.findElement(By.className("login-btn")).click();
		Thread.sleep(3000);

		driver.findElement(By.xpath("//a[@href='/Skillmuni_Prod/learning-zone']")).click();
		System.out.println("\u2705 Clicked Knowledge Knook -> See All");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'play-card-content')]")));
		List<WebElement> categoryElements = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));

		for (int i = 0; i < categoryElements.size(); i++) {
			List<WebElement> categories = driver.findElements(By.xpath("//div[contains(@class,'play-card-content')]"));
			if (categories.size() <= i) continue;

			WebElement category = categories.get(i);
			String categoryName = category.getText().trim().split("\n")[0];
			System.out.println("\n\u25B6\uFE0F Category " + (i + 1) + ": " + categoryName);

			js.executeScript("arguments[0].scrollIntoView(true);", category);
			Thread.sleep(1000);
			js.executeScript("arguments[0].click();", category);
			Thread.sleep(3000);

			try {
				By allCardsLocator = By.xpath("//div[contains(@class, 'cards-container')]/div[contains(@class, 'card')]");
				wait.until(ExpectedConditions.visibilityOfElementLocated(allCardsLocator));
				List<WebElement> cards = driver.findElements(allCardsLocator);

				for (int c = 0; c < cards.size(); c++) {
					cards = driver.findElements(allCardsLocator);
					WebElement card = cards.get(c);
					try {
						String title = card.findElement(By.xpath(".//p[contains(@class,'card-title')]"))
								.getText().trim();
						String stage = card.findElement(By.xpath(".//span[contains(@class,'stage-text')]"))
								.getText().trim();
						System.out.println("\uD83D\uDCDA Sub-Category: " + title);
						System.out.println("\uD83E\uDD29 Stage: " + stage);

						js.executeScript("arguments[0].scrollIntoView(true);", card);
						Thread.sleep(500);
						js.executeScript("arguments[0].click();", card);

						wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='drag-track']")));
						System.out.println("\u2705 'Drag right to Dive In' is visible");

						js.executeScript("window.scrollTo(0, 0);");
						Thread.sleep(1000);
						for (int s = 0; s < 15; s++) {
							js.executeScript("window.scrollBy(0, 300);");
							Thread.sleep(500);
						}

						List<WebElement> briefs = driver.findElements(By.xpath("//div[contains(@class,'scroll-item')]"));
						System.out.println("\uD83D\uDCE6 Total briefs: " + briefs.size());

						for (int j = 0; j < briefs.size(); j++) {
							briefs = driver.findElements(By.xpath("//div[contains(@class,'scroll-item')]"));
							WebElement brief = briefs.get(j);

							boolean hasVideo = !brief.findElements(By.xpath(".//iframe[contains(@src,'youtube.com/embed/')]"))
									.isEmpty();
							boolean isSolved = !brief.findElements(By.xpath(
									".//button[contains(text(),'You did it!')] | .//div[contains(@class,'drag-text') and contains(text(),'Drag right to Dive In')]"))
									.isEmpty();

							System.out.println("Brief #" + (j + 1) + " " + (hasVideo ? "\uD83C\uDFA5 has Video" : "\uD83D\uDDBC️ No Video")
									+ " - " + (isSolved ? "\u2705 Solved" : "\u23F3 Not Solved"));

							if (hasVideo) {
								try {
									WebElement iframe = brief.findElement(By.xpath(".//iframe[contains(@src,'youtube.com/embed/')]"));
									js.executeScript(
										"var iframe = arguments[0]; if (!iframe.src.includes('enablejsapi=1')) { iframe.src += (iframe.src.includes('?') ? '&' : '?') + 'enablejsapi=1'; }",
										iframe);
									js.executeScript("arguments[0].scrollIntoView(true);", iframe);
									Thread.sleep(1000);

									js.executeScript(
										"var iframe = arguments[0];"
												+ "iframe.contentWindow.postMessage(JSON.stringify({event: 'command', func: 'mute', args: []}), '*');"
												+ "iframe.contentWindow.postMessage(JSON.stringify({event: 'command', func: 'playVideo', args: []}), '*');",
										iframe);
									Thread.sleep(4000);

									WebElement freshIframe = brief.findElement(By.xpath(".//iframe[contains(@src,'youtube.com/embed/')]"));
									js.executeScript(
										"var iframe = arguments[0];"
												+ "iframe.contentWindow.postMessage(JSON.stringify({event: 'command', func: 'pauseVideo', args: []}), '*');",
										freshIframe);
									Thread.sleep(2000);
									System.out.println("\u23F8\uFE0F Pause command sent");
								} catch (Exception e) {
									System.out.println("\u26A0\uFE0F Video control issue: " + e.getMessage());
								} finally {
									try {
										driver.switchTo().defaultContent();
									} catch (Exception ignored) {}
									Thread.sleep(1000);
								}
							}
						}
					} catch (Exception e) {
						System.out.println("\u26A0\uFE0F Error in subcategory: " + e.getMessage());
					}
				}
			} catch (Exception e) {
				System.out.println("\u26A0\uFE0F Category issue: " + e.getMessage());
			}

			driver.navigate().back();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'play-card-content')]")));
			Thread.sleep(2000);
		}

		System.out.println("\n\u2705 Test case completed.");
		driver.quit();
	}
}
