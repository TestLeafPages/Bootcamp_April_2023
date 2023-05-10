package base;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.asserts.SoftAssert;

import utils.ReadData;


public class ProjectSpecificMethod {
	
	public RemoteWebDriver driver;
	public String excelfileName;
	public SoftAssert softAssert;

		@BeforeMethod
		public void preCondition() {
			ChromeOptions options=new ChromeOptions();
			options.addArguments("--disable-notifications");
			driver = new ChromeDriver(options);
			softAssert = new SoftAssert();
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
			driver.get("https://login.salesforce.com/");
			
		}
		
		@AfterMethod
		public void postCondition() {
			driver.close();
		}
		
		@DataProvider(name = "fetchData")
		public Object[][] readExcelData() throws IOException {
			return ReadData.readExcelData(excelfileName);
		}

}
