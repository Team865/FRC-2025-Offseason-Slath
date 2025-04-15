package frc.robot.Subsystems.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface RollersIO {
    @AutoLog
    public class RollerInputs {
        public double volts;
        public double velocity;
    }

    default void setVoltage(double volts){}
}
