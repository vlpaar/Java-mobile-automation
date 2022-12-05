package baseTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import io.appium.java_client.MobileDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class BaseTest {
	public static String reportname;
	public static String className;
	public String sc = "./screenshots/";
	public String scriptName = "";
	DesiredCapabilities dc = new DesiredCapabilities();
	protected static MobileDriver<MobileElement> driver;
	public String testdatafile = System.getProperty("user.dir") + "/src/test/resources/testData/TestData.properties";
	public static Properties TD = null;

	//deletes the reports from previous test
	@BeforeSuite(alwaysRun = true)
	public static void DeleteReportFiles() throws IOException {
		File Report = new File(System.getProperty("user.dir") + "/Reports/");
		System.out.println("Deleting Report files");
		FileUtils.cleanDirectory(Report);
	}
	//open application(setup class initializes the driver)
	@BeforeClass(alwaysRun = true)
	public void setup() throws Exception {
		try {
			className = this.getClass().getSimpleName();
			System.out.println(className);
			CreateHTMLLog(className);
			scriptName = className;
			TD = new Properties();
			FileInputStream fsconf1 = new FileInputStream(testdatafile);
			TD.load(fsconf1);
			dc.setCapability("reportDirectory", TD.getProperty("reportDirectory"));
			dc.setCapability("reportFormat", TD.getProperty("reportFormat"));
			dc.setCapability("testName", "Java Automation");
			if(TD.getProperty("DeviceType").equalsIgnoreCase("Android")) 
			{
				dc.setCapability(MobileCapabilityType.UDID, TD.getProperty("UDID_ANDROID"));
				dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, TD.getProperty("APP_PACKAGE"));
				dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, TD.getProperty("APP_ACTIVITY"));
				dc.setCapability(MobileCapabilityType.NO_RESET, true);
				driver = new AndroidDriver<MobileElement>(new URL(TD.getProperty("ExecutionUrl")), dc);
			}
			if(TD.getProperty("DeviceType").equalsIgnoreCase("iOS"))
			{
				dc.setCapability(MobileCapabilityType.UDID, TD.getProperty("UDID_IOS"));
				dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, TD.getProperty("BUNDLE_ID"));
				driver = new IOSDriver<>(new URL(TD.getProperty("ExecutionUrl")), dc);
			}
			((RemoteWebDriver) driver).setLogLevel(Level.INFO);
			wait_High();
			Logpass("TC_default", "Launch App", "App successfully launched");
		}
		catch(Exception e) {
			e.printStackTrace();
			LogFail("TC_default", "Launch App", "Error while Launching App", e);
		}
	}
	//closes the application
	@AfterClass(alwaysRun=true)
	public void tearDown() throws Exception {
		System.out.println("After class");
		driver.closeApp();
	}
	//custom after suite method, can be useful for external reports
	@AfterSuite
	public void finalreport() throws Exception {

	}
	/*methods checks the presence of an element
	 *returns true if present
	 *throws an exception if element is not present*/

	public boolean verify(By ObjectName) throws Exception {
		boolean displayed = false;
		new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOfElementLocated(ObjectName));
		if (driver.findElement(ObjectName).isDisplayed()) {
			displayed = true;
		} else {
			throw new NoSuchElementException();
		}
		return displayed;
	}
	/*methods checks the presence of an element
	 *returns true if present
	 *returns false if element is not present*/
	public boolean verifyElementExists(By obj) throws Exception {
		wait_Low();
		if (driver.findElements(obj).size() != 0) {
			return true;
		} else {
			return false;
		}
	}
	//method to click an element
	public void click(By ObjectName) throws Exception {
		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(ObjectName));
		if (driver.findElement(ObjectName).isEnabled()) {
			driver.findElement(ObjectName).click();
		} else {
			throw new NoSuchElementException();
		}
	}
	//method to enter text in a textbox
	public void enterText(By obj,String val) throws Exception {
		verify(obj);
		driver.findElement(obj).sendKeys(val);
	}
	//method to scroll until a specific object appears on screen(max 25 times)
	public void Scroll_Till_ObjVisible(By ObjectName) throws InterruptedException {
		int count = 0;
		while (driver.findElements(ObjectName).size() == 0) {
			Dimension size = driver.manage().window().getSize();
			int starty = (int) (size.height * 0.70);
			int endy = (int) (size.height * 0.30);
			int startx = size.width / 2;
			driver.swipe(startx, starty, startx, endy, 3000);
			Thread.sleep(2000);
			if (count >= 25) {
				throw new NoSuchElementException();
			} else {
				count++;
			}
		}
		Thread.sleep(3000);
	}
	//wait for 6 seconds
	public void wait_Medium() throws Exception {
		Thread.sleep(6000);
	}
	//wait for 10 seconds
	public void wait_High() throws Exception {
		Thread.sleep(10000);
	}
	//wait for 3 seconds
	public void wait_Low() throws Exception {
		Thread.sleep(3000);
	}


	//###################################################Reporting###################################################//
	int stepnumber = 1;
	//method returns report name
	public String getReportname() {
		return reportname;
	}
	//method to get current time stamp
	public String getTimestampl() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String dateFormate = dateFormat.format(date).toString();
		return dateFormate;
	}
	//method to create a simple HTML report log
	public void CreateHTMLLog(String testcaseName) throws Exception {
		try {
			System.out.println(testcaseName);
			createFolders();
			reportname = make_uniq(testcaseName);
			System.out.println("ReportName >>>" + reportname);
			BufferedWriter out = new BufferedWriter(new FileWriter("./Reports/logs/" + reportname + ".html"));
			out.write("<html><head><title>Demo Java Automation : UI - Automation Report - Step Level</title></head><body>"
					+ "<caption align = 'center'><font size='3.5' face='Calibri' color = 'black'><h2 style=\"color:blue;\"><font color='#0000FF'>Demo Clear Trip : UI - Automation Report - Step Level</font></h2></font></caption><br>"
					+ "<caption align = 'left'><font size='3.5' face='Calibri' color = 'black'> EXECUTION STARTED AT :<b>"
					+ getTimestampl() + "</b></font></caption><br>"
					+ "<caption align = 'left'><font size='3.5' face='Calibri' color = 'black'> PROGRAM NAME :<b>"
					+ "</b></font></caption><br>"
					+ "<caption align = 'left'><font size='3.5' face='Calibri' color = 'black'> <b></b></font></caption><br>"
					+ "<table border='1'>");
			out.write("<tr bgcolor='#305496'>"
					+ "<td><font size='3.5' face='Calibri' color = 'white'><b>TC ID</b></font></td>"
					+ "<td><font size='3.5' face='Calibri' color = 'white'><b>DESCRIPTION</b></font></td>"
					+ "<td><font size='3.5' face='Calibri' color = 'white'><b>RESULT</b></font></td>"
					+ "<td><font size='3.5' face='Calibri' color = 'white'><b>STATUS</b></font></td>"
					+ "<td><font size='3.5' face='Calibri' color = 'white'><b>ERROR MESSAGE</b></font></td>"
					+ "<td><font size='3.5' face='Calibri' color = 'white'><b>SCREENSHOT</b></font></td></tr>\r\n");
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	//method to create directories for new reports and screenshots(old directories are deleted in BeforeSuite method to delete old reports)
	public void createFolders() {
		try {
			File f1 = new File(System.getProperty("user.dir") + "/Reports");
			if (!f1.exists()) {
				f1.mkdir();
			}
			File f3 = new File(System.getProperty("user.dir") + "/Reports/logs");
			if (!f3.exists()) {
				f3.mkdir();
			}
			File f2 = new File(System.getProperty("user.dir") + "/Reports/logs/screenshots");
			if (!f2.exists()) {
				f2.mkdir();
			}
			File f4 = new File(System.getProperty("user.dir") + "/Reports/logs/Responses");
			if (!f4.exists()) {
				f4.mkdir();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	//creates a unique string based on date and time
	public static String make_uniq(String recordName) {
		String timestamp = "";
		String make_uniqvalue = "";
		Date today = new Date();
		SimpleDateFormat date = new SimpleDateFormat("MMddyyyy");
		SimpleDateFormat time = new SimpleDateFormat("HHmmss");
		timestamp = date.format(today) + time.format(today);
		if (recordName != null) {
			make_uniqvalue = recordName + timestamp;
		} else {
			make_uniqvalue = timestamp;
		}
		return make_uniqvalue;
	}
	//method to get date as string
	public String date() {
		SimpleDateFormat formattedDate = new SimpleDateFormat("ddMMyyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		String tomorrow = (String) (formattedDate.format(c.getTime()));
		System.out.println("Tomorrows date is " + tomorrow);
		return tomorrow;
	}
	//creates a test case fail entry to the report log
	public void LogFail(String action, String description, String input, Exception Errormessage) {
		try {
			String test = make_uniq("CAPTURE_");
			testTakesScreenshot(test);
			BufferedWriter out = new BufferedWriter(new FileWriter("./Reports/logs/" + reportname + ".html", true));
			System.out.println(sc + test);
			out.write("<tr bgcolor='#DC143C'><td><font size='3.5' face='Calibri' color = 'white'>" + action
					+ "</font></td><td><font size='3.5' face='Calibri' color = 'white'>" + description
					+ "</font></td><td><font size='3.5' face='Calibri' color = 'white'>" + input
					+ "</font></td><td><font size='3.5' face='Calibri' color = 'white'>FAIL</font></td><td><font size='3.5' face='Calibri' color = 'white'>"
					+ Errormessage + "</font></td><td><a href='" + sc + test + "'>Click here</a></td>"
					+ "</font></td></tr>\r\n");
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	//method to take screenshot
	public void testTakesScreenshot(String capture) throws Exception {

		File srcq = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcq, new File("./Reports/logs/screenshots/" + capture));
	}
	//creates a test case pass entry to the report log
	public void Logpass(String action, String description, String input) {
		try {
			String test = make_uniq("CAPTURE_");
			testTakesScreenshot(test);
			System.out.println("Logpass" + reportname);
			BufferedWriter out = new BufferedWriter(new FileWriter("./Reports/logs/" + reportname + ".html", true));
			out.write("<tr bgcolor='#32CD32'><td><font size='3.5' face='Calibri' color = 'black'>" + action
					+ "</font></td><td><font size='3.5' face='Calibri' color = 'black'>" + description
					+ "</font></td><td><font size='3.5' face='Calibri' color = 'black'>" + input
					+ "</font></td><td><font size='3.5' face='Calibri' color = 'black'>PASS</font></td><td><font size='3.5' face='Calibri' color = 'black'></font></td><td><a href='"
					+ sc + test + "'>click here</a></td></tr>\r\n");
			out.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}