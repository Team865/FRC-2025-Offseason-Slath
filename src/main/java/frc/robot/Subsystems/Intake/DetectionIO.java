package frc.robot.Subsystems.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface DetectionIO {
    @AutoLog
    public class DetectionIOInputs {
        public boolean connected;
        public double distance;
    };

    default public void updateInputs(DetectionIOInputs inputs){}
}
