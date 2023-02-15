package com.lms.pages;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class LMSHolidayListPage {

	private WebDriver driver;
	private static final Logger logger = LogManager.getLogger(LMSHolidayListPage.class);

	// Constructor of the page class
	public LMSHolidayListPage(WebDriver driver) {
		this.driver = driver;
	}

	private By windowTitle = By.xpath("//div/h4[contains(text(),\"Office Holiday list\")]");

	// Method to get the title of the LMS Holiday List window
	public String getWindowTitle() throws InterruptedException {

		// Title of the window
		// WebDriverWait wait = new WebDriverWait(this.driver,Duration.ofSeconds(30));
		Thread.sleep(5000);
		return driver.findElement(windowTitle).getText();

	}
	int counter = 0;
	// Method to count the Public Holidays
	public void countOfPublicHolidays() throws IOException {

		logger.info("Validate whether the count of public holidays is less than 10");

		int rowSize = getTableRows().size();
		logger.info("No of rows in the table is: " + rowSize);

		int colSize = getTableColumns().size();
		logger.info("No of columns in the table is: " + colSize);

		
		for (int i = 1; i <= rowSize; i++) {
			for (int j = 1; j <= colSize; j++) {

				String cellContent = driver
						.findElement(By
								.xpath("//*[@id=\"display-holidaylist-form\"]/table/tbody/tr[" + i + "]/td[" + j + "]"))
						.getText() + "  ";

				if (cellContent.contains("Public Holiday")) {
					// Increment the counter
					counter++;
				}

			}
		}
		// Check if the count of public holidays is greater than or equal to 10
		if (counter <= 10) {
			logger.info("Number of public holidays:" + counter);
		}

	}
/**
	// Generate reports based on the holiday type
	public void generateReport() throws IOException {
		// Get the data in each row and check if the Holiday Type is Public or Optional
		List<WebElement> rowData = getTableRows();
		
		String holidayTypeText = null;

		for (int row = 1; row <= rowData.size(); row++) {
			WebElement holidayType = driver
					.findElement(By.xpath("//*[@id=\"display-holidaylist-form\"]/table/tbody/tr[" + row + "]/td[4]"));
			 holidayTypeText = holidayType.getText();

			if (holidayTypeText.equalsIgnoreCase("Public Holiday")) {
				// Method to generate Public Holiday excel report
				String sheetName = "Public Holiday";
				generateHoldiayExcelReport(sheetName);
			} else if (holidayTypeText.equalsIgnoreCase("Optional Holiday")) {
				// Method to generate Optional Holiday excel report
				String sheetName = "Optional Holiday";
				generateHoldiayExcelReport(sheetName);
			}
		}

	}
**/
	// Method to create the excel report
	public void generateHoldiayExcelReport() throws IOException {
		logger.info("Generate report based on the holiday type");

		FileOutputStream fileOut = null;
		// Get the table headers and data
		List<WebElement> headerData = getTableHeader();
		List<WebElement> rowData = getTableRows();
		List<WebElement> colData = getTableColumns();

		//String cellContentTxt = null;

		// Create a workbook in xlsx format
		Workbook workbook = new XSSFWorkbook();
		

		// Create sheet
		Sheet sheet1 = workbook.createSheet("Public Holiday");
		Sheet sheet2 = workbook.createSheet("Optional Holiday");

		Row headerValue1 = sheet1.createRow(0);
		Row headerValue2 = sheet2.createRow(0);

		// Set a font for the headings
		Font headerfont = workbook.createFont();
		headerfont.setBold(true);
		headerfont.setFontHeightInPoints((short) 12);
		headerfont.setColor(IndexedColors.BLACK.index);

		// Create cell style
		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFont(headerfont);
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);

		// Set the headers in the sheet
		for (int header = 1; header <= headerData.size(); header++) {
			String headerText = driver
					.findElement(By.xpath("//*[@id=\"display-holidaylist-form\"]/table/thead/tr[1]/th[" + header + "]"))
					.getText();

			Cell cell1 = headerValue1.createCell(header - 1);
			cell1.setCellValue(headerText);
			cell1.setCellStyle(headerStyle);
			
			Cell cell2 = headerValue2.createCell(header - 1);
			cell2.setCellValue(headerText);
			cell2.setCellStyle(headerStyle);
		}

		// Set the values for holiday type in both the sheets
        String type = null;
        counter = 1;
		for (int row = 1; row <= rowData.size(); row++) {
			 type = driver.findElement(By.xpath("//*[@id=\"display-holidaylist-form\"]/table/tbody/tr["+row+"]/td[4]")).getText();
			 Row rowvalue = sheet1.createRow(counter);
			 
			if (type.equalsIgnoreCase("Public Holiday")) 
			{
			for (int col = 1; col <= colData.size(); col++) {
	     		  List<WebElement> cellcontent = driver.findElements(By.xpath("//*[@id=\"display-holidaylist-form\"]/table/tbody/tr["+row+"]/td["+col+"]"));
	     	       
	     		  	for(WebElement loop:cellcontent ) {       		  		
	     		  		String detail = loop.getText();					
					rowvalue.createCell(col-1).setCellValue(detail);
				}

			}counter++;
		}
		}
		int b = 1;
			for (int row = 1; row <= rowData.size(); row++) {
				 type = driver.findElement(By.xpath("//*[@id=\"display-holidaylist-form\"]/table/tbody/tr["+row+"]/td[4]")).getText();
				 Row rowvalue = sheet2.createRow(b);	
				if (type.equalsIgnoreCase("Optional Holiday")) 
				{					
				for (int col = 1; col <= colData.size(); col++) {
		     		  List<WebElement> cellcontent = driver.findElements(By.xpath("//*[@id=\"display-holidaylist-form\"]/table/tbody/tr["+row+"]/td["+col+"]"));
		     	       
		     		  	for(WebElement loop:cellcontent ) {       		  		
		     		  		String detail = loop.getText();					
						rowvalue.createCell(col-1).setCellValue(detail);
					}

				}
				b=b+1;
			}
		}
/**
		if (sheetName.equalsIgnoreCase("Public Holiday")) {
			fileOut = new FileOutputStream(
					"C:\\Users\\KL836FH\\eclipse-workspace\\PublicHolidayReport.xlsx");
		} else if (sheetName.equalsIgnoreCase("Optional Holiday")) {
			fileOut = new FileOutputStream(
					"C:\\Users\\KL836FH\\eclipse-workspace\\OptionalHolidayReport.xlsx");
		}
		**/
      /**
		workbook.write(fileOut);
		fileOut.close();
		workbook.close();
		**/
		logger.info("Report generated successfully");
		FileOutputStream fileOut1 = new FileOutputStream("C:\\Users\\KL836FH\\eclipse-workspace\\HolidayReport.xlsx");
		workbook.write(fileOut1);
		fileOut1.close();
		workbook.close();
		logger.info("Report generated successfully");
	}


	//Method to get the table headers
	public List<WebElement> getTableHeader() {

		logger.info("Get the values of the table header");
		// Get the headers
		List<WebElement> tableHeaders = driver
				.findElements(By.xpath("//*[@id=\"display-holidaylist-form\"]/table/thead/tr[1]/th"));

		return tableHeaders;
	}

	// Method to get the table rows
	public List<WebElement> getTableRows() {

		// Get all the row elements
		List<WebElement> rowElements = driver
				.findElements(By.xpath("//*[@id=\"display-holidaylist-form\"]/table/tbody/tr"));
		return rowElements;
	}

	// Method to get the table columns
	public List<WebElement> getTableColumns() {

		// Get all the row elements
		List<WebElement> colElements = driver
				.findElements(By.xpath("//*[@id=\"display-holidaylist-form\"]/table/tbody/tr[1]/td"));

		return colElements;
	}
}