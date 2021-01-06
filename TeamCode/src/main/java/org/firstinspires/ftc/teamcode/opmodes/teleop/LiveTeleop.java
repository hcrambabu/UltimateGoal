package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.opmodes.LiveTeleopBase;

@TeleOp(name="Teleop Live", group="driver control")
//@Disabled
public class LiveTeleop extends LiveTeleopBase {

    @Override
    public void on_init() {
    }

    @Override
    public void on_start() {
        this.getRuntime();
        robot.drive_train.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    @Override
    public void on_loop() {

        /// GAMEPAD 2 CONTROLS ///

        double intakePower = gamepad2.left_trigger-gamepad2.right_trigger;
        robot.intake.setIntakePower(intakePower);

        if(gamepad2.right_bumper) {
            robot.shooter.triggerOn(false);
        } else {
            robot.shooter.triggerOff(false);
        }

//        if(gamepad2.b) {
//            robot.shooter.dropCageDown(false);
//        } else if(gamepad2.a){
//            robot.shooter.liftCageUp(false);
//        }

        if(gamepad2.left_bumper){
            robot.wobble.closeWobbleServo(false);
        } else if (gamepad2.y){
            robot.wobble.openWobbleServo(false);
        }

        if(gamepad2.dpad_up) {
            robot.wobble.upWobbleLift(false);
        } else if(gamepad2.dpad_down) {
            robot.wobble.closeWobbleLift(false);
        } else if(gamepad2.dpad_right){
            robot.wobble.middleWobbleLift(false);
        }

        /// GAMEPAD 1 CONTROLS ///

        double speed_mod = 1;
//        if(gamepad1.left_bumper) {
//            speed_mod = 0.25;
//        } else if(gamepad1.right_bumper) {
//            speed_mod = 0.5;
//        }
        robot.drive_train.mechanum_drive(gamepad1.left_stick_x * speed_mod, gamepad1.left_stick_y * speed_mod, gamepad1.right_stick_x * speed_mod);

        if(gamepad1.left_trigger > 0) {
            robot.shooter.spin(false);
        } else {
            robot.shooter.stop();
        }
    }

    @Override
    public void on_stop() {

    }
}
