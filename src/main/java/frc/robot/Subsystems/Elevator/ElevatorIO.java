package frc.robot.Subsystems.Elevator;

import org.littletonrobotics.junction.AutoLog;

public interface ElevatorIO {
    @AutoLog
    public static class ElevatorIOInputs {
        public double appliedVolts = 0.0;
        public double statorCurrent = 0.0;
        public double supplyCurrent = 0.0;
        public double velocityInchesPerSecond = 0.0;
        public double positionInches = 0.0;
        public double setpointInches = 0.0;
        public boolean connected = false;
    }

    public default void setPID(double p, double i, double d) {}

    public default void setFeedforward(double s, double g, double v, double a) {}

    public default void setMotionProfile(double velocity, double acceleration, double jerk) {}

    public default void setGoal(double goal) {}

    public default void updateInputs(ElevatorIOInputs inputs) {}
}
