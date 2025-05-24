// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Elevator;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Subsystems.Elevator.ElevatorConstants.ReefLevel;
import org.littletonrobotics.junction.Logger;

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

    public Command setReefLevel(ReefLevel level) {
        return this.runOnce(() -> this.targetReefLevel = level);
    }

    /** Extends elevator to target Reef Level, and retract back to 0 when stopped */
    public Command runExtension() {
        return this.startEnd(
                () -> this.elevatorIO.setGoal(targetReefLevel.getDistanceInches()), () -> this.elevatorIO.setGoal(0));
    }
}
