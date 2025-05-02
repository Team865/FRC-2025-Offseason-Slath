package frc.robot.Subsystems.Intake;

import au.grapplerobotics.ConfigurationFailedException;
import au.grapplerobotics.LaserCan;
import au.grapplerobotics.interfaces.LaserCanInterface.Measurement;

public class DetectionIOLaserCan implements DetectionIO {
    LaserCan sensor;

    public DetectionIOLaserCan(int deviceId) {
        sensor = new LaserCan(deviceId);

        try {
            sensor.setRangingMode(LaserCan.RangingMode.SHORT);
            sensor.setRegionOfInterest(new LaserCan.RegionOfInterest(8, 8, 16, 16));
            sensor.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_20MS);
        } catch (ConfigurationFailedException e){
            System.out.println("LaserCan configuration failed. " + e);
        }
    }

    @Override
    public void updateInputs(DetectionIOInputs inputs) {
        Measurement measurement = sensor.getMeasurement();

        // Guard clauses
        if(measurement == null){
            System.out.println("Measurement could not be found.");
            return;
        } else if(measurement.status != LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT){
            System.out.println("Could not get a valid measurement");
            return;
        }

        // Measurement
        inputs.distanceMM = measurement.distance_mm;
    }
}
