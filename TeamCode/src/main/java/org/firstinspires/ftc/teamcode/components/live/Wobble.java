package org.firstinspires.ftc.teamcode.components.live;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;

class WobbleConfig {

    public static final double HOLD_WOBBLE_POS = 1.0;
    public static final double OPEN_WOBBLE_POS = 0.0;

    public static final double LIFT_DOWN_POS = 0.35f;
    public static final double LIFT_MIDDLE_POS = 0.45f;
    public static final double LIFT_UP_POS = 0.9f;
}

public class Wobble extends Component {

    //// SERVOS ////
    private Servo lift;
    private Servo wobbleservo;
    private boolean useHardWait = true;
    private long hardWaitMills = 800;

    {
        name = "Wobble";
    }

    public Wobble(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        //// SERVOS ////
        lift        = hwmap.get(Servo.class, "lift");
        wobbleservo    = hwmap.get(Servo.class, "wobbleservo");
    }

    public void closeWobbleServo(boolean wait) {
        wait = Math.abs(wobbleservo.getPosition() - WobbleConfig.HOLD_WOBBLE_POS) > SERVO_POS_ERROR_BOUNDARY;
        wobbleservo.setPosition(WobbleConfig.HOLD_WOBBLE_POS);
        while(wait && isOpmodeActive() && Math.abs(wobbleservo.getPosition() - WobbleConfig.HOLD_WOBBLE_POS) > SERVO_POS_ERROR_BOUNDARY) {
            wobbleservo.setPosition(WobbleConfig.HOLD_WOBBLE_POS);
        }
        if(wait && isOpmodeActive() && useHardWait) {
            waitForTime(hardWaitMills/1000.0);
        }
    }

    public void openWobbleServo(boolean wait) {
        wait = Math.abs(wobbleservo.getPosition() - WobbleConfig.OPEN_WOBBLE_POS) > SERVO_POS_ERROR_BOUNDARY;
        wobbleservo.setPosition(WobbleConfig.OPEN_WOBBLE_POS);
        while(wait && isOpmodeActive() && Math.abs(wobbleservo.getPosition() - WobbleConfig.OPEN_WOBBLE_POS) > SERVO_POS_ERROR_BOUNDARY) {
            wobbleservo.setPosition(WobbleConfig.OPEN_WOBBLE_POS);
        }
        if(wait && useHardWait) {
            waitForTime(hardWaitMills/1000.0);
        }
    }

    public void closeWobbleLift(boolean wait) {
        wait = Math.abs(lift.getPosition() - WobbleConfig.LIFT_DOWN_POS) > SERVO_POS_ERROR_BOUNDARY;
        lift.setPosition(WobbleConfig.LIFT_DOWN_POS);
        while(wait && isOpmodeActive() && Math.abs(lift.getPosition() - WobbleConfig.LIFT_DOWN_POS) > SERVO_POS_ERROR_BOUNDARY) {
            lift.setPosition(WobbleConfig.LIFT_DOWN_POS);
        }
        if(wait && useHardWait) {
            waitForTime(hardWaitMills/1000.0);
        }
    }

    public void middleWobbleLift(boolean wait) {
        wait = Math.abs(lift.getPosition() - WobbleConfig.LIFT_MIDDLE_POS) > SERVO_POS_ERROR_BOUNDARY;
        lift.setPosition(WobbleConfig.LIFT_MIDDLE_POS);
        while(wait && isOpmodeActive() && Math.abs(lift.getPosition() - WobbleConfig.LIFT_MIDDLE_POS) > SERVO_POS_ERROR_BOUNDARY) {
            lift.setPosition(WobbleConfig.LIFT_MIDDLE_POS);
        }
        if(wait && useHardWait) {
            waitForTime(hardWaitMills/1000.0);
        }
    }

    public void upWobbleLift(boolean wait) {
        wait = Math.abs(lift.getPosition() - WobbleConfig.LIFT_UP_POS) > SERVO_POS_ERROR_BOUNDARY;
        lift.setPosition(WobbleConfig.LIFT_UP_POS);
        while(wait && isOpmodeActive() && Math.abs(lift.getPosition() - WobbleConfig.LIFT_UP_POS) > SERVO_POS_ERROR_BOUNDARY) {
            lift.setPosition(WobbleConfig.LIFT_UP_POS);
        }
        if(wait && useHardWait) {
            waitForTime(hardWaitMills/1000.0);
        }
    }

    public void catchWoble(boolean wait) {
        closeWobbleServo(wait);
        upWobbleLift(wait);
    }

    public void dropWobble(boolean wait) {
        middleWobbleLift(wait);
        openWobbleServo(wait);
        upWobbleLift(wait);
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);
    }

    @Override
    public void startup() {
        super.startup();

        wobbleservo.setDirection(Servo.Direction.REVERSE);
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);
        telemetry.addData("WOBBLE LIFT", lift.getPosition());
        telemetry.addData("WOBBLE HOLD", wobbleservo.getPosition());
    }
}
