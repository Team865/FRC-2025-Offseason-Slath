package frc.robot.Subsystems.Intake;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.wpilibj2.command.button.Trigger;

public interface DetectionIO {
    @AutoLog
    public static class DetectionIOInputs {
        public boolean connected = false;
        public double distance = 0.0;
    }

    Trigger objectDetected = new Trigger(null);

    public default void updateInputs(DetectionIOInputs inputs) {}
}
