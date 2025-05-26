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

    public static record PIDGains(double kP, double kI, double kD) {}

    public static record FeedforwardGains(double kS, double kG, double kV, double kA) {}

    public static final PIDGains pidGains = new PIDGains(2, 0.3, 0);
    public static final FeedforwardGains feedforwardGains = new FeedforwardGains(0, 0, 0, 0);
}
