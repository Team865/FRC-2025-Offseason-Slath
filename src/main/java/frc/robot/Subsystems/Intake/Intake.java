// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Intake;

import static frc.robot.Subsystems.Intake.IntakeConstants.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants;
import frc.robot.Constants.Mode;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
    private final RollersIO rollersIO;
    private final DetectionIO middleSensorIO;
    private final DetectionIO bottomSensorIO;

    public final RollersIOInputsAutoLogged rollersInputs = new RollersIOInputsAutoLogged();
    private final DetectionIOInputsAutoLogged middleSensorInputs = new DetectionIOInputsAutoLogged();
    private final DetectionIOInputsAutoLogged bottomSensorInputs = new DetectionIOInputsAutoLogged();
    private final double intakingVoltage = 6.0;

    /** Creates a new Intake. */
    public Intake(RollersIO rollersIO, DetectionIO middleSensorIO, DetectionIO bottomSensorIO) {
        this.rollersIO = rollersIO;
        this.middleSensorIO = middleSensorIO;
        this.bottomSensorIO = bottomSensorIO;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        this.rollersIO.updateInputs(this.rollersInputs);
        this.middleSensorIO.updateInputs(this.middleSensorInputs);
        this.bottomSensorIO.updateInputs(this.bottomSensorInputs);

        Logger.processInputs("Intake/Rollers", rollersInputs);
        Logger.processInputs("Intake/MiddleSensors", middleSensorInputs);
        Logger.processInputs("Intake/BottomSensors", bottomSensorInputs);
    }

    // General control commands
    public Command setRollers(double volts) {
        return this.runOnce(() -> rollersIO.setVoltage(volts));
    }

    public Command setRollers() {
        return this.setRollers(intakingVoltage);
    }

    public Command runRollers(double volts) {
        return this.startEnd(
                () -> {
                    System.out.println("RUNNING ROLLERS");
                    rollersIO.setVoltage(volts);
                },
                () -> {
                    System.out.println("STOPPING ROLLERS");
                    rollersIO.setVoltage(0.0);
                });
    }

    public Command runRollers() {
        return this.runRollers(intakingVoltage);
    }

    // Logical methods
    public Trigger middleSensorIsDetecting() {
        return new Trigger(() -> middleSensorInputs.distanceMM <= MIDDLE_SENSOR_MAX_DIST_MM);
    }

    public Trigger bottomSensorIsDetecting() {
        return new Trigger(() -> bottomSensorInputs.distanceMM <= BOTTOM_SENSOR_MAX_DIST_MM);
    }

    private Command intakeSensorSimulation() {
        return Commands.runOnce(() -> {
                    // Set distance to beyond the detection threshold
                    middleSensorInputs.distanceMM = MIDDLE_SENSOR_MAX_DIST_MM + 1;
                    bottomSensorInputs.distanceMM = BOTTOM_SENSOR_MAX_DIST_MM + 1;
                })
                .andThen(new WaitCommand(0.5))
                .andThen(() -> {
                    // Set distance back to within the threshold
                    middleSensorInputs.distanceMM = 0;
                    bottomSensorInputs.distanceMM = 0;
                });
    }

    public Command intake() {
        return (
                // Simulate the sensors if necessary
                Constants.currentMode == Mode.SIM ? this.intakeSensorSimulation() : Commands.none()
                // Intake logic
                )
                .alongWith(this.runRollers().until(middleSensorIsDetecting().and(bottomSensorIsDetecting())));
    }

    public Command outake() {
        return this.runOnce(() -> {
            System.out.println("RUNNING ROLLERS (OUTAKE)");
            this.runRollers(0.0);
        });
        // .until(() -> MathUtil.isNear(
        //         1, this.bottomSensorInputs.distance, 1)) // Tolerance and expected values are placeholders
        // .until(() -> !MathUtil.isNear(
        //         1, this.bottomSensorInputs.distance, 1)) // Wait until the sensor stops detecting the coral
        // .andThen(() -> {
        //     System.out.println("STOP RUNNING ROLLERS");
        // });
    }
}
