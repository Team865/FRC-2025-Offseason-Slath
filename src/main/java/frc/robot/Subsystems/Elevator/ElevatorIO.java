package frc.robot.Subsystems.Elevator;

import org.littletonrobotics.junction.AutoLog;

public interface ElevatorIO {
    @AutoLog
    public static class ElevatorIOInputs {
        public double appliedVolts = 0.0;
        public double currentAmps = 0.0;
        public double velocityRotPerSecond = 0.0;
        public double positionRot = 0.0;
        public boolean connected = false;
    }

    public default void setPID(int p, int i, int d) {
        // Do nothing
    }
}
