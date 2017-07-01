package fullpage.screenshot.test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import fullpage.screenshot.TakePageScreenShot;

public class FullPageScreenShotTest {

	public static void main(String[] args) {

		System.setProperty("webdriver.chrome.driver", "D:\\Resources\\Drivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://www.lexmark.com/en_us");
		

		TakePageScreenShot.PrintFullPage(driver,"D:\\temp2\\"+"HomePage1.png");

	}
}
