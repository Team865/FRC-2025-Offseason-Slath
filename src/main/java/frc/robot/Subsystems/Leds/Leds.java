// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Leds;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.littletonrobotics.junction.AutoLogOutput;

public class Leds extends SubsystemBase {
    private Spark blinkin;

    @Getter
    @AutoLogOutput(key = "LED/Color")
    private SparkColor color;

    private SparkColor defaultColor = SparkColor.WHITE;

    @RequiredArgsConstructor
    public enum SparkColor {
        HOT_PINK(0.57),
        DARK_RED(0.59),
        RED(0.61),
        RED_ORANGE(0.63),
        ORANGE(0.65),
        GOLD(0.67),
        YELLOW(0.69),
        LAWN_GREEN(0.71),
        LIME(0.73),
        DARK_GREEN(0.75),
        GREEN(0.77),
        BLUE_GREEN(0.79),
        AQUA(0.81),
        SKY_BLUE(0.83),
        DARK_BLUE(0.85),
        BLUE(0.87),
        BLUE_VIOLET(0.89),
        VIOLET(0.91),
        WHITE(0.93),
        GRAY(0.95),
        DARK_GRAY(0.97),
        BLACK(0.99);

        public final double sparkOutput;
    }

    public enum LEDState {
        INTAKING,
        SHOOTING,
        CLIMBING
    }

    @AutoLogOutput(key = "LED/State")
    private LEDState currentLEDState = LEDState.INTAKING;

    public Leds(int port) {
        blinkin = new Spark(port);

        // defaultColor = DriverStation.getAlliance()
        //         .map(alliance -> alliance == Alliance.Blue ? SparkColor.BLUE : SparkColor.RED)
        //         .orElse(SparkColor.WHITE);

        color = defaultColor;
    }

    @Override
    public void periodic() {
        blinkin.set(color.sparkOutput);
    }

    private void solidColor(SparkColor color) {
        this.color = color;
    }

    private void blinkColor(SparkColor color, double interval) {
        boolean on = ((Timer.getFPGATimestamp() % interval) / interval) > 0.5;
        solidColor(on ? color : SparkColor.BLACK);
    }

    public Command solidColorCommand(SparkColor color) {
        return this.runOnce(() -> solidColor(color));
    }

    public Command blinkColorCommand(SparkColor color, double interval, double duration) {
        return this.run(() -> blinkColor(color, interval))
                .withTimeout(duration)
                .finallyDo(() -> solidColor(defaultColor));
    }

    public Command setLEDState(LEDState state) {
        return this.runOnce(() -> currentLEDState = state);
    }
}