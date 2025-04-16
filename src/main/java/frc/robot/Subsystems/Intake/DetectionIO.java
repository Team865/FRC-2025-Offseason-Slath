package frc.robot.Subsystems.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface DetectionIO {
    @AutoLog
    public static class DetectionIOInputs {
        public boolean connected = false;
        public double distance = 0.0;
    }

    public default void updateInputs(DetectionIOInputs inputs) {}
}
