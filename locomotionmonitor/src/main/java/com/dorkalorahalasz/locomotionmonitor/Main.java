package com.dorkalorahalasz.locomotionmonitor;

import java.util.Collections;
import java.util.Hashtable;

import org.apache.commons.lang.SystemUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {

    private static Hashtable<String, Integer> monthMapping = new Hashtable<String, Integer>() {{
        put("January", 1);
        put("February", 2);
        put("March", 3);
        put("April", 4);
        put("May", 5);
        put("June", 6);
        put("July", 7);
        put("August", 8);
        put("September", 9);
        put("October", 10);
        put("November", 11);
        put("December", 12);
    }};

    public static void main(String[] args) {
        //create custom WebDriver
        WebDriver driver = null;
        driver = prepareDriver();
        System.out.println("Search starts!");
        boolean success = findTickets(driver);
        //report the results
        if(success){
            System.out.println("You can buy your tickets!");
        } else{
            System.out.println("You have to wait for your tickets to be available");
        }

        driver.close();
       
    }

    private static boolean findTickets(WebDriver driver) {
                
        //navigate to the target page
        //TODO url
        try{
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
            System.out.println("Set start station to " + startStation);
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
            System.out.println("Set end station to " + endStation);
            endStation.chars().forEach(c -> {
                endInput.sendKeys(Character.toString(c));
                sleep(100); // simulate human typing
            });
            sleep(1000);
            endInput.sendKeys(Keys.RETURN);

            sleep(3000);

            //choose the date
            driver.findElements(By.xpath("//button[@class='datepicker-toggler-button']")).get(0).click();
            sleep(3000);

            //and now something tricky to solve the text based month navigation
            //TODO add years logic
            int targetMonth = 8;
            int currentMonth;
            String currentMonthName;

            System.out.println("Try to set the  " + targetMonth +". month");
            while(true){
                currentMonthName=driver.findElement(By.xpath("//button[@class='prevMonth']/following-sibling::h2")).getText();
                //remove the year
                currentMonthName=currentMonthName.substring(0, currentMonthName.indexOf(" "));
                currentMonth=monthMapping.get(currentMonthName);

                System.out.println("Now we have " + currentMonthName + " which is the "+ currentMonth+". month");

                if(targetMonth==currentMonth){
                    System.out.println("We are in the right month now!");
                    break;
                } 
                //you dont want to search in the past i guess...
                //at least true, until i have the year logic implemented
                else{
                    System.out.println("We have to move to the next month");
                    driver.findElement(By.xpath("//button[@class='nextMonth']")).click();
                    sleep(1000);
                }
            }

            //and now pick the day
            int targetDay=25;
            String daySelectorXpath="//button[@class='dateButton' and contains(text(),'" + targetDay + "')]";

            WebElement button = driver.findElement(By.xpath(daySelectorXpath));
            //check that the date is disabled
            if (button.getAttribute("disabled") != null) {
                System.out.println("The day button is disabled.");
                return false;
            } else {
                System.out.println("The day button is enabled.");
                return true;
            }
    } catch(Exception e){
        System.err.println("Something went wrong " + e);
        return false;
    }
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