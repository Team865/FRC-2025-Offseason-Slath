package frc.robot.Subsystems.Elevator;

import static frc.robot.Subsystems.Elevator.ElevatorConstants.DRUM_RADIUS_METERS;
import static frc.robot.Subsystems.Elevator.ElevatorConstants.FOLLOWER_TALON_ID;
import static frc.robot.Subsystems.Elevator.ElevatorConstants.GEAR_RATIO;
import static frc.robot.Subsystems.Elevator.ElevatorConstants.MAIN_TALON_ID;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.units.measure.Voltage;

public class ElevatorIOTalonFX implements ElevatorIO {
    private final TalonFX mainTalon = new TalonFX(MAIN_TALON_ID);
    private final TalonFX followerTalon = new TalonFX(FOLLOWER_TALON_ID);

    private final StatusSignal<Voltage> appliedVolts = mainTalon.getMotorVoltage();
    private final VoltageOut voltageRequest = new VoltageOut(0.0);

    public ElevatorIOTalonFX(){
        var config = new TalonFXConfiguration();

        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        config.Feedback.SensorToMechanismRatio = GEAR_RATIO / (2 * Math.PI * DRUM_RADIUS_METERS);
    }

    @Override
    public void setFeedforward(double s, double g, double v, double a) {
        // TODO Auto-generated method stub
        ElevatorIO.super.setFeedforward(s, g, v, a);
    }

    @Override
    public void setGoal(double goal) {
        // TODO Auto-generated method stub
        ElevatorIO.super.setGoal(goal);
    }

    @Override
    public void setPID(double p, double i, double d) {
        // TODO Auto-generated method stub
        ElevatorIO.super.setPID(p, i, d);
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
        // TODO Auto-generated method stub
        ElevatorIO.super.updateInputs(inputs);
    }

}
