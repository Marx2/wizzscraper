//package com.marx.wizzscraper;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.netflix.feign.EnableFeignClients;
//
//public class Example {
//
//	public static void main(String[] args) {
//
//		if (true)
//			return;
//		FirefoxOptions options = new FirefoxOptions();
//		options.setHeadless(true);
//		// You can optionally pass a Settings object here,
//		// constructed using Settings.Builder
//		FirefoxDriver driver = new FirefoxDriver(options);
//		// This will block for the page load and any
//		// associated AJAX requests
////		driver.get("http://example.com");
//		driver.get("https://be.wizzair.com/9.3.0/Api/search/flightDates?departureStation=KTW&arrivalStation=DSA&from=2019-01-16&to=2019-03-16");
//		// You can get status code unlike other Selenium drivers.
//		// It blocks for AJAX requests and page loads after clicks
//		// and keyboard events.
//		System.out.println(driver.getTitle());
//
//		// Returns the page source in its current state, including
//		// any DOM updates that occurred after page load
//		System.out.println(driver.getPageSource());
//
//		// Close the browser. Allows this thread to terminate.
//		driver.quit();
//	}
//}