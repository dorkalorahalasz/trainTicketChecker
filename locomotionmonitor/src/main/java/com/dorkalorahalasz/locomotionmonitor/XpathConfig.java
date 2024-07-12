package com.dorkalorahalasz.locomotionmonitor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/*
 * This class allows to configure the xpaths used by the app, so you dont have to find them hardcoded
 */

@Configuration
@PropertySource("classpath:xpath_expressions.properties")
public class XpathConfig {

    protected String xpathStartStationInput;

    protected String xpathEndStationInput;

    protected String xpathDatePickerButton;

    protected String xpathCurrMonth;

    protected String xpathNextMonthButton;

    protected String xpathDayButton;

    protected String xpathAcceptCookiesButton;

    protected String xpathAcceptPopupButton;

    @Value("${startStationInput}")
    public void setXpathStartStationInput(String xpathStartStationInput) throws Exception {
        if(xpathStartStationInput == null || xpathStartStationInput.trim().equals("")) {
            throw new Exception("startStationInput is missing! Please check xpath_expressions.properties");
        }
        this.xpathStartStationInput = xpathStartStationInput;
    }

    @Value("${endStationInput}")
    public void setXpathEndStationInput(String xpathEndStationInput) throws Exception {
        if(xpathEndStationInput == null || xpathEndStationInput.trim().equals("")) {
            throw new Exception("endStationInput is missing! Please check xpath_expressions.properties");
        }
        this.xpathEndStationInput = xpathEndStationInput;
    }

    @Value("${datePickerButton}")
    public void setXpathDatePickerButton(String xpathDatePickerButton) throws Exception {
        if(xpathDatePickerButton == null || xpathDatePickerButton.trim().equals("")) {
            throw new Exception("datePickerButton is missing! Please check xpath_expressions.properties");
        }
        this.xpathDatePickerButton = xpathDatePickerButton;
    }

    @Value("${currMonth}")
    public void setXpathCurrMonth(String xpathCurrMonth) throws Exception {
        if(xpathCurrMonth == null || xpathCurrMonth.trim().equals("")) {
            throw new Exception("currMonth is missing! Please check xpath_expressions.properties");
        }
        this.xpathCurrMonth = xpathCurrMonth;
    }

    @Value("${nextMonthButton}")
    public void setXpathNextMonthButton(String xpathNextMonthButton) throws Exception {
        if(xpathNextMonthButton == null || xpathNextMonthButton.trim().equals("")) {
            throw new Exception("nextMonthButton is missing! Please check xpath_expressions.properties");
        }
        this.xpathNextMonthButton = xpathNextMonthButton;
    }

    @Value("${dayButton}")
    public void setXpathDayButton(String xpathDayButton) throws Exception {
        if(xpathDayButton == null || xpathDayButton.trim().equals("")) {
            throw new Exception("dayButton is missing! Please check xpath_expressions.properties");
        }
        this.xpathDayButton = xpathDayButton;
    }

    @Value("${acceptCookiesButton}")
    public void setXpathAcceptCookiesButton(String xpathAcceptCookiesButton) throws Exception {
        if(xpathAcceptCookiesButton == null || xpathAcceptCookiesButton.trim().equals("")) {
            throw new Exception("acceptCookiesButton is missing! Please check xpath_expressions.properties");
        }
        this.xpathAcceptCookiesButton = xpathAcceptCookiesButton;
    }

    @Value("${acceptPopupButton}")
    public void setXpathAcceptPopupButton(String xpathAcceptPopupButton) throws Exception {
        if(xpathAcceptPopupButton == null || xpathAcceptPopupButton.trim().equals("")) {
            throw new Exception("acceptPopupButton is missing! Please check xpath_expressions.properties");
        }
        this.xpathAcceptPopupButton = xpathAcceptPopupButton;
    }

}
