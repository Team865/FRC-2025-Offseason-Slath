// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Elevator;

import static frc.robot.Subsystems.Elevator.ElevatorConstants.*;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LoggedTunableNumber;
import org.littletonrobotics.junction.Logger;

public class Elevator extends SubsystemBase {
    private ElevatorIO elevatorIO;

    private ReefLevel targetReefLevel = ReefLevel.L1;
    private ElevatorIOInputsAutoLogged elevatorInputs = new ElevatorIOInputsAutoLogged();

    // Control System Tuning
    private final LoggedTunableNumber kP = new LoggedTunableNumber("Elevator/Feedback/P", pidGains.kP());
    private final LoggedTunableNumber kI = new LoggedTunableNumber("Elevator/Feedback/I", pidGains.kI());
    private final LoggedTunableNumber kD = new LoggedTunableNumber("Elevator/Feedback/D", pidGains.kD());

    /** Creates a new Elevator. */
    public Elevator(ElevatorIO elevatorIO) {
        this.elevatorIO = elevatorIO;

        elevatorIO.setPID(kP.get(), kI.get(), kD.get());
    }

    @Override
    public void periodic() {
        elevatorIO.updateInputs(elevatorInputs);

        LoggedTunableNumber.ifChanged(
                hashCode(),
                () -> {
                    elevatorIO.setPID(kP.get(), kI.get(), kD.get());
                },
                kP,
                kI,
                kD);

        Logger.processInputs("Elevator", elevatorInputs);
    }

    public Command setReefLevel(ReefLevel level) {
        return this.runOnce(() -> this.targetReefLevel = level);
    }

    /** Extends elevator to target Reef Level */
    public Command extend() {
        return this.runOnce(() -> this.elevatorIO.setGoal(targetReefLevel.getDistanceInches()));
    }

    /** Retracts elevator to zero */
    public Command retract() {
        return this.runOnce(() -> this.elevatorIO.setGoal(0));
    }
}
