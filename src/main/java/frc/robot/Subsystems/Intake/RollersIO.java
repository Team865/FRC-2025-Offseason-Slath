package frc.robot.Subsystems.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface RollersIO {
    @AutoLog
    public static class RollersIOInputs {
        public double volts = 0.0;
        public double velocity = 0.0;
        public boolean connected = false;
    }

    public default void setVoltage(double volts) {}

    public default void updateInputs(RollersIOInputs inputs) {}
}
