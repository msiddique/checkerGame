package com.checkers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckersTest {
	WebDriver driver;
	String url = "https://www.gamesforthebrain.com/game/checkers/";

	@BeforeAll
	public void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
		driver.get(url);
	}

	@Test
	public void isSiteUpTest() {
		String expectedTitle = "Checkers - Games for the Brain";
		assertEquals(expectedTitle, driver.getTitle());
	}

	@Test
	public void gamePlayTest() throws InterruptedException {
		// Make 1st move
		makeMove("space62", "space73");
		// Make 2nd move
		waitForTurn();
		makeMove("space22", "space33");
		// Make 3rd move
		waitForTurn();
		makeMove("space11", "space22");
		// Make 4th move
		waitForTurn();
		makeMove("space22", "space04");
		// Make 5th move
		waitForTurn();
		makeMove("space02", "space24");
		// Reset Game
		restartGame();
		assertTrue(verifyGameIsReset());
	}

	public void waitForTurn() throws InterruptedException {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Thread.sleep(3000);
		WebElement moveStatus = driver.findElement(By.id("message"));
		String message = "Make a move.";
		wait.until(ExpectedConditions.textToBePresentInElement(moveStatus, message));

	}

	public void makeMove(String from, String to) {
		WebElement currentPosition = driver.findElement(By.name(from));
		WebElement newPosition = driver.findElement(By.name(to));
		currentPosition.click();
		newPosition.click();
	}

	public void restartGame() {
		WebElement restartLink = driver.findElement(By.linkText("Restart..."));
		restartLink.click();
	}


	public boolean verifyGameIsReset() {
		boolean reset = true;
		WebElement row4 = driver.findElement(By.cssSelector("#board .line:nth-of-type(4)"));
		WebElement row5 = driver.findElement(By.cssSelector("#board .line:nth-of-type(5)"));

		List<WebElement> expectedEmptySpaces = new ArrayList<>();

		expectedEmptySpaces.addAll(row4.findElements(By.tagName("img")));
		expectedEmptySpaces.addAll(row5.findElements(By.tagName("img")));
		for (WebElement space : expectedEmptySpaces) {
			String imageName = space.getAttribute("src");
			if (!(imageName.endsWith("black.gif") | imageName.endsWith("gray.gif"))) {
				reset = false;
				break;
			}
		}
		return reset;
	}

	@AfterAll
	public void tearDown() {
		driver.quit();
	}

}
