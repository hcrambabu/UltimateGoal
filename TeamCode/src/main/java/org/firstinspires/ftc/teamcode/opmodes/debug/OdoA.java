package org.firstinspires.ftc.teamcode.opmodes.debug;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.robots.LiveRobot;

import static org.firstinspires.ftc.teamcode.constants.AutonomousConst.RED;

@Autonomous(name="Odo A", group="autonomous")
//@Disabled
public class OdoA extends LinearOpMode {

    LiveRobot robot;

    int pattern;

    @Override
    public void runOpMode() throws InterruptedException {
        robot = new LiveRobot(this);
        robot.startup();

        int color = RED;

        robot.drive_train.color = color;


        waitForStart();

        robot.drive_train.setInitPos(0, 0, 0);
        robot.drive_train.odo_move(0, 0, -Math.PI/2, 1, -1, Math.PI/180.0);

        robot.shutdown();
    }
}
