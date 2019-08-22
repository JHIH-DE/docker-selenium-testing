import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseTest
{
	//Declare ThreadLocal Driver (ThreadLocalMap) for ThreadSafe Tests
	protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<RemoteWebDriver>();

	@BeforeMethod
	@Parameters(value={"browser"})
	public void setupTest (String browser) throws MalformedURLException {
		if(browser.equals("chrome") || browser.equals("firefox")){
			//Set DesiredCapabilities
			DesiredCapabilities capabilities = new DesiredCapabilities();

			//Set BrowserName
			capabilities.setCapability("browserName", browser);

			//Set the Hub url (Docker exposed hub URL)
			driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities));
		}
		else{
			throw new SkipException("no supported browser available.");
		}
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