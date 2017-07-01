package fullpage.screenshot;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TakePageScreenShot {

	private static JavascriptExecutor jsc;
	private static long browserVisibleHeight;
	private static long browserFullHeight;
	private static String tempPath = null;
	private static String tempFolder = null;

	public static void PrintSinglePage(WebDriver driver,String path) 
	{
		File scrSinglePage = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrSinglePage, new File(path));
		} catch (IOException e) {
		}	
	}

	public static void PrintFullPage(WebDriver driver ,String path) 

	{
		tempPath = System.getProperty("java.io.tmpdir");
		tempFolder = "StichImage";
		File dir = new File(tempPath+ "\\" +tempFolder);
		dir.mkdir();
		System.out.println(dir.toString());
		int noOfFile = dir.list().length;
		try{
			if(noOfFile>0){
				FileUtils.cleanDirectory(dir);
			}
			printVisiblePageAndScroll(driver);

			stich(browserFullHeight,browserVisibleHeight,path);
		}
		catch(Exception e){
			System.out.println("in catch");
		}

	}

	public static void printVisiblePageAndScroll(WebDriver driver) 
	{
		jsc = (JavascriptExecutor)driver;
		browserVisibleHeight = (long) jsc.executeScript("return window.innerHeight");
		browserFullHeight = (long) jsc.executeScript("return document.body.scrollHeight");
		int noOfSnap = 0;
		long remainder =0;
		remainder = browserFullHeight%browserVisibleHeight;
		if(remainder>0){
			noOfSnap = (int) (browserFullHeight/browserVisibleHeight)+1;
		}
		else{
			noOfSnap = (int) (browserFullHeight/browserVisibleHeight);
		}
		for(int i=0; i<noOfSnap; i++){
			File snap = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try{
				FileUtils.copyFile(snap, new File(tempPath +"\\"+ tempFolder+ "\\"+ i +".png"));
				//Thread.sleep(250);
				jsc.executeScript("window.scrollBy(0,"+ browserVisibleHeight+" )");
				Thread.sleep(250);
			}
			catch(Exception e){

			}
		}
	}

	public static void stich(long webPageFullHeight,long webPageVisibleHeight,String path) throws IOException{

		File a = new File(tempPath+ "\\"+tempFolder);
		int noOfImgFile = a.listFiles().length;
		int finalImageWidth;
		int type;
		//fetching image files
		File[] imgFiles = new File[noOfImgFile];

		for (int i = 0; i < noOfImgFile; i++) {	
			imgFiles[i] = new File(tempPath+ "\\"+tempFolder +"\\" + i + ".png");
		}
		BufferedImage[] buffImages = new BufferedImage[noOfImgFile];
		for (int i = 0; i < noOfImgFile; i++) {
			buffImages[i] = ImageIO.read(imgFiles[i]);            
		}

		type = buffImages[0].getType();
		finalImageWidth = buffImages[0].getWidth()-18;

		int finalImgHeight=0;
		int imgStichHeight = 0;                     //to change the y axis of from where the next image should be appended
		long remainder = webPageFullHeight%webPageVisibleHeight;

		for (int i = 0; i < noOfImgFile; i++){ 
			finalImgHeight = finalImgHeight + buffImages[i].getHeight();
		}
		if(remainder!=0){
			finalImgHeight=finalImgHeight-(int)(webPageVisibleHeight-webPageFullHeight%webPageVisibleHeight);
		}
		BufferedImage finalImg = new BufferedImage(finalImageWidth, finalImgHeight, type);	

		if(remainder!=0){
			for (int i = 0; i < noOfImgFile-1; i++) {
				finalImg.createGraphics().drawImage(buffImages[i], 0, imgStichHeight, null);
				imgStichHeight =imgStichHeight + buffImages[i].getHeight();
			}
			finalImg.createGraphics().drawImage(buffImages[noOfImgFile-1], 0, imgStichHeight-(int)(webPageVisibleHeight-webPageFullHeight%webPageVisibleHeight), null);
			imgStichHeight =imgStichHeight + buffImages[noOfImgFile-1].getHeight();

		}
		else{
			for (int i = 0; i < noOfImgFile; i++) {
				finalImg.createGraphics().drawImage(buffImages[i], 0, imgStichHeight, null);
				imgStichHeight =imgStichHeight + buffImages[i].getHeight();
			}
		}
		System.out.println("Image concatenated.");
		//ImageIO.write(finalImg, "png", new File(path));
		ImageIO.write(finalImg, "png", new File("D:\\temp2\\home.png"));
	}
}
