package org.firstinspires.ftc.teamcode.opmodes.teleop;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.opmodes.LiveTeleopBase;

@Autonomous(name="Teleop Auto", group="autonomous")
//@Disabled
public class LiveAuto extends LiveTeleopBase {

    private static final double angle_acc = Math.PI/60.0;
    private static final double distance_acc = 1.0;

    @Override
    public void on_init() {
        robot.phone_camera.start_streaming();
    }

    @Override
    public void on_start() {
        robot.wobble.closeWobbleLift(true);
        robot.wobble.openWobbleServo(true);
        robot.wobble.closeWobbleServo(true);
        robot.wobble.middleWobbleLift(true);

        int pattren = robot.phone_camera.get_pattern();
        robot.phone_camera.stop_streaming();

        robot.shooter.spin(true);
        waitForTime(0.5);
        if(!isOpmodeActive()) return;
        robot.drive_train.odo_move(robot.drive_train.lcs.x, robot.drive_train.lcs.y, -(Math.PI/180)*4, 1, distance_acc, angle_acc);

        robot.shooter.shoot();
        robot.shooter.shoot();
        robot.shooter.shoot();
        robot.shooter.unshoot();

        catchNShhotInitialRings(pattren);
        gotoRectBasedOnPattren(pattren);
        robot.wobble.dropWobble(true);
        gotoLine();
    }

    private void gotoLine() {
        robot.drive_train.odo_move(-12.0, 12.0, 0, 1, distance_acc, angle_acc);
    }

    private void gotoRectBasedOnPattren(int pattren) {
        //robot.drive_train.odo_move(12.0, -36.0, 0.0, 1, -1, -1);
        //robot.drive_train.odo_move(12.0, -12.0, 0.0, 1, -1, -1);
        switch (pattren) {
            case 1:
                robot.drive_train.odo_move(-12.0, 0.0, -Math.PI/2, 1, distance_acc, angle_acc);
                break;
            case 2:
                robot.drive_train.odo_move(-24.0, 12.0, 0.0, 1, distance_acc, angle_acc);
                break;
            case 3:
                robot.drive_train.odo_move(-24.0, 36.0, -Math.PI/4, 1, distance_acc, angle_acc);
                break;
        }
    }

    private void catchNShhotInitialRings(int patteren) {
        if(patteren == 1) return;
        robot.drive_train.odo_move(robot.drive_train.lcs.x - 5.5, robot.drive_train.lcs.y, 0.0, 1, -1, angle_acc);
        robot.intake.setIntakePower(-1.0);
        robot.drive_train.odo_move(-12.0, -12.0, 0.0, 1, -1, angle_acc);
        if(patteren == 2) {
            waitForTime(2.0);
            robot.intake.setIntakePower(0.0);
            robot.shooter.shoot();
        } else {
            waitForTime(4.0);
            robot.intake.setIntakePower(0.0);
            robot.shooter.shoot();
            robot.shooter.shoot();
            robot.shooter.shoot();
        }
        robot.shooter.unshoot();
    }

    public void waitForTime(double sec) {
        double expTime = getRuntime() + sec;
        try {
            while (isOpmodeActive() && getRuntime() < expTime) {
                //idle();
                Thread.sleep(50);
            }
        }catch (Exception ex){}
    }

    @Override
    public void on_stop() {

    }

    @Override
    public void on_loop() {

    }

    public boolean isOpmodeActive() {
        return opModeIsActive() && !isStopRequested();
    }
}
