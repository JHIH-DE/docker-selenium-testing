package seleniumgrid;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SeleniumGridSample {
	public static void main(String[] args) {
		//testMethod();
		//mainMethod();
	}

	private static void testMethod() {
		try {
			RemoteWebDriver chrome = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
			RemoteWebDriver firefox = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.firefox());
			// run against chrome
			chrome.get("https://www.google.com/");
			System.out.println(chrome.getTitle());

			takePhotosOnView(chrome, "chrome", "home");

			// run against firefox
			firefox.get("https://www.google.com/");
			System.out.println(firefox.getTitle());

			takePhotosOnView(firefox, "firefox", "home");

			File[] files = new File("/Users/jason_li/Documents/selenimu.project/snapshot").listFiles();
			folderUploader(files);

			chrome.quit();
			firefox.quit();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private static void mainMethod() {
		try {
			RemoteWebDriver chromeikm = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
			testProcess(chromeikm, "chrome");

			RemoteWebDriver firefoxikm = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.firefox());
			testProcess(firefoxikm, "firefox");

			File[] files = new File("/Users/jason_li/Documents/selenimu.project/snapshot").listFiles();
			folderUploader(files);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static void snapshot(WebDriver driver, String directoryName, String fileName, String actionNmae) {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String file_name = fileName + "_" + actionNmae;
		//System.currentTimeMillis();  系統時間

		String main_directory_main = "snapshot";
		File main_directory = new File(main_directory_main);
		if (!main_directory.exists()) {
			main_directory.mkdir();
		}

		File directory = new File(main_directory_main + "/" + directoryName);
		if (!directory.exists()) {
			directory.mkdir();
		}
		try {
			FileUtils.copyFile(scrFile, new File(main_directory_main + "/" + directoryName + "/" + file_name + ".png"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void fileUploader(String sourceFolderPath) throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {
		try {
			// Create a minioClient with the MinIO Server name, Port, Access key and Secret key.
			MinioClient minioClient = new MinioClient("http://10.190.254.109:9001", "selenium_minio", "selenium_miniopw");

			// Check if the bucket already exists.
			boolean isExist = minioClient.bucketExists("selenium");
			if (isExist) {
				//System.out.println("Bucket already exists.");
			} else {
				// Make a new bucket called asiatrip to hold a zip file of photos.
				minioClient.makeBucket("selenium");
			}

			// Upload the zip file to the bucket with putObject
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat timeFormat = new SimpleDateFormat("HH");
			Date date = new Date();
			minioClient.putObject("selenium", dateFormat.format(date) + "/" + timeFormat.format(date) + "/" + sourceFolderPath, "/Users/jason_li/Documents/selenimu.project/snapshot/" + sourceFolderPath);
			System.out.println("/selenimu.project/" + sourceFolderPath + " is successfully uploaded as " + sourceFolderPath + " to `selenium` bucket.");
		} catch (MinioException e) {
			System.out.println("Error occurred: " + e);
		}

	}

	public static void folderUploader(File[] files) {
		for (File file : files) {
			if (file.isDirectory()) {
				folderUploader(file.listFiles()); // Calls same method again.
			} else {
				if (!file.getName().equals(".DS_Store")) {
					try {
						fileUploader(file.getParentFile().getName() + "/" + file.getName());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void pressEsc(WebDriver driver) {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).build().perform();
	}

	public static void refresh(WebDriver driver) {
		driver.navigate().refresh();
	}

	public static void takePhotosOnView(RemoteWebDriver driver, String browserName, String actionName) {

		try{
			Dimension d = new Dimension(640, 480);
			driver.manage().window().setSize(d);
			Thread.sleep(1 * 1000);
			snapshot(driver, "640x480", browserName, actionName);

			d = new Dimension(800, 600);
			driver.manage().window().setSize(d);
			//refresh(driver);
			Thread.sleep(1 * 1000);
			snapshot(driver, "800x480", browserName, actionName);

			d = new Dimension(1024, 768);
			driver.manage().window().setSize(d);
			//refresh(driver);
			Thread.sleep(1 * 1000);
			snapshot(driver, "1024x768", browserName, actionName);

			d = new Dimension(1600, 1200);
			driver.manage().window().setSize(d);
			//refresh(driver);
			Thread.sleep(1 * 1000);
			snapshot(driver, "1600x1200", browserName, actionName);
		}
		catch (Exception e){
			e.printStackTrace();
		}

	}

	public static void takePhotosOnResult(RemoteWebDriver driver, String browserName, String actionName) {
		Dimension d = new Dimension(1024, 768);
		driver.manage().window().setSize(d);
		snapshot(driver, "result", browserName, actionName);
	}

	private static void testProcess(RemoteWebDriver driver, String browserName) {
		StringBuffer verificationErrors = new StringBuffer();

		driver.get("https://castst.walsin.com/cas/login?service=http://local.walsin.com:28080/xinzhuang-ikm/v1/shiro-cas");

		// Locate the search input box and input "Testing Search"

		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("ur07104");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("1qaz2wsx!");
		driver.findElement(By.name("submitForm")).click();

		//driver.get("http://ikmdev.walsin.com:28080/#/tds");
		driver.get("http://local.walsin.com:28080/xinzhuang-ikm/#/tds");

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

		pressEsc(driver);
		takePhotosOnResult(driver, browserName, "searchResult");

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

		pressEsc(driver);

		try {
			driver.findElement(By.id("mat-dialog-submit")).click();
			Thread.sleep(1 * 1000);
			WebDriverWait wait = new WebDriverWait(driver, 20);
			//WebElement searchResult = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='新增'])[1]/following::snack-bar-container[1]")));
			WebElement result = wait.until(ExpectedConditions.presenceOfElementLocated((By.tagName("app-alert-message"))));
			Assert.assertEquals("check_circle", result.getText().split(" ")[0]);
			takePhotosOnResult(driver, browserName, "successCreate");

			driver.findElement(By.tagName("app-alert-message")).click();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.print("verificationErrors: " + verificationErrors.toString());
		driver.findElement(By.id("mat-dialog-close")).click();
		driver.quit();
	}
}