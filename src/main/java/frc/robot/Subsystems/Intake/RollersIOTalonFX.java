package frc.robot.Subsystems.Intake;

import static frc.robot.Subsystems.Intake.IntakeConstants.*;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.util.PhoenixUtil;

public class RollersIOTalonFX implements RollersIO {
    private final TalonFX motor = new TalonFX(ROLLERS_ID);
    
    private final StatusSignal<Voltage> appliedVolts = motor.getMotorVoltage();
    private final VoltageOut voltageRequest = new VoltageOut(0.0);

    public RollersIOTalonFX() {
        var config = new TalonFXConfiguration();

        PhoenixUtil.tryUntilOk(5, () -> motor.getConfigurator().apply(config, 0.25));

        BaseStatusSignal.setUpdateFrequencyForAll(50.0, appliedVolts);
        motor.optimizeBusUtilization();
    }

    @Override
    public void updateInputs(RollersIOInputs inputs) {
        BaseStatusSignal.refreshAll(appliedVolts);

        inputs.appliedVolts = appliedVolts.getValueAsDouble();
    }

    @Override
    public void setVoltage(double volts) {
        motor.setControl(voltageRequest.withOutput(volts));
    }
}
