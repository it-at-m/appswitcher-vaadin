package de.muenchen.oss.appswitcher.vaadin;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

class IntegrationTest {

	WebDriver driver;

	WebDriverManager wdm = WebDriverManager.firefoxdriver().browserInDocker().enableVnc().enableRecording();

//	@BeforeAll
//	static void setupAll() {
//		WebDriverManager.firefoxdriver().browserInDocker().enableVnc().enableRecording();
//	}

	@BeforeEach
	void setUp() {
		driver = wdm.create();
	}

	@AfterEach
	void teardown() {
		driver.quit();
		wdm.quit();
	}

	@Test
	void test() throws Exception {
		driver.get("http://localhost:8080");

		WebElement findElement = driver.findElement(By.id("appswitcher-component"));
		assertThat(findElement).isNotNull();
	}
}
