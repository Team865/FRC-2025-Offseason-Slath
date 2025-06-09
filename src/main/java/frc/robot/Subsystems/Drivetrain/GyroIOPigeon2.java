package frc.robot.Subsystems.Drivetrain;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.MountPoseConfigs;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.util.PhoenixUtil;

public class GyroIOPigeon2 implements GyroIO {
    private final Pigeon2 pigeonGyro;

    private final StatusSignal<Angle> yawRad;
    private final StatusSignal<AngularVelocity> yawVelocityRadPerSec;

    public GyroIOPigeon2(int id, String canBus) {
        pigeonGyro = new Pigeon2(id, canBus);

        Pigeon2Configuration config = new Pigeon2Configuration();
        // gyro is mounted backwards from robot front
        config.MountPose = new MountPoseConfigs().withMountPoseYaw(180);

        PhoenixUtil.tryUntilOk(5, () -> pigeonGyro.getConfigurator().apply(config, 0.25));
        PhoenixUtil.tryUntilOk(5, () -> pigeonGyro.setYaw(0.0));

        yawRad = pigeonGyro.getYaw();
        yawVelocityRadPerSec = pigeonGyro.getAngularVelocityZWorld();

        BaseStatusSignal.setUpdateFrequencyForAll(50.0, yawRad, yawVelocityRadPerSec);

        pigeonGyro.optimizeBusUtilization();
    }

    @Override
    public void updateInputs(GyroIOInputs inputs) {
        inputs.connected = BaseStatusSignal.refreshAll(yawRad, yawVelocityRadPerSec).equals(StatusCode.OK);
        inputs.yawRotation2d = Rotation2d.fromDegrees(Units.degreesToRadians(yawRad.getValueAsDouble()));
    }
}
