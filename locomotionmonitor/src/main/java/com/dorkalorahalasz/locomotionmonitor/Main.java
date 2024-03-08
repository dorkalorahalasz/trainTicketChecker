package com.dorkalorahalasz.locomotionmonitor;

import java.util.Collections;

import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
    public static void main(String[] args) {
        //create custom WebDriver
        WebDriver driver = null;
        driver = prepareDriver();
        boolean success = findTickets(driver);
        //report the results
    }

    private static boolean findTickets(WebDriver driver) {
                
        //navigate to the target page
        //TODO url
        driver.navigate().to("https://jegy.mav.hu/");
        driver.manage().window().maximize();
        sleep(5000);
        closePopupIfPresent(driver);
        acknowledgeCookies(driver);
        
        //do the search

        //Start station
        WebElement startInput = driver.findElement(By.xpath("//input[@id='startStation-input']"));
        startInput.click();
        //TODO start station
        String startStation = "Győr";
        startStation.chars().forEach(c -> {
            startInput.sendKeys(Character.toString(c));
            sleep(100); // simulate human typing
        });
        sleep(1000);
        startInput.sendKeys(Keys.RETURN);

        sleep(3000);

        //End station
        WebElement endInput = driver.findElement(By.xpath("//input[@id='endStation-input']"));
        startInput.click();
        //TODO start station
        String endStation = "Zürich HB";
        endStation.chars().forEach(c -> {
            endInput.sendKeys(Character.toString(c));
            sleep(100); // simulate human typing
        });
        sleep(1000);
        startInput.sendKeys(Keys.RETURN);

        //choose the date
        driver.findElements(By.xpath("//button[@class='datepicker-toggler-button']")).get(0).click();
        sleep(3000);
        

    return false;
    }

    private static void acknowledgeCookies(WebDriver driver) {
        try{
            driver.findElement(By.xpath("//div[@class[contains(., 'cookie-container ng-star-inserted')]]//button[@class[contains(., 'ng-star-inserted')]]")).click();
            System.out.println("Cookie popup closed");
            sleep(5000);
        } catch(NoSuchElementException ex){
            System.out.println("No cookie popup shown");
        } catch (Exception e){
            System.out.println("Error when closing cookie popup:" + e);
        }
    }

    private static void closePopupIfPresent(WebDriver driver) {
        try{
            driver.findElement(By.xpath("//button[@class[contains(., 'test-helper-confirm-yes')]]")).click();
            System.out.println("Popup closed");
            sleep(5000);
        } catch(NoSuchElementException ex){
            System.out.println("No popup shown");
        } catch (Exception e){
            System.out.println("Error when closing popup:" + e);
        }
    }

    protected static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static protected WebDriver prepareDriver() {
        WebDriver driver = null;
        ChromeOptions chromeOptions = new ChromeOptions();
        boolean isHeadless = false; //TODO
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        // chromeOptions.setExperimentalOption("useAutomationExtension", false);
        //TODO correct path    
        String chromedriver = "C:\\Users\\halas\\.vscode\\trainTicketChecker\\trainTicketChecker\\locomotionmonitor\\resources\\chrome-driver\\chromedriver-122.exe";
        System.setProperty("webdriver.chrome.silentOutput", "true");
         if (SystemUtils.IS_OS_WINDOWS) {
            System.setProperty("webdriver.chrome.driver", chromedriver);
            if (!isHeadless) {
                chromeOptions.addArguments("--lang=en", "--disable-gpu", "--window-size=1920,1080",
                        "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
            } else {
                chromeOptions.addArguments("--lang=en", "--headless", "--disable-gpu", "--window-size=1920,1080",
                            "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
            }
            chromeOptions.addArguments("--remote-allow-origins=*");
 
        } else if (SystemUtils.IS_OS_LINUX) {
            System.setProperty("webdriver.chrome.driver", chromedriver.replace(".exe", ""));
            if (!isHeadless) {
                chromeOptions.addArguments("--lang=en", "--no-sandbox", "--disable-gpu", "--window-size=1920,1080",
                            "--print-to-pdf",
                            "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
            } else {
                chromeOptions.addArguments("--no-sandbox", "--headless", "--lang=en", "--disable-gpu",
                            "--window-size=1920,1080",
                            "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
                }
        } else if (SystemUtils.IS_OS_MAC) {
            System.setProperty("webdriver.chrome.driver", chromedriver);
            chromeOptions.addArguments("--lang=en", "--save-page-as-mhtml", "--window-size=1920,1080");
        }
        driver = new ChromeDriver(chromeOptions);
           
        return driver;
    }
}