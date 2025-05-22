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
import frc.robot.Robot;
import frc.robot.util.LoggedTunableNumber;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Intake extends SubsystemBase {
    private final RollersIO rollersIO;
    private final DetectionIO middleSensorIO;
    private final DetectionIO bottomSensorIO;

    public final RollersIOInputsAutoLogged rollersInputs = new RollersIOInputsAutoLogged();
    private final DetectionIOInputsAutoLogged middleSensorInputs = new DetectionIOInputsAutoLogged();
    private final DetectionIOInputsAutoLogged bottomSensorInputs = new DetectionIOInputsAutoLogged();
    private final double intakingVoltage = -7.5;

    private final LoggedTunableNumber MIDDLE_SENSOR_THRESHOLD_TUNABLE =
            new LoggedTunableNumber("Intake/MiddleSensorThresholdMM", MIDDLE_SENSOR_MAX_DIST_MM);
    private final LoggedTunableNumber BOTTOM_SENSOR_THRESHOLD_TUNABLE =
            new LoggedTunableNumber("Intake/BottomSensorThresholdMM", BOTTOM_SENSOR_MAX_DIST_MM);
    private final LoggedTunableNumber COMBINED_SENSOR_THRESHOLD_TUNABLE =
            new LoggedTunableNumber("Intake/CombinedSensorThresholdMM", COMBINED_MAX_DIST_MM);

    /** Creates a new Intake subsystem.
     * @see #intake()
     * @see #outakeBottom()
     * @see #outakeTop()
     */
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
    /** A command that sets rollers' voltage to a specific voltage level
     * @param   volts   The voltage value to apply (should range between -12.0 and 12.0)
     * @return          The command that sets the roller voltage
     */
    public Command setRollers(double volts) {
        return this.runOnce(() -> rollersIO.setVoltage(volts));
    }

    /** Calls {@link #setRollers(double volts)} at a default voltage
     * @see #setRollers(double volts)
     * @return The Command from {@link #setRollers(double volts)}
     * */
    public Command setRollers() {
        return this.setRollers(intakingVoltage);
    }

    /** A command that sets the rollers' voltage to a specific voltage level,
     * and stops the rollers when the command is interrupted
     * @param   volts   The voltage value to apply (should range between -12.0 and 12.0)
     * @return          The associated command
     * */
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

    /** Calls {@link #runRollers(double volts)} at a default voltage
     * @see #runRollers(double volts)
     * @return The Command from {@link #runRollers(double volts)}
     * */
    public Command runRollers() {
        return this.runRollers(intakingVoltage);
    }

    @AutoLogOutput
    // Logical methods
    public Trigger middleSensorIsDetecting() {
        return new Trigger(() -> middleSensorInputs.distanceMM <= MIDDLE_SENSOR_THRESHOLD_TUNABLE.get());
    }

    public Trigger bothSensorsAreDetecting() {
        return middleSensorIsDetecting().and(bottomSensorIsDetecting());
    }

    public Trigger combinedSensorIsDetecting() {
        return new Trigger(() -> (middleSensorInputs.distanceMM + bottomSensorInputs.distanceMM)
                <= COMBINED_SENSOR_THRESHOLD_TUNABLE.get());
    }

    @AutoLogOutput
    public Trigger bottomSensorIsDetecting() {
        return new Trigger(() -> bottomSensorInputs.distanceMM <= BOTTOM_SENSOR_THRESHOLD_TUNABLE.get());
    }

    /** Simulates middle and bottom sensors for SIM mode intake.
     * The returned Command should be ran using the alongWith() decorator to run in parallel to the logic code.
     * @return The Command that executes the simulation
     * @see #outakeSensorSimulation()
     */
    private Command intakeSensorSimulation() {
        // Return no command if the robot isn't in sim
        if (!Robot.isSimulation()) return Commands.none();

        setRollers(intakingVoltage);

        return Commands.runOnce(() -> {
                    // Set distance to beyond the detection threshold
                    middleSensorInputs.distanceMM = (int) MIDDLE_SENSOR_THRESHOLD_TUNABLE.get() + 1;
                    bottomSensorInputs.distanceMM = (int) BOTTOM_SENSOR_THRESHOLD_TUNABLE.get() + 1;
                })
                .andThen(new WaitCommand(0.5))
                .andThen(() -> {
                    // Set distance back to within the threshold
                    middleSensorInputs.distanceMM = 0;
                    bottomSensorInputs.distanceMM = 0;
                });
    }

    /** Simulates middle and bottom sensors for SIM mode outake.
     * The returned Command should be ran using the alongWith() decorator to run in parallel to the logic code.
     * @return The Command that executes the simulation
     * @see #intakeSensorSimulation()
     */
    private Command outakeSensorSimulation() {
        // Return no command if the robot isn't in sim
        if (!Robot.isSimulation()) return Commands.none();

        return Commands.runOnce(() -> {
                    // Set distance to within the sensor threshold
                    middleSensorInputs.distanceMM = 0;
                    bottomSensorInputs.distanceMM = 0;
                })
                .andThen(new WaitCommand(0.3))
                .andThen(() -> {
                    // Set distance to beyond the detection threshold
                    middleSensorInputs.distanceMM = (int) MIDDLE_SENSOR_THRESHOLD_TUNABLE.get() + 1;
                    bottomSensorInputs.distanceMM = (int) BOTTOM_SENSOR_THRESHOLD_TUNABLE.get() + 1;
                });
    }

    /** The intake command for the robot
     * @return The Command for the intake
     * @see #outakeBottom()
     * @see #outakeTop()
     */
    public Command intake() {
        return (
                // Simulate the sensors if necessary
                Robot.isSimulation() ? this.intakeSensorSimulation() : Commands.none()
                // Run rollers until both sensors detect the coral
                )
                .alongWith(this.runRollers().until(bothSensorsAreDetecting()));
    }

    /** Command to outake the coral out the bottom.
     * Should be used for L2, L3, and L4
     * @return The command for the outake
     * @see #outakeTop()
     * @see #intake()
     */
    public Command outakeBottom() {
        return (
                // Simulate sensors if necessary
                Robot.isSimulation() ? this.outakeSensorSimulation() : Commands.none()
                // Run rollers until the bottom sensor no longer detects the coral
                )
                .alongWith(this.runRollers().until(bottomSensorIsDetecting().negate()));
    }

    /** Command to outake the coral out the top.
     * Should be used for L1
     * @return The command for the outake
     * @see #outakeBottom()
     * @see #intake()
     */
    public Command outakeTop() {
        return (
                // Simulate sensors if necessary
                Robot.isSimulation() ? this.outakeSensorSimulation() : Commands.none()
                // Run rollers in inverse until the middle sensor no longer detects the coral
                )
                .alongWith(this.runRollers(-intakingVoltage)
                        .until(middleSensorIsDetecting().negate()));
    }
}
