package algo.weatherdata;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Retrieves temperature data from a weather station file.
 */
public class WeatherDataHandler {

	HashMap<LocalDate, Measurement> measurements = new HashMap<>();
	DecimalFormat df_obj = new DecimalFormat("#.##"); // To round double values

	/**
	 * Load weather data from file.
	 * 
	 * @param filePath path to file with weather data
	 * @throws IOException if there is a problem while reading the file
	 */
	public void loadData(String filePath) throws IOException {		
		List<String> fileData = Files.readAllLines(Paths.get(filePath));

        for (String line : fileData) {
        	String[] newLine = line.split(";");
			LocalDate date = LocalDate.parse(newLine[0]);

			if (measurements.containsKey(date)) {
				Measurement measurement = measurements.get(date);
				measurement.addTemperature(Double.parseDouble(newLine[2]));
				if (newLine[3].equals("G")) { measurement.addApprovedValue(); }
			} else {
				Double temperature = Double.parseDouble(newLine[2]);
				Measurement measurement = new Measurement(temperature, date);
				measurements.put(date, measurement);
				if (newLine[3].equals("G")) { measurement.addApprovedValue(); }
			}
		}
	}

	/**
	 * @param dateFrom start date (YYYY-MM-DD) inclusive  
	 * @param dateTo end date (YYYY-MM-DD) inclusive
	 * @return average temperature for each date, sorted by date  
	 */
	public List<String> averageTemperatures(LocalDate dateFrom, LocalDate dateTo) {
		ArrayList<String> averageTemperatures = new ArrayList<>();
		try {
			for (LocalDate date = dateFrom; date.isBefore(dateTo) || date.equals(dateTo); date = date.plusDays(1)) {
				Double averageTemperature = measurements.get(date).getAverageTemperature();
				averageTemperatures.add(date.toString() + " average temperature: " +
						df_obj.format(averageTemperature) + " degree Celsius");
			}

		} catch (Exception e) {System.out.println("Date not in register.");} // vad händer här?

		return averageTemperatures;
	}

	/**
	 * @param dateFrom start date (YYYY-MM-DD) inclusive  
	 * @param dateTo end date (YYYY-MM-DD) inclusive
	 * @return dates with missing values together with number of missing values for each date,
	 * sorted by number of missing values (descending)
	 */
	public List<String> missingValues(LocalDate dateFrom, LocalDate dateTo) {
		ArrayList<Measurement> missingValues = new ArrayList<>();
		ArrayList<String> missingValuesAsString = new ArrayList<>();
		try {
			for (LocalDate date = dateFrom ; date.isBefore(dateTo) || date.equals(dateTo); date = date.plusDays(1)) {
				missingValues.add(measurements.get(date));
			}
			missingValues.sort(Comparator.comparing(Measurement::getMissingValues).reversed().thenComparing(Measurement::getDate));

			for (Measurement measurement : missingValues) {
				missingValuesAsString.add(measurement.getDate().toString() + " missing " +
						measurement.getMissingValues() + " values");
			}
		} catch (Exception e) {System.out.println("Date not in register.");}

		return missingValuesAsString;
	}

	/**
	 * @param dateFrom start date (YYYY-MM-DD) inclusive  
	 * @param dateTo end date (YYYY-MM-DD) inclusive
	 * @return period and percentage of approved values for the period  
	 */
	public List<String> approvedValues(LocalDate dateFrom, LocalDate dateTo) {
		List<String> percentageApproved = new ArrayList<>();
		double approved = 0.0;
		double measures = 0.0;
		try {
			for (LocalDate date = dateFrom ; date.isBefore(dateTo) || date.equals(dateTo); date = date.plusDays(1)) {
				approved += measurements.get(date).getApprovedValues();
				measures += measurements.get(date).getNumberOfMeasurements();
			}
			percentageApproved.add("Approved values between " + dateFrom.toString() + " and " +
					dateTo.toString() + " : " + df_obj.format((approved / measures) * 100) + " %");
		} catch (Exception e) {System.out.println("Date not in register.");}

		return percentageApproved;
	}
}