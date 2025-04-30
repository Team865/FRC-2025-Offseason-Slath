package frc.robot.Subsystems.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface RollersIO {
    @AutoLog
    public static class RollersIOInputs {
        public double appliedVolts = 0.0;
        public double currentAmps = 0.0;
        public double velocityRotPerSecond = 0.0;
        public double positionRot = 0.0;
        public boolean connected = false;
    }

    public default void setVoltage(double volts) {}

    public default void updateInputs(RollersIOInputs inputs) {}
}
