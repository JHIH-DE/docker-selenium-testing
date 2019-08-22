import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class IKMTest  extends BaseGridTest{
	@Test
	public void iKmTdsTest() throws Exception {
		StringBuffer verificationErrors = new StringBuffer();

		RemoteWebDriver driver = getDriver();

		String browserName = getBrowser();

		driver.get("https://castst.walsin.com/cas/login?service=http://local.walsin.com:28080/xinzhuang-ikm/v1/shiro-cas");

		// Locate the search input box and input "Testing Search"

		Reporter.log(String.format("iKM Login Test Started %s",Thread.currentThread().getId()),true);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("ur07104");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("1qaz2wsx!");
		driver.findElement(By.name("submitForm")).click();
		Reporter.log(String.format("iKM Login Test Ended %s",Thread.currentThread().getId()),true);

		driver.navigate().to("http://local.walsin.com:28080/xinzhuang-ikm/#/tds");

		Reporter.log(String.format("iKM Search Test Started %s",Thread.currentThread().getId()),true);
		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			WebElement searchInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mat-input-0")));
			searchInput.sendKeys("PL400-70D");
			driver.findElement(By.id("search-tds-button")).click();
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}


		try {
			WebDriverWait wait = new WebDriverWait(driver, 20);
			WebElement searchResult = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("tdsMain-model-0")));
			Assert.assertEquals("PL400-70D", searchResult.getText());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
		Reporter.log(String.format("iKM Search Test Ended %s",Thread.currentThread().getId()),true);

		pressEsc();
		takePhotosOnResult(driver, browserName, "searchResult");


		Reporter.log(String.format("iKM Edit Test Started %s",Thread.currentThread().getId()),true);
		driver.findElement(By.id("add-tds-button")).click();

		takePhotosOnView(driver, browserName, "tdsViewEditDialog");

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

		pressEsc();

		try {
			driver.findElement(By.id("mat-dialog-submit")).click();
			Thread.sleep(1 * 1000);
			WebDriverWait wait = new WebDriverWait(driver, 20);
			WebElement result = wait.until(ExpectedConditions.presenceOfElementLocated((By.tagName("app-alert-message"))));
			Assert.assertEquals("check_circle", result.getText().split(" ")[0]);
			takePhotosOnResult(driver, browserName, "successCreate");

			driver.findElement(By.tagName("app-alert-message")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Reporter.log(String.format("iKM Edit Test Ended %s",Thread.currentThread().getId()),true);

		System.out.print("verificationErrors: " + verificationErrors.toString());
		driver.findElement(By.id("mat-dialog-close")).click();
	}
}
