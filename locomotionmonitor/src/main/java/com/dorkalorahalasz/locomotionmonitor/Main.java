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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    private static Hashtable<String, Integer> monthMapping = new Hashtable<String, Integer>() {
        {
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
        }
    };

    // TODO add config

    // main page url
    private static String targetUrl = "https://jegy.mav.hu/";
    // the whole name of the start station
    private static String startStation = "Győr";
    // zthe whole name of the end station
    private static String endStation = "Zürich HB";
    // the year of your planned journey
    private static int targetYear = 2024;
    // the month of your planned journey (January = 1)
    private static int targetMonth = 8;
    // the day of your planned journey
    private static int targetDay = 25;
    // the path to your chromedriver.exe
    private static String chromedriver = "C:\\Users\\halas\\.vscode\\trainTicketChecker\\trainTicketChecker\\locomotionmonitor\\resources\\chrome-driver\\chromedriver-122.exe";
    // headless mode on?
    private static boolean isHeadless = false;

    public static void main(String[] args) {

        WebDriver driver = null;
        try {
            // create custom WebDriver
            driver = prepareDriver();
            log.info("You searching for tickets from " + startStation + " to " + endStation + " on the "
                    + targetDay + "." + targetMonth + "." + targetYear);
            log.info("Starting to find tickets...");
            // do the search
            boolean success = findTickets(driver);
            // report the results
            if (success) {
                log.warn("You can buy your tickets!");
            } else {
                log.warn("You have to wait for your tickets to be available");
            }
            // TODO error handling with custom errors
        } catch (Exception e) {
            log.error("Something went wrong in main " + e);
        } finally {
            // close the driver anyways
            driver.close();
        }

    }

    private static boolean findTickets(WebDriver driver) {

        // navigate to the target page
        try {
            driver.navigate().to(targetUrl);
            driver.manage().window().maximize();
            sleep(5000);
            closePopupIfPresent(driver);
            acknowledgeCookies(driver);

            // fill the search fields

            // Start station
            if (fillStationInput(driver, "//input[@id='startStation-input']", startStation)) {
                // End station
                if (fillStationInput(driver, "//input[@id='endStation-input']", endStation)) {
                } else
                    return false; // TODO custom err
            } else
                return false; // TODO custom err

            // choose the date
            driver.findElements(By.xpath("//button[@class='datepicker-toggler-button']")).get(0).click();
            sleep(3000);

            // and now something tricky to solve the text based month navigation
            // TODO add years logic
            int currentMonth;
            String currentMonthName;

            // System.out.println("Try to set the " + targetMonth +". month");
            while (true) {
                currentMonthName = driver.findElement(By.xpath("//button[@class='prevMonth']/following-sibling::h2"))
                        .getText();
                // remove the year
                currentMonthName = currentMonthName.substring(0, currentMonthName.indexOf(" "));
                currentMonth = monthMapping.get(currentMonthName);

                // System.out.println("Now we have " + currentMonthName + " which is the "+
                // currentMonth+". month");

                if (targetMonth == currentMonth) {
                    // System.out.println("We are in the right month now!");
                    break;
                }
                // you dont want to search in the past i guess...
                // at least true, until i have the year logic implemented
                else {
                    // System.out.println("We have to move to the next month");
                    driver.findElement(By.xpath("//button[@class='nextMonth']")).click();
                    sleep(1000);
                }
            }

            // and now pick the day
            String daySelectorXpath = "//button[@class='dateButton' and contains(text(),'" + targetDay + "')]";

            WebElement button = driver.findElement(By.xpath(daySelectorXpath));
            // check that the date is disabled
            if (button.getAttribute("disabled") != null) {
                log.debug("The day button is disabled.");
                return false;
            } else {
                log.debug("The day button is enabled.");
                return true;
            }
        } catch (Exception e) {
            log.error("Something went wrong " + e);
            return false; // TODO custom err
        }
    }

    private static boolean fillStationInput(WebDriver driver, String xpath, String stationName) {
        try {
            WebElement input = driver.findElement(By.xpath(xpath));
            input.click();
            stationName.chars().forEach(c -> {
                input.sendKeys(Character.toString(c));
                sleep(100); // simulate human typing
            });
            sleep(1000);
            input.sendKeys(Keys.RETURN);
            sleep(3000);
            return true;
        } catch (Exception e) {
            log.error("Error when setting station " + stationName + " " + e);
            return false;
        }
    }

    private static void acknowledgeCookies(WebDriver driver) {
        try {
            driver.findElement(By.xpath(
                    "//div[@class[contains(., 'cookie-container ng-star-inserted')]]//button[@class[contains(., 'ng-star-inserted')]]"))
                    .click();
            log.debug("Cookie popup closed");
            sleep(5000);
        } catch (NoSuchElementException ex) {
            log.debug("No cookie popup shown");
        } catch (Exception e) {
            log.error("Error when closing cookie popup:" + e);
        }
    }

    private static void closePopupIfPresent(WebDriver driver) {
        try {
            driver.findElement(By.xpath("//button[@class[contains(., 'test-helper-confirm-yes')]]")).click();
            log.debug("Popup closed");
            sleep(5000);
        } catch (NoSuchElementException ex) {
            log.debug("No popup shown");
        } catch (Exception e) {
            log.error("Error when closing popup:" + e);
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
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
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