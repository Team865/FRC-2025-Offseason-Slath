package frc.robot.Subsystems.Drivetrain;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;

import frc.robot.util.PhoenixUtil;

public class ModuleIOTalonFX implements ModuleIO {
    private final TalonFX driveTalon;
    private final TalonFX turnTalon;
    private final CANcoder encoder;

    private final VelocityVoltage velocityRequest = new VelocityVoltage(0.0);

    public ModuleIOTalonFX(SwerveModuleConstants<TalonFXConfiguration, TalonFXConfiguration, CANcoderConfiguration> constants, String canBus){
        driveTalon = new TalonFX(constants.DriveMotorId, canBus);
        turnTalon = new TalonFX(constants.SteerMotorId, canBus);
        encoder = new CANcoder(constants.EncoderId, canBus);

        TalonFXConfiguration driveConfig = constants.DriveMotorInitialConfigs;
        driveConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        driveConfig.Slot0 = constants.DriveMotorGains;
        driveConfig.Feedback.SensorToMechanismRatio = constants.DriveMotorGearRatio;
        driveConfig.TorqueCurrent.PeakForwardTorqueCurrent = constants.SlipCurrent;
        driveConfig.TorqueCurrent.PeakReverseTorqueCurrent = -constants.SlipCurrent;
        driveConfig.CurrentLimits.StatorCurrentLimit = constants.SlipCurrent;
        driveConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        driveConfig.MotorOutput.Inverted = constants.DriveMotorInverted ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;

        TalonFXConfiguration turnConfig = constants.SteerMotorInitialConfigs;
        turnConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        turnConfig.Slot0 = constants.SteerMotorGains;
        turnConfig.Feedback.RotorToSensorRatio = constants.SteerMotorGearRatio;
        turnConfig.TorqueCurrent.PeakForwardTorqueCurrent = constants.SlipCurrent;
        turnConfig.TorqueCurrent.PeakReverseTorqueCurrent = -constants.SlipCurrent;
        turnConfig.CurrentLimits.StatorCurrentLimit = constants.SlipCurrent;
        turnConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        turnConfig.MotorOutput.Inverted = constants.SteerMotorInverted ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;

        CANcoderConfiguration encoderConfig = constants.EncoderInitialConfigs;
        encoderConfig.MagnetSensor.MagnetOffset = constants.EncoderOffset;
        encoderConfig.MagnetSensor.SensorDirection = constants.EncoderInverted ? SensorDirectionValue.Clockwise_Positive : SensorDirectionValue.CounterClockwise_Positive;

        PhoenixUtil.tryUntilOk(5, () -> driveTalon.getConfigurator().apply(driveConfig, 0.25));
        PhoenixUtil.tryUntilOk(5, () -> turnTalon.getConfigurator().apply(turnConfig, 0.25));
        PhoenixUtil.tryUntilOk(5, () -> encoder.getConfigurator().apply(encoderConfig, 0.25));
    }
}
