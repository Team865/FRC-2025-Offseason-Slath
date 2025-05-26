package frc.robot.Subsystems.Elevator;

import static frc.robot.Subsystems.Elevator.ElevatorConstants.DRUM_RADIUS_METERS;
import static frc.robot.Subsystems.Elevator.ElevatorConstants.FOLLOWER_TALON_ID;
import static frc.robot.Subsystems.Elevator.ElevatorConstants.GEAR_RATIO;
import static frc.robot.Subsystems.Elevator.ElevatorConstants.MAIN_TALON_ID;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DynamicMotionMagicVoltage;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.util.PhoenixUtil;

public class ElevatorIOTalonFX implements ElevatorIO {
    private final TalonFX mainTalon = new TalonFX(MAIN_TALON_ID, "Drivetrain");
    private final TalonFX followerTalon = new TalonFX(FOLLOWER_TALON_ID, "Drivetrain");
    private final TalonFXConfiguration talonConfig = new TalonFXConfiguration();

    private final MotionMagicVoltage motionRequest = new MotionMagicVoltage(0.0).withEnableFOC(true).withUpdateFreqHz(50.0);
    private final StatusSignal<Voltage> appliedVolts = mainTalon.getMotorVoltage();
    private final StatusSignal<Current> supplyCurrent = mainTalon.getSupplyCurrent();
    private final StatusSignal<Current> statorCurrent = mainTalon.getStatorCurrent();

    // These are in inches, not rotations
    private final StatusSignal<Angle> position = mainTalon.getPosition();
    private final StatusSignal<AngularVelocity> velocity = mainTalon.getVelocity();

    public ElevatorIOTalonFX(){
        talonConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        talonConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        talonConfig.Feedback.SensorToMechanismRatio = GEAR_RATIO / (2 * Math.PI * DRUM_RADIUS_METERS);
        talonConfig.Slot0.GravityType = GravityTypeValue.Elevator_Static;

        // Try to apply config
        PhoenixUtil.tryUntilOk(5, () -> mainTalon.getConfigurator().apply(talonConfig));
        PhoenixUtil.tryUntilOk(5, () -> followerTalon.getConfigurator().apply(talonConfig));
        PhoenixUtil.tryUntilOk(5, () -> mainTalon.setControl(new Follower(MAIN_TALON_ID, true)));

        ParentDevice.optimizeBusUtilizationForAll(mainTalon, followerTalon);
        BaseStatusSignal.setUpdateFrequencyForAll(
            50.0, 
            appliedVolts,
            supplyCurrent,
            statorCurrent,
            position,
            velocity
        );
    }

    @Override
    public void setFeedforward(double s, double g, double v, double a) {
        talonConfig.Slot0.kS = s;
        talonConfig.Slot0.kG = g;
        talonConfig.Slot0.kV = v;
        talonConfig.Slot0.kA = a;

        PhoenixUtil.tryUntilOk(5, () -> mainTalon.getConfigurator().apply(talonConfig));
        PhoenixUtil.tryUntilOk(5, () -> followerTalon.getConfigurator().apply(talonConfig));
    }

    @Override
    public void setPID(double p, double i, double d) {
        talonConfig.Slot0.kP = p;
        talonConfig.Slot0.kI = i;
        talonConfig.Slot0.kD = d;

        PhoenixUtil.tryUntilOk(5, () -> mainTalon.getConfigurator().apply(talonConfig));
        PhoenixUtil.tryUntilOk(5, () -> followerTalon.getConfigurator().apply(talonConfig));
    }

    @Override
    public void setGoal(double goal) {
        mainTalon.setControl(motionRequest.withPosition(goal));
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
        BaseStatusSignal.refreshAll(appliedVolts);

        inputs.appliedVolts = appliedVolts.getValueAsDouble();
        inputs.supplyCurrent = supplyCurrent.getValueAsDouble();
        inputs.statorCurrent = statorCurrent.getValueAsDouble();
        inputs.positionInches = position.getValueAsDouble();
        inputs.velocityInchesPerSecond = velocity.getValueAsDouble();
    }
}
