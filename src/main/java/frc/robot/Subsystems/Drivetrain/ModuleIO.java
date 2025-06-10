// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Drivetrain;

import org.littletonrobotics.junction.AutoLog;

import edu.wpi.first.math.geometry.Rotation2d;

public interface ModuleIO {
    @AutoLog
    public static class ModuleIOInputs {
        public boolean connected = false;
        public double drivePositionMeters = 0.0;
        public double driveVelocityMetersPerSec = 0.0;

        public Rotation2d turnPosition = Rotation2d.kZero;
    }

    public default void updateInputs(ModuleIOInputs inputs) {}
}
