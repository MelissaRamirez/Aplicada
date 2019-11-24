package com.venticas.dataTest;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PruebaExperimental {

	private WebDriver webDriver;
	
	By registerButton = By.id("register");
	
	 @Before
	    public void setUp() {
	    System.setProperty("webdriver.chrome.driver", "src/main/resources/ChromeDriver/chromedriver.exe");
	    webDriver = new ChromeDriver();
	    webDriver.manage().window().maximize();
//	    http://127.0.0.1:8080/venticas/login
	    webDriver.get("https://cleventy.com/tutorial-selenium-primeros-pasos/");
	    }
	 
	 @Test
	 public void createUser() { 
		 webDriver.findElement(registerButton).click();
	 }
}
