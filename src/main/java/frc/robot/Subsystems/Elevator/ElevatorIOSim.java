package frc.robot.Subsystems.Elevator;

import edu.wpi.first.math.controller.PIDController;

public class ElevatorIOSim implements ElevatorIO {
    private final PIDController pidController = new PIDController(0, 0, 0);
    private double goal = 0.0;

    @Override
    public void setGoal(double goal) {
        this.goal = goal;
    }

    @Override
    public void setPID(double p, double i, double d) {
        pidController.setP(p);
        pidController.setI(i);
        pidController.setD(d);
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
        // TODO Auto-generated method stub
        ElevatorIO.super.updateInputs(inputs);
    }
}
