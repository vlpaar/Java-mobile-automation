package tests;

import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;
import baseTest.BaseTest;
import pageObjects.HomePage;

public class Test1 extends BaseTest{
	//static elements
	private HomePage home;
	public static String TC_expected;
	public static String TC_Name;
	//sample constructor
	public Test1(){
		home = new HomePage();
	}
	//sample test
	@Test(priority=1)
	public void Home() {
		try {
			TC_Name="TC1";
			TC_expected="To Verify Flights Button";
			assertEquals(home.verify_Flights(), true);
			Logpass("TC1", TC_expected, "Home Button exists!");
		} catch (Exception e) {
			e.printStackTrace();
			LogFail("TC1", TC_expected, "ERROR", e);
		}
	}
}