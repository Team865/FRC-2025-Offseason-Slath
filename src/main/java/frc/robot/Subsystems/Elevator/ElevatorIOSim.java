package frc.robot.Subsystems.Elevator;

import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.ElevatorSim;

public class ElevatorIOSim implements ElevatorIO {
    private final ProfiledPIDController pidController =
            new ProfiledPIDController(0, 0, 0, new TrapezoidProfile.Constraints(30, 15));
    private final ElevatorFeedforward feedforwardController = new ElevatorFeedforward(0, 0, 0, 0);
    private double appliedVolts = 0.0;
    private double goalInches = 0.0;

    private ElevatorSim elevatorSim = new ElevatorSim(
            4.62, 0.07, DCMotor.getKrakenX60(2), Units.inchesToMeters(0), Units.inchesToMeters(29), true, 0);

    @Override
    public void setGoal(double goal) {
        this.goalInches = goal;
    }

    @Override
    public void setPID(double p, double i, double d) {
        pidController.setP(p);
        pidController.setI(i);
        pidController.setD(d);
    }

    @Override
    public void setFeedforward(double s, double g, double v, double a) {
        feedforwardController.setKs(s);
        feedforwardController.setKg(g);
        feedforwardController.setKv(v);
        feedforwardController.setKa(a);
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
        appliedVolts = pidController.calculate(inputs.positionInches, goalInches);
        appliedVolts += feedforwardController.calculate(pidController.getSetpoint().velocity);
        elevatorSim.setInputVoltage(appliedVolts);
        elevatorSim.update(0.02);

        inputs.setpointInches = goalInches;
        inputs.appliedVolts = appliedVolts;
        inputs.supplyCurrent = elevatorSim.getCurrentDrawAmps();
        inputs.statorCurrent = elevatorSim.getCurrentDrawAmps();
        inputs.positionInches = Units.metersToInches(elevatorSim.getPositionMeters());
        inputs.velocityInchesPerSecond = Units.metersToInches(elevatorSim.getVelocityMetersPerSecond());
    }
}
