// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private RollersIO rollersIO;
    private DetectionIO detectionIO;

    /** Creates a new Intake. */
    public Intake(RollersIO rollersIO, DetectionIO detectionIO) {
        this.rollersIO = rollersIO;
        this.detectionIO = detectionIO;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public Command intake(){
        return this.runOnce(() -> {
            System.out.println("INTAKE COMMAND WAS CALLED");
        }).until(() -> (false)); // Replace this with sensor logic once sensor io is completed
    }
}
