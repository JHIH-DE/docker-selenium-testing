import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
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
import java.util.concurrent.TimeUnit;

public class BaseGridTest {
	//Declare ThreadLocal Driver (ThreadLocalMap) for ThreadSafe Tests
	protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();
	protected static ThreadLocal<String> browser = new ThreadLocal<String>();
	protected static StringBuffer verificationErrors = new StringBuffer();

	@BeforeMethod
	@Parameters(value = {"browser"})
	public void setupTest(String browser) throws MalformedURLException {
		//Set DesiredCapabilities
		DesiredCapabilities capabilities = new DesiredCapabilities();

		//Set BrowserName
		capabilities.setCapability("browserName", browser);

		this.browser.set(browser);

		//Set the Hub url (Docker exposed hub URL)
		driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities));

		driver.get().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	public RemoteWebDriver getDriver() {
		//Get driver from ThreadLocalMap
		return driver.get();
	}

	public String getBrowser() {
		return browser.get();
	}

	@AfterMethod
	public void tearDown() throws Exception {
		getDriver().quit();
	}

	@AfterClass
	void terminate() {
		//Remove the ThreadLocalMap element
		driver.remove();
	}

	@AfterClass
	void folderUploader(){
		File[] files = new File("snapshot").listFiles();
		folderUploader(files);
	}

	public void pressEsc() {
		Actions action = new Actions(driver.get());
		action.sendKeys(Keys.ESCAPE).build().perform();
	}

	public void snapshot(WebDriver driver, String directoryName, String fileName, String actionNmae) {
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

	public void takePhotosOnView(RemoteWebDriver driver, String browserName, String actionName) {

		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void takePhotosOnResult(RemoteWebDriver driver, String browserName, String actionName) {
		Dimension d = new Dimension(1024, 768);
		driver.manage().window().setSize(d);
		snapshot(driver, "result", browserName, actionName);
	}

	public void uploaderToMinio(String sourceFolderPath) throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {
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
			minioClient.putObject("selenium", dateFormat.format(date) + "/" + timeFormat.format(date) + "/" + sourceFolderPath, "/Users/jason_li/Documents/selenium-testing/snapshot/" + sourceFolderPath);
			System.out.println("/selenium-testing/" + sourceFolderPath + " is successfully uploaded as " + sourceFolderPath + " to `selenium` bucket.");
		} catch (MinioException e) {
			System.out.println("Error occurred: " + e);
		}
	}

	public void folderUploader(File[] files) {
		for (File file : files) {
			if (file.isDirectory()) {
				folderUploader(file.listFiles()); // Calls same method again.
			} else {
				if (!file.getName().equals(".DS_Store")) {
					try {
						uploaderToMinio(file.getParentFile().getName() + "/" + file.getName());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
