package frc.robot.Subsystems.Drivetrain;

import edu.wpi.first.math.geometry.Rotation2d;
import org.littletonrobotics.junction.AutoLog;

public interface GyroIO {
    @AutoLog
    public static class GyroIOInputs {
        public boolean connected = false;
        public Rotation2d yawRotation2d = new Rotation2d();
        public double yawVelocityRadPerSec = 0.0;
    }

    public default void updateInputs(GyroIOInputs inputs) {}
}
