package testcase;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import base.ProjectSpecificMethod;

public class ServiceConsole extends ProjectSpecificMethod {



	@BeforeTest
	public void SetData() {
		excelfileName = "LoginCredentials";
	}
	
	@Test(dataProvider = "fetchData")
	public void runDashboard(String uname, String pwd) throws InterruptedException {
		 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		//Login to https://login.salesforce.com
		driver.findElement(By.id("username")).sendKeys(uname);
		driver.findElement(By.id("password")).sendKeys(pwd);
		driver.findElement(By.id("Login")).click();
		
		wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.className("slds-icon-waffle"))));
		//Click toggle menu button from the left corner
		driver.findElement(By.className("slds-icon-waffle")).click();
		
		//Click Service Console from App Launcher
		WebElement serviceConsole = driver.findElement(By.xpath("//p[text()='Service Console']"));
		driver.executeScript("arguments[0].click();", serviceConsole);
		Thread.sleep(5000);
		try {
			List<WebElement> tabsToClose = driver.findElements(By.xpath("//button[contains(@class,'slds-button slds-button_icon slds-button_icon-x')]"));
			
			int numOfTabs = tabsToClose.size();
			System.out.println("No of tabs : " +(numOfTabs));
			if(numOfTabs > 0) {
				//Close the opened tabs
				for(int i = 0; i < tabsToClose.size(); i++) {
//				wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//button[contains(@class,'slds-button slds-button_icon slds-button_icon-x')]"))));
					Thread.sleep(500);
				driver.findElement(By.xpath("//button[contains(@class,'slds-button slds-button_icon slds-button_icon-x')]")).click();
				
				} 
			}
			}catch (Exception e) {
				
			System.out.println("No Tabs Opened");
			}
			
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//button[@title='Show Navigation Menu']"))));
			//Click Dashboards Dropdown
			driver.findElement(By.xpath("//button[@title='Show Navigation Menu']")).click();
			
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//ul[contains(@class,'slds-listbox slds')]//span[text()='Dashboards']"))));
			//Select Dashboards from DropDown
			driver.findElement(By.xpath("//ul[contains(@class,'slds-listbox slds')]//span[text()='Dashboards']")).click();
			
			//Click on New Dashboard
			driver.findElement(By.className("forceActionLink")).click();
			
			//Switch to frame
			driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title='dashboard']")));
			
			//Use faker to generate dynamic dashboard names
			Faker faker = new Faker();
			String randomName = faker.name().firstName();
			//Enter the Dashboard name as "YourName_Workout"
			String dashboardName = randomName +"_Workout";
			driver.findElement(By.id("dashboardNameInput")).sendKeys(dashboardName);
			
			//Enter Description as "Assessment" 
			driver.findElement(By.id("dashboardDescriptionInput")).sendKeys("Assessment");
			
			//Click Create
			driver.findElement(By.id("submitBtn")).click();
			
			Thread.sleep(2000);
			//Switch to frame
			 driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title='dashboard']")));
			
			try {
				//Click Done
				WebElement doneButton = driver.findElement(By.xpath("//div[@class='toolbarActions']//button[text()='Done']"));
				driver.executeScript("arguments[0].click();", doneButton);
				try {
				doneButton.click();
				}catch (Exception e) {
					System.out.println("RuntimeException");
				}
				

			}catch (Exception e) {
							System.err.println("Done button is not clicked");
			}
			
			//Verify the Dashboard is Created
			boolean dbName = driver.findElement(By.xpath("//span[@class='slds-page-header__title slds-truncate']")).isDisplayed();
			System.out.println(dbName);
			
			//Click Subscribe
			driver.findElement(By.xpath("//button[@class='slds-button slds-button_neutral']")).click();
			driver.switchTo().defaultContent();
			
			
			try {
				
			//Select Frequency as "Daily"
			driver.findElement(By.xpath("//label/span[text()='Daily']")).click();
			//Get the current time
			WebElement timeDD = driver.findElement(By.xpath("//select[@id='time']"));
			String currentTime = driver.findElement(By.xpath("//option[@selected='selected']")).getAttribute("value");	
			System.out.println("current time : " +currentTime);
			Select dd =  new Select(timeDD);
			
			/* If the current time is 11:00 pm, then select 12:00 am (in the dropdown)
			 * Else Set the time as current time + 1 hr
			 */
			if(currentTime.equals("23")) { //DOM values
				dd.selectByValue("0");
			}else {
			int value = Integer.parseInt(currentTime);
			int time = value+1;
			String ddValue = Integer.toString(time);
			dd.selectByValue(ddValue);
			}
			
			//Click Save
			driver.findElement(By.xpath("//span[text()='Save']")).click();
			
			//Verify "Your subscription is all set" message displayed or not
			boolean toastMsg = driver.findElement(By.xpath("//span[contains(@class,'toastMessage')]")).isDisplayed();
			softAssert.assertEquals(true, toastMsg);
			System.out.println("toastMsg : " + toastMsg);
					
			}catch (Exception e) {

				//Capture the alert message and print it in console for unsuccessful subscription
				wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[contains(@class,'slds-text-align--center')]"))));
				String errorMsg = driver.findElement(By.xpath("//div[contains(@class,'slds-text-align--center')]")).getText();
				System.out.println("Error Message : " + errorMsg);
				//Click OK
				WebElement okButton = driver.findElement(By.xpath("//button[contains(@class,'forceActionButton')]"));
				driver.executeScript("arguments[0].click();", okButton);
				try {
					okButton.click();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}

		
			//Close the "YourName_Workout" tab
			WebElement closeTab = driver.findElement(By.xpath("//button[contains(@class,'slds-button slds-button_icon slds-button_icon-x')]"));
			driver.executeScript("arguments[0].click();", closeTab);
			
			//Click Private Dashboards
			wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//li[@class='slds-nav-vertical__item scope']/a[@title='Private Dashboards']"))));
			driver.findElement(By.xpath("//li[@class='slds-nav-vertical__item scope']/a[@title='Private Dashboards']")).click();
			
			
			//Verify the newly created Dashboard is available
			driver.findElement(By.xpath("//input[contains(@class,'search-text-field')]")).sendKeys(dashboardName);
			
			//Click newly created dashboard
			WebElement dashboard = driver.findElement(By.xpath("//span[@class='highlightSearchText']"));
			
			//Get the text of the newly created dashboard
			String newDashboard = dashboard.getText(); 
			softAssert.assertEquals(newDashboard, dashboardName);
			
			wait.until(ExpectedConditions.elementToBeClickable(dashboard));
			driver.executeScript("arguments[0].click();", dashboard);
			
			//Switch to frame
			driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title='dashboard']")));
			
			//Click the dropdown near Subscribe
			driver.findElement(By.xpath("//button[contains(@class,'slds-button slds-button_icon-border-')]")).click();
			
			//Select Delete
			driver.findElement(By.xpath("//span[text()='Delete']")).click();
			
			wait.until(ExpectedConditions.elementToBeClickable(By.id("modalBtn1")));
			
			//Click Delete button from the popup
			driver.findElement(By.id("modalBtn1")).click();
			driver.switchTo().defaultContent();
			
			Thread.sleep(2000);
			driver.findElement(By.xpath("//input[contains(@class,'search-text-field')]")).sendKeys(dashboardName);
			
			//Confirm the Delete (Verify the item is not available under Private Dashboard folder)
			String result = driver.findElement(By.xpath("//span[@class='emptyMessageTitle']")).getText();
			softAssert.assertEquals(result, "No results found");
			softAssert.assertAll();
	}
	
}