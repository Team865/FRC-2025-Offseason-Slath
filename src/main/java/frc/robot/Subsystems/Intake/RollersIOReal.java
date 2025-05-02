package frc.robot.Subsystems.Intake;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.measure.Voltage;
import static frc.robot.Subsystems.Intake.IntakeConstants.*;

public class RollersIOReal implements RollersIO {
    private final TalonFX motor = new TalonFX(ROLLERS_ID);
    private final StatusSignal<Voltage> appliedVolts = motor.getMotorVoltage();

    private final VoltageOut voltageRequest = new VoltageOut(0.0);

    public RollersIOReal() {
        var config = new TalonFXConfiguration();

        motor.getConfigurator().apply(config);
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
