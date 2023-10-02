package com.cognizant.mfpe.pensionManagementSystem.pensionerDetail.controller;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.mfpe.pensionManagementSystem.pensionerDetail.PensionerDetailApplication;
import com.cognizant.mfpe.pensionManagementSystem.pensionerDetail.dao.BankLogServiceDao;
import com.cognizant.mfpe.pensionManagementSystem.pensionerDetail.model.BankLog;
import com.cognizant.mfpe.pensionManagementSystem.pensionerDetail.model.Pensioner;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import io.swagger.annotations.ApiOperation;

@RestController
public class ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// {
	private static Logger LOGGER = LoggerFactory.getLogger(PensionerDetailApplication.class);
	private static List<Pensioner> pensionersList = new ArrayList<>();

	@Autowired
	private BankLogServiceDao bankLogServiceDao;

	/**
	 * This method is used to load CSV data on start up
	 */
	@PostConstruct
	public void loadPensionerData() {
		LOGGER.info("START");
		readCsv();
	}

	/**
	 * This method reads data from CSV file
	 * 
	 */
	public static void readCsv() {
		PensionerDetailController.pensionersList.clear();
		Resource resource = new ClassPathResource("allPensionersData.csv");
		LOGGER.info("resource: " + resource);
		try {
			InputStream input = resource.getInputStream();
			LOGGER.info("read csv file started: ");
			InputStream file = resource.getInputStream();
			LOGGER.info("file: " + file);
			CSVReader reader = new CSVReader(new InputStreamReader(file, Charset.forName("UTF8")));
			LOGGER.info("reader: " + reader);
			String[] pensionerString;
			Pensioner pensioner;

			String[] line = reader.readNext();
			LOGGER.info("line: " + line);
			while ((pensionerString = reader.readNext()) != null) // returns a Boolean value
			{
				pensioner = new Pensioner(pensionerString[0],
						new SimpleDateFormat("dd-MM-yyyy").parse(pensionerString[1]), pensionerString[2],
						Double.parseDouble(pensionerString[3]), Double.parseDouble(pensionerString[4]),
						pensionerString[5], Long.parseLong(pensionerString[6]), pensionerString[7],
						Long.parseLong(pensionerString[8]), Double.parseDouble(pensionerString[9]),
						Double.parseDouble(pensionerString[10]));
				LOGGER.info("Pensioner details: " + pensioner);
				PensionerDetailController.pensionersList.add(pensioner);
			}
			System.out.println(pensionersList);
			LOGGER.info("List of pensioners: " + pensionersList);
		} catch (CsvValidationException | NumberFormatException | IOException | ParseException e) {
			LOGGER.info("EXCEPTION: " + e);
			LOGGER.info("EXCEPTION message: " + e.getMessage());
			LOGGER.info("EXCEPTION stacktrace: " + e.getStackTrace());
			LOGGER.info("EXCEPTION localised message: " + e.getLocalizedMessage());
			LOGGER.info("EXCEPTION: ");
		}
		LOGGER.info("END");
	}

	/**
	 * The method at this end point retrieves pensioner details based on Aadhaar
	 * number
	 * 
	 * @param String token, aadhaar number
	 * @return Pensioner
	 */
	@GetMapping("/PensionerDetailByAadhaar/{aadhaarNumber}")
	@ApiOperation(value = "Provides the pensioner details", response = Pensioner.class)
	public Pensioner getPensionerDetailByAadhaar(@RequestHeader("Authorization") String token,
			@PathVariable long aadhaarNumber) {
		LOGGER.info("START");
		if (bankLogServiceDao.isSessionValid(token)) {
			Pensioner pensioner;
			LOGGER.info("PensionerDetailController.pensionersList: " + PensionerDetailController.pensionersList);
			for (int i = 0; i < PensionerDetailController.pensionersList.size(); i++) {
				pensioner = PensionerDetailController.pensionersList.get(i);
				System.out.println(pensioner);
				LOGGER.info("pensioner: " + pensioner);
				System.out.println(pensioner.getAadhaarNumber());
				LOGGER.info("pensioner's list aadhar number: " + pensioner.getAadhaarNumber());
				System.out.println(aadhaarNumber);
				LOGGER.info("pensioner input aadhar: " + aadhaarNumber);
				if (pensioner.getAadhaarNumber() == aadhaarNumber) {
					LOGGER.info("END -> got aadhar number verified");
					return pensioner;
				}
			}
			LOGGER.info("END -> aadhar number not verified");

			return null;
		}
		LOGGER.info("END");

		return null;

	}

	/**
	 * The method at this end point updates pensioner detail to CSV
	 * 
	 * @param String token, Pensioner
	 * @return Boolean
	 */
	@PostMapping("/UpdatePensioner")
	@ApiOperation(value = "Updates the pensioner details in the CSV file")
	public boolean updatePensioner(@RequestHeader("Authorization") String token,
			@RequestBody Pensioner updatedPensioner) {
		LOGGER.info("START");
		if (bankLogServiceDao.isSessionValid(token)) {
			try {
				boolean flag = false;
				for (Pensioner pensioner : PensionerDetailController.pensionersList) {
					if (pensioner.getAadhaarNumber().equals(updatedPensioner.getAadhaarNumber())) {
						PensionerDetailController.pensionersList.remove(pensioner);
						PensionerDetailController.pensionersList.add(updatedPensioner);
						flag = true;
						break;
					}
				}
				if (flag == false) {
					LOGGER.info("END");
					return flag;
				} else {
					Resource resource = new ClassPathResource("allPensionersData.csv");
					File file;
					file = resource.getFile();

					CSVReader reader2 = null;
					reader2 = new CSVReader(new FileReader(file));

					List<String[]> allElements;
					allElements = reader2.readAll();
					allElements.clear();

					FileWriter sw;
					sw = new FileWriter(file);

					CSVWriter writer = new CSVWriter(sw);
					writer.writeAll(allElements);
					String[] header = { "Name", "DateOfBirth", "PAN", "SalaryEarned", "Allowances", "PensionType",
							"AadhaarNumber", "BankName", "BankAccountNumber", "BankServiceCharge", "PensionAmount" };
					writer.writeNext(header);
					for (Pensioner pensioners : PensionerDetailController.pensionersList) {
						String dob = new SimpleDateFormat("dd-MM-yyyy").format(pensioners.getDateOfBirth());
						String[] data = { pensioners.getName(), dob, pensioners.getPanNumber(),
								Double.toString(pensioners.getSalary()), Double.toString(pensioners.getAllowances()),
								pensioners.getTypeOfPension(), Long.toString(pensioners.getAadhaarNumber()),
								pensioners.getBankName(), Long.toString(pensioners.getAccountNumber()),
								Double.toString(pensioners.getBankServiceCharge()),
								Double.toString(pensioners.getPensionAmount()) };
						writer.writeNext(data);

					}

					writer.close();
					readCsv();

				}
			} catch (IOException e) {
				LOGGER.info("EXCEPTION");

			} catch (CsvException e) {
				LOGGER.info("EXCEPTION");
			}
			LOGGER.info("END");

			return true;
		}
		LOGGER.info("END");

		return false;

	}

	/**
	 * The method at this end point logs bank transaction to the database
	 * 
	 * @param String token, Pensioner
	 * @return Boolean
	 */

	@PostMapping("/LogTransaction")
	@ApiOperation(value = "Saves the bank details to the database")
	public boolean logTransaction(@RequestHeader("Authorization") String token, @RequestBody Pensioner pensioner) {
		LOGGER.info("START");

		if (bankLogServiceDao.isSessionValid(token)) {

			if (bankLogServiceDao.logTransaction(
					new BankLog(pensioner.getName(), new java.sql.Date(pensioner.getDateOfBirth().getTime()),
							pensioner.getPanNumber(), pensioner.getSalary(), pensioner.getAllowances(),
							pensioner.getTypeOfPension(), pensioner.getAadhaarNumber(), pensioner.getBankName(),
							pensioner.getAccountNumber(), pensioner.getBankServiceCharge(),
							pensioner.getPensionAmount(), new java.sql.Date((new Date()).getTime())))) {
				LOGGER.info("END");
				return true;
			}
			LOGGER.info("END");

			return false;
		}
		LOGGER.info("END");

		return false;
	}
}
