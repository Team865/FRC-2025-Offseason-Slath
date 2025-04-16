// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Intake;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final RollersIO rollersIO;
    private final DetectionIO middleSensorIO;
    private final DetectionIO bottomSensorIO;

    public final RollersIOInputsAutoLogged rollersInputs = new RollersIOInputsAutoLogged();
    private final DetectionIOInputsAutoLogged middleSensorInputs = new DetectionIOInputsAutoLogged();
    private final DetectionIOInputsAutoLogged bottomSensorInputs = new DetectionIOInputsAutoLogged();

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

    public Command intake() {
        return this.runOnce(() -> {
                    System.out.println("INTAKE COMMAND WAS CALLED");
                })
                .until(() -> (false)); // Replace this with sensor logic once sensor io is completed
    }
}
