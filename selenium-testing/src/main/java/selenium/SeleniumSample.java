package selenium;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.xmlpull.v1.XmlPullParserException;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SeleniumSample {
	public static void main(String[] args){
		StringBuffer verificationErrors = new StringBuffer();

		WebDriver driver = null;

		System.setProperty("webdriver.chrome.driver", "/Users/jason_li/Documents/chromedriver");

		// Launch Chrome browser
		driver = new ChromeDriver(DesiredCapabilities.chrome());

		// Chrome Mobile Emulator testing with specific phone resoultion
//		Map<String, String> mobileEmulation = new HashMap<>();
//		mobileEmulation.put("deviceName", "iPhone 6/7/8");
//
//		ChromeOptions chromeOptions = new ChromeOptions(); chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
//
//		driver = new ChromeDriver(chromeOptions);

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		//Visit xinzhuang-ikm
		driver.get("https://castst.walsin.com/cas/login?service=http://local.walsin.com:28080/xinzhuang-ikm/v1/shiro-cas");

		// Locate the search input box and input "Testing Search"

		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("ur07104");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("1qaz2wsx!");
		driver.findElement(By.name("submitForm")).click();

		driver.get("http://local.walsin.com:28080/xinzhuang-ikm/#/tds");

		driver.findElement(By.id("mat-input-0")).clear();
		driver.findElement(By.id("mat-input-0")).sendKeys("PL400-70D");
		driver.findElement(By.id("search-tds-button")).click();

		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			WebElement searchResult = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tdsMain-model-0")));
			Assert.assertEquals("PL400-70D", searchResult.getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}

		pressEsc(driver);
		snapshot(driver,"searchResult");

		driver.findElement(By.id("add-tds-button")).click();

		driver.findElement(By.id("mat-input-1")).clear();
		driver.findElement(By.id("mat-input-1")).sendKeys("testTds");

		driver.findElement(By.id("mat-input-2")).clear();
		driver.findElement(By.id("mat-input-2")).sendKeys("科茂");

		driver.findElement(By.id("mat-input-4")).clear();
		driver.findElement(By.id("mat-input-4")).sendKeys("TMPTMA");

		driver.findElement(By.id("mat-input-5")).clear();
		driver.findElement(By.id("mat-input-5")).sendKeys("TMPTMA");
		driver.findElement(By.id("mat-input-5")).sendKeys(Keys.ENTER);

		driver.findElement(By.id("tdsCat-selector")).click();
		driver.findElement(By.id("tdsCat-option-0")).click();
		driver.findElement(By.id("tdsClass-selector")).click();
		driver.findElement(By.id("tdsClass-option-0")).click();
		driver.findElement(By.id("tdsFunction-selector")).click();
		driver.findElement(By.id("tdsFunction-option-0")).click();
		driver.findElement(By.id("tdsFunction-option-1")).click();

		pressEsc(driver);

		try {
			driver.findElement(By.id("mat-dialog-submit")).click();
			Thread.sleep(1 * 1000);
			WebDriverWait wait = new WebDriverWait(driver, 20);
			WebElement result = wait.until(ExpectedConditions.presenceOfElementLocated((By.tagName("app-alert-message"))));
			Assert.assertEquals("check_circle", result.getText().split(" ")[0]);
			snapshot(driver,"successCreate");
			driver.findElement(By.id("mat-dialog-close")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.print("verificationErrors: " + verificationErrors.toString());
		driver.quit();
	}


	public static void pressEsc(WebDriver driver) {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).build().perform();
	}

	public static void snapshot(WebDriver driver,String fileName) {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File("snapshot/"+fileName+".png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
