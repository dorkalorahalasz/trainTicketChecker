# trainTicketChecker

#### Background story:<br/> 
you want to buy the cheapest train tickets as soon as they are available, but dont want to do the search on the webpage every day? This programm is the first step to simplify the process. You just make a configuration with date and stations, start the program and it reports you the availability of the tickets. Thats it!

#### General:<br/>
This application is based on Java, Selenium and Spring<br/> 
It is able to navigate to a website (tested with https://jegy.mav.hu/), do the search based on your configuration and gives back an answer.

#### Usage:<br/>
Clone the repository in some code editor (eg Eclipse or VSCode), which is able to run a java application <br/>
Find the ***application-user.properties*** file and fill the `startStation`, `endStation`, `targetYear`, `targetMonth` and `targetDay` values<br/>
Run the program and wait for the result<br/>

#### Requirements:<br/>
You have to have GoogleChrome and maven installed on your machine

#### Troubleshooting:<br/>
`selenium.common.exceptions.SessionNotCreatedException: Message: session not created: This version of ChromeDriver only supports Chrome version XX
Current browser version is YY with binary path...`<br/>
* check your current GoogleChrome version<br/>
* download the proper ChromeDriver from https://developer.chrome.com/docs/chromedriver/downloads<br/>
* unzip it and place the exe file in `src/main/resources/chrome-driver/`<br/>
* update the `chromedriver` value in ***application-dev.properties*** file

`selenium.common.exceptions.NoSuchElementException: Message: no such element: Unable to locate element: {"method":"xpath","selector":"//incorrect_xpath"}`<br/>
* check the xpath on the webpage
* if its not correct, create the correct xpath
* update the proper xpath value in ***xpath_expressions.properties*** file
  
**OR** 

* open a GitHub Issue and document the error for me :)


#### For developers:<br/>

Headless mode can be turned on and off in ***application-dev.properties***

For those who wants to get data from a webpage with similar searching logic
* modify the `targetUrl` in ***application-dev.properties***
* update the xpath values in ***xpath_expressions.properties***


### This is just a first simple version. For more features check [this new repo](https://github.com/dorkalorahalasz/trainTicketCheckerDockerized) and please have a bit of patience, im working on it ;)


