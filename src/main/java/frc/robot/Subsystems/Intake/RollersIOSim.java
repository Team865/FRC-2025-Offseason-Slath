package frc.robot.Subsystems.Intake;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class RollersIOSim implements RollersIO {
    private DCMotorSim sim = new DCMotorSim(
            LinearSystemId.createDCMotorSystem(DCMotor.getKrakenX60(1), 0.004, 2), DCMotor.getKrakenX60(1));

    private double appliedVolts = 0.0;

    @Override
    public void updateInputs(RollersIOInputs inputs) {
        sim.setInputVoltage(appliedVolts);
        sim.update(0.020);

        inputs.positionRot = sim.getAngularPositionRotations();
        inputs.velocityRotPerSecond = sim.getAngularVelocityRPM();
        inputs.currentAmps = sim.getCurrentDrawAmps();
        inputs.appliedVolts = appliedVolts;
    }

    @Override
    public void setVoltage(double volts) {
        appliedVolts = MathUtil.clamp(volts, -12.0, 12.0);
    }
}
