package pageObjects;

import org.openqa.selenium.By;

import baseTest.BaseTest;

public class HomePage extends BaseTest{
	//sample page object located by xpath
	public By Flights = By.xpath("//*[@text='Flights']");
	
//##########################methods for page objects##########################//
	//method to verify flights button
	public boolean verify_Flights() throws Exception {
		return verify(Flights);
	}
	//method to click flights button
	public void click_Flights() throws Exception {
		click(Flights);
	}
}