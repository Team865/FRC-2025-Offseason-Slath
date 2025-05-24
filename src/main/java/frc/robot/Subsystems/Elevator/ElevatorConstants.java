package frc.robot.Subsystems.Elevator;

public final class ElevatorConstants {
    public enum ReefLevel {
        L1(0),
        L2(11.25),
        L3(18.875),
        L4(28.5);

        private final double distanceInches;
        private ReefLevel(double distanceInches){
            this.distanceInches = distanceInches;
        }

        public double getDistanceInches(){
            return this.distanceInches;
        }
    }
}
