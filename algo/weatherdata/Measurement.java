package algo.weatherdata;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Each date is connected to a measurement object, which contains a list of the
 * temperatures and the number of approved measurements for that specific date.
 */

public class Measurement  {

    private ArrayList<Double> temperatures = new ArrayList<>();
    private double approvedValues = 0.0;
    private LocalDate date;

    /**
     * Constructor
     * @param temperature of that specific measurement
     */
    public Measurement(Double temperature, LocalDate date) {
        this.temperatures.add(temperature); // Need to add temp to list on instantiation so it won't be lost
        this.date = date;
    }

    /**
     *
     * @return the date of the measurements
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     *
     * @return the number of measurements of that date
     */
    public int getNumberOfMeasurements() {
        return this.temperatures.size();
    }

    /**
     *
     * @return number of missing values for that date
     */
    public int getMissingValues() {
        return 24 - this.getNumberOfMeasurements();
    }

    /**
     *
     * @return all temperatures of a date added together
     */
    public double getTemperatures() {
        double temp = 0.0;
        for (double temperature : this.temperatures) {
            temp += temperature;
        }
        return temp;
    }

    /**
     *
     * @return the average temperature of a date
     */
    public Double getAverageTemperature() {
        double t = this.getTemperatures();
        double numberOfMeasurements = this.getNumberOfMeasurements();
        return t / numberOfMeasurements;
    }

    /**
     *
     * @return the number of approved measurement values of the date
     */
    public double getApprovedValues() {
        return this.approvedValues;
    }

    /**
     * Adds another approved value to the count
     */
    public void addApprovedValue() {
        this.approvedValues += 1.0;
    }

    /**
     *
     * @param temperature to be added to list of temperatures for the specific date
     */
    public void addTemperature(double temperature) {
        this.temperatures.add(temperature);
    }



}
