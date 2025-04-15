package frc.robot.Subsystems.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface RollersIO {
    @AutoLog
    public class RollersIOInputs {
        public double volts;
        public double velocity;
        public boolean connected;
    }

    public default void setVoltage(double volts){}

    public default void updateInputs(RollersIOInputs inputs){
        // Update the inputs here
    }
}
