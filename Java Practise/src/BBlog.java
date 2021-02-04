import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

public class BBlog {

	static WebDriver driver;
	static String createTxt;
	static String readTxt;
	static String myBlog;
	static int flag = 0;

	public static void main(String[] args) throws InterruptedException {

		// *------- Set Browser----------------------------------------------*//
		setBrowserConfig();

		// *------- Open the Webpage to test & login------------------------*//
		openWebsite();
		System.out.println("!!!!!Open Website and login done!!!!");

		// *-------Create a New Post ---------------------------------------*//
		createPost();
		System.out.println("!!!!!Create Post is successful  !!!!");

		// *----------------Read / find the POST----------------------------*//
		readPost();
		System.out.println("!!!!!Read & validate post done !!!!");

		// *----------------Search & find the POST---------------------------*//
		// * give the article title to be searched.
		//myBlog = "This is my article 02/02/2021 23:28:25 - TRUPTI";
	   myBlog = "This is my article 02/02/2021 22:57:15";

		// *-----------------------------------------------------------------*//
		findPost();
		if (flag == 1) {
			System.out.println("!!!!!Found & validate Post done  !!!!");
		} else {
			System.out.println("!!!!!Cannot Find the Post  !!!!");
		}
	}

	// ==========================================================================================
	public static void setBrowserConfig() {
		System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\Selenium Chrome Drivers\\chromedriver.exe");
		driver = new ChromeDriver();
	}

	// ==========================================================================================
	public static void openWebsite() throws InterruptedException {
		driver.get("https://candidatex:qa-is-cool@qa-task.backbasecloud.com/");
		driver.manage().window().maximize();
		System.out.println(driver.getTitle());
		// *----------------Signin into BBlog
		driver.findElement(By.linkText("Sign in")).click();
		driver.findElement(By.cssSelector("input[placeholder='Username']")).sendKeys("hrrane@gmail.com");
		driver.findElement(By.cssSelector("input[placeholder='Password']")).sendKeys("Password@123");
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("button[class='btn btn-lg btn-primary pull-xs-right']")).click();
		Thread.sleep(2000);
	}

	// ==========================================================================================
	public static void createPost() throws InterruptedException {
		// Click on new Post button
		driver.findElement(By.xpath("/html/body/app-root/app-navbar/nav/div/ul/li[3]")).click();
		Thread.sleep(1500);
		// Create object of SimpleDateFormat class and decide the format
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		// get current date time with Date()
		Date date = new Date();
		// Now format the date
		String date1 = dateFormat.format(date);

		// Article details are entered below
		createTxt = "This is Rey's article=>" + date1;

		driver.findElement(By.cssSelector("input[placeholder='Article Title']")).sendKeys(createTxt);
		driver.findElement(By.xpath(
				"/html/body/app-root/app-article-editor/div/div/div/div/app-dynamic-form/form/app-input[2]/fieldset/input"))
				.sendKeys("this is for automation test");
		driver.findElement(By.cssSelector("textarea[placeholder='Write your article (in markdown)']"))
				.sendKeys("Primary Text in the article");
		driver.findElement(By.cssSelector("input[placeholder='Enter Tags']")).sendKeys("#rey");
		driver.findElement(By.xpath("//button[@class='btn btn-lg pull-xs-right btn-primary']")).click();
		Thread.sleep(1500);

		System.out.println("ARTICLE TITLE CREATED:" + createTxt);
	}

	// ==========================================================================================
	public static void readPost() throws InterruptedException {
		// Go to home and click global feed
		driver.findElement(By.xpath("//ul[@class='nav navbar-nav pull-xs-right']/li[1]/a")).click();
		driver.findElement(By.xpath("//ul[@class='nav nav-pills outline-active']/li[2]/a")).click();
		Thread.sleep(2000);

		List<WebElement> articles = driver.findElements(By.cssSelector("div[class='article-preview'] a h1"));
		// System.out.println("Total elements found : " + articles.size());
		// System.out.println("printing 2nd element : " + articles.get(2).getText());

		Thread.sleep(2000);

		for (int i = 0; i < articles.size(); i++) {
			readTxt = articles.get(i).getText();
			// System.out.println("articles-list : " + i + "-->" + readTxt);
			System.out.println("ARTICLE TITLE READ:" + readTxt);
			Assert.assertEquals(createTxt, readTxt);
			System.out.println("The Article read from the Blog is:" + readTxt);
			break;
		}
	}

	// ==========================================================================================
	public static void findPost() throws InterruptedException {
		// Go to home and click global feed
		driver.findElement(By.xpath("//ul[@class='nav navbar-nav pull-xs-right']/li[1]/a")).click();
		driver.findElement(By.xpath("//ul[@class='nav nav-pills outline-active']/li[2]/a")).click();
		Thread.sleep(1500);
		List<WebElement> nxtPage = driver.findElements(By.cssSelector("ul[class='pagination'] li a"));
		// System.out.println("Total elements found : " + pages.size());

		int x = 0;
		int pagenum = 1;
		do {
			List<WebElement> pages = driver.findElements(By.cssSelector("div[class='article-preview'] a h1"));
			// System.out.println("Total elements found : " + pages.size());
			for (int y = 0; y < pages.size(); y++) {
				String foundBlog = pages.get(y).getText();
				System.out.println(foundBlog);
				if (foundBlog.equalsIgnoreCase(myBlog)) {
					pagenum = pagenum + x;
					System.out.println(
							"Searched Blog for Article : " + foundBlog + " : and finally found on Page: " + pagenum);
					flag = 1;
					x = 50;
					break;
				}
			}
			x++;
			System.out.println("Completed Reading Page :" + x);
			nxtPage.get(x).click();
			Thread.sleep(1000);
		} while (x < 50);

	}

}
