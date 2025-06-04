package frc.robot.Subsystems.Elevator;

public final class ElevatorConstants {
    public enum ReefLevel {
        L1(0.5),
        L2(11.25),
        L3(18.875),
        L4(28.5);

        private final double distanceInches;

        private ReefLevel(double distanceInches) {
            this.distanceInches = distanceInches;
        }

        public double getDistanceInches() {
            return this.distanceInches;
        }
    }

    public static final int MAIN_TALON_ID = 11;
    public static final int FOLLOWER_TALON_ID = 12;

    public static final double DRUM_RADIUS_METERS = 0.048514 / 2;
    public static final double GEAR_RATIO = 80 / 16;

    public static record PIDGains(double kP, double kI, double kD) {}

    public static record FeedforwardGains(double kS, double kG, double kV, double kA) {}

    public static record MotionProfile(double cruiseVelocity, double acceleration, double jerk) {}
    ;

    public static final PIDGains pidGains = new PIDGains(100, 0, 0);
    public static final FeedforwardGains feedforwardGains = new FeedforwardGains(0.24, 0.56, 4.44, 0.07);
    public static final MotionProfile motionProfile = new MotionProfile(80, 200, 200);
}
