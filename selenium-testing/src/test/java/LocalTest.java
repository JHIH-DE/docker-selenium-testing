import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class LocalTest
{
	protected static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	@BeforeMethod
	@Parameters(value={"browser"})
	public void setupTest (String browser) throws MalformedURLException {
		StringBuffer verificationErrors = new StringBuffer();

		switch (browser){
			case "chrome":
				System.setProperty("webdriver.chrome.driver", "driver/chromedriver");
				// Launch Chrome browser
				driver.set(new ChromeDriver(DesiredCapabilities.chrome()));
				break;
			case "safari":
				driver.set(new SafariDriver());
				break;
			default:
				throw new SkipException("no supported browser available.");
		}
		driver.get().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

	}

	public WebDriver getDriver() {
		//Get driver from ThreadLocalMap
		return driver.get();
	}

	@AfterMethod
	public void tearDown() throws Exception {
		getDriver().quit();
	}

	@AfterClass
	void terminate () {
		//Remove the ThreadLocalMap element
		driver.remove();
	}

}