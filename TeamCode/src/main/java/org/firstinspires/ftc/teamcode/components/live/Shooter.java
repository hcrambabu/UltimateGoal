package org.firstinspires.ftc.teamcode.components.live;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;

@Config
class ShooterConfig {
    public static PIDCoefficients flywheel_pid_coeffs = new PIDCoefficients(250, 2, 30);

    public static int INTAKE_MOTOR_RPM = 6000;
    public static int INTAKE_MOTOR_COUNTS_PER_REV = 28;
    public static int INTAKE_MOTOR_MAX_COUNT_PER_SEC = (INTAKE_MOTOR_RPM * INTAKE_MOTOR_COUNTS_PER_REV)/60; // MAX Speed

    public static double target_speed = (INTAKE_MOTOR_MAX_COUNT_PER_SEC * 0.555); // counts per second

    public static double shoot_trigger_pos = 0.0;
    public static double unshoot_trigger_pos = 0.35;

    public static double VELOCITY_ERROR = 100.0;
}

public class Shooter extends Component {

    //// MOTORS ////
    private DcMotorEx flywheel;     // Flywheel

    //// SERVOS ////
    private Servo cage;
    private boolean useHardWait = true;
    private long hardWaitMills = 800;

    {
        name = "Shooter";
    }

    public Shooter(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        //// MOTORS ////
        flywheel    = hwmap.get(DcMotorEx.class, "flywheel");

        //// SERVOS ////
        cage        = hwmap.get(Servo.class, "cage");
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("SHOOTER FLWH", robot.bulk_data_2.getMotorVelocity(flywheel));
        telemetry.addData("SHOOTER TRIG", cage.getPosition());
    }

    @Override
    public void startup() {
        super.startup();

        flywheel.setDirection(DcMotor.Direction.REVERSE);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        triggerOff(false);
        update_pid_coeffs();
    }

    public void update_pid_coeffs() {
        flywheel.setVelocityPIDFCoefficients(
                ShooterConfig.flywheel_pid_coeffs.p,
                ShooterConfig.flywheel_pid_coeffs.i,
                ShooterConfig.flywheel_pid_coeffs.d,
                0 // no f
        );
    }

    @Override
    public void shutdown() {
        super.shutdown();

        flywheel.setPower(0);
        unshoot();
    }

    public void spin(boolean wait) {
        flywheel.setVelocity(ShooterConfig.target_speed);
        while(wait && isOpmodeActive() && Math.abs(flywheel.getVelocity() - ShooterConfig.target_speed) > ShooterConfig.VELOCITY_ERROR) {
            flywheel.setVelocity(ShooterConfig.target_speed);
        }
    }

    public void stop() {
        flywheel.setVelocity(0);
    }

    public void triggerOn(boolean wait) {
        wait = wait && Math.abs(cage.getPosition() - ShooterConfig.shoot_trigger_pos) > SERVO_POS_ERROR_BOUNDARY;
        cage.setPosition(ShooterConfig.shoot_trigger_pos);
        while(wait && isOpmodeActive() && Math.abs(cage.getPosition() - ShooterConfig.shoot_trigger_pos) > SERVO_POS_ERROR_BOUNDARY) {
            cage.setPosition(ShooterConfig.shoot_trigger_pos);
        }
        if(wait && useHardWait) {
            waitForTime(hardWaitMills/1000.0);
        }
    }

    public void triggerOff(boolean wait) {
        wait = wait && Math.abs(cage.getPosition() - ShooterConfig.unshoot_trigger_pos) > SERVO_POS_ERROR_BOUNDARY;
        cage.setPosition(ShooterConfig.unshoot_trigger_pos);
        while(wait && isOpmodeActive() && Math.abs(cage.getPosition() - ShooterConfig.unshoot_trigger_pos) > SERVO_POS_ERROR_BOUNDARY) {
            cage.setPosition(ShooterConfig.unshoot_trigger_pos);
        }
        if(wait && useHardWait) {
            waitForTime(hardWaitMills/1000.0);
        }
    }

    public void shoot() {
        spin(true);
        System.out.println("Spin started");
        triggerOn(true);
        System.out.println("TRIG on");
        triggerOff(true);
        System.out.println("TRIG off");
    }

    public void unshoot() {
        stop();
        triggerOff(true);
    }
}
