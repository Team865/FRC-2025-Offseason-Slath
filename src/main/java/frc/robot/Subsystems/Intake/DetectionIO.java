package frc.robot.Subsystems.Intake;

import edu.wpi.first.wpilibj2.command.button.Trigger;
import org.littletonrobotics.junction.AutoLog;

public interface DetectionIO {
    @AutoLog
    public static class DetectionIOInputs {
        public boolean connected = false;
        public double distance = 0.0;
    }

    Trigger objectDetected = new Trigger(() -> true);

    public default void updateInputs(DetectionIOInputs inputs) {}
}
