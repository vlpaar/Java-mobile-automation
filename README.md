# java-mobile-automation
Java is one of the most versatile programming languages which is platform independent. My current aim is to create an automation framework for applications(native, hybrid) on real Android and iOS devices.

Common setup steps:
1. Install latest Eclipse.
2. Install TestNG Eclipse Plugin from Eclipse Marketplace.
3. Import this maven project.
4. Install Appium Studio and open it.

Further steps for Android:
1. Turn on USB debugging in Android device from developer options.
2. Install the application to be automated in the Android device.
3. Connect it to PC via usb cable.
4. open Appium studio, click Add Device>Android Device and verify the device is recognized in Appium Studio.
5. Go to src/test/resources/TestData.properties in IDE and put the UDID of the device in UDID_ANDROID, app package and activity names in APP_PACKAGE and APP_ACTIVITY respectively, DeviceType as Android.
6. Add necessary tests in tests package and page objects in pageobjects package under src/test/java.
7. You can add multiple tests to testng.xml and run them at same time.

Further steps for iOS(need a developer account):
1. Make sure iTunes and its components are installed if you're using windows machine.
2. Install the application to be automated in the Android device.
3. Connect iOS device to PC via USB and allow access to data.
4. Go to src/test/resources/TestData.properties in IDE and put the UDID of the device in UDID_IOS, bundle ID in BUNDLE_ID, DeviceType as iOS.
5. Open Appium Studio, go to Tools>iOS Provisioning Profile Management, Register Developer Account.
6. Login with your developer email and password.(use app specific password if TFA is enabled)
7. Add necessary tests in tests package and page objects in pageobjects package under src/test/java.
8. You can add multiple tests to testng.xml and run them at same time.


Authors:
1. Rohith Mullapudi
2. Lasya Vadapalli
