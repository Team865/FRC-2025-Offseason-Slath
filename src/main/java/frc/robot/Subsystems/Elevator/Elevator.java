// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Elevator;

import org.littletonrobotics.junction.Logger;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.Elevator.ElevatorConstants.ReefLevel;

public class Elevator extends SubsystemBase {
    private ElevatorIO elevatorIO;

    private ReefLevel targetReefLevel;
    private ElevatorIOInputsAutoLogged elevatorInputs = new ElevatorIOInputsAutoLogged();

    /** Creates a new Elevator. */
    public Elevator(ElevatorIO elevatorIO) {
        this.elevatorIO = elevatorIO;
    }

    @Override
    public void periodic() {
        elevatorIO.updateInputs(elevatorInputs);

        Logger.processInputs("Elevator", elevatorInputs);
    }
}
