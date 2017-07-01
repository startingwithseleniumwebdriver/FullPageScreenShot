package fullpage.screenshot.test;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import fullpage.screenshot.TakePageScreenShot;

public class FullPageScreenShotTest {

	WebDriver driver;

	@BeforeTest
	public void initTest(){
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		driver = new ChromeDriver();
	}
	@Test
	public void testScreenShot() {
		driver.manage().window().maximize();
		driver.get("http://www.lexmark.com/en_us");
		TakePageScreenShot.PrintFullPage(driver,
				"./snaps/Snaps_" + new SimpleDateFormat("HH-mm-ss dd-MM-YYYY").format(new Date())+".png");
	}
	
	@AfterTest
	public void tearDown(){
		driver.quit();
	}
}
