package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="IntakeProtov1")
public class IntakeProto extends LinearOpMode{
    private ElapsedTime elapsedtimer = new ElapsedTime();

    private DcMotor flywheel;
    private DcMotor intake;
    private Servo lift;

    private DcMotor RFDrive;
    private DcMotor RBDrive;
    private DcMotor LFDrive;
    private DcMotor LBDrive;

    private Servo cage;
    private Servo cagelift;
    private Servo wobbleservo;


    @Override
    public void runOpMode() throws InterruptedException {

        flywheel = hardwareMap.get(DcMotor.class,"flywheel");
        intake = hardwareMap.get(DcMotor.class, "intake");
        lift = hardwareMap.get(Servo.class, "lift");

        RFDrive = hardwareMap.get(DcMotor.class, "rf");
        RBDrive = hardwareMap.get(DcMotor.class, "rb");
        LFDrive = hardwareMap.get(DcMotor.class, "lf");
        LBDrive = hardwareMap.get(DcMotor.class, "lb");

        cage = hardwareMap.get(Servo.class, "cage");
        cagelift = hardwareMap.get(Servo.class, "cagelift");
        wobbleservo = hardwareMap.get(Servo.class, "wobbleservo");

        flywheel.setDirection(DcMotor.Direction.FORWARD);

        LFDrive.setDirection(DcMotor.Direction.REVERSE);
        LBDrive.setDirection(DcMotor.Direction.REVERSE);

        wobbleservo.setDirection(Servo.Direction.REVERSE);
        lift.setDirection(Servo.Direction.REVERSE);

        RFDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LFDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RBDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LBDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        waitForStart();


        while(opModeIsActive()){

            double flywheelpower = 0;
            if(gamepad1.right_bumper){
                flywheelpower = (gamepad1.right_trigger-gamepad1.left_trigger)*0.77;
            } else {
                flywheelpower = (gamepad1.right_trigger-gamepad1.left_trigger)*0.72;
            }

            double intakePower = gamepad2.left_trigger-gamepad2.right_trigger;


            float move = gamepad1.left_stick_y *-1;
            float mecanum = gamepad1.left_stick_x;
            float turn = gamepad1.right_stick_x;

            double lfPower = move + mecanum + turn;
            double lbPower = move - mecanum + turn;
            double rfPower = move - mecanum - turn;
            double rbPower = move + mecanum - turn;



            if(gamepad2.right_bumper) {
                cage.setPosition(0.0f);
            } else {
                cage.setPosition(0.35f);
            }

            if(gamepad2.b){
                cagelift.setPosition(1.0f);
            } else if(gamepad2.a){
                cagelift.setPosition(0.7f);
            }

            if(gamepad2.left_bumper){
                wobbleservo.setPosition(0.13f);
            } else if (gamepad2.y){
                wobbleservo.setPosition(1.0f);
            }

            if(gamepad2.dpad_up) {
                lift.setPosition(0.75f);
            } else if(gamepad2.dpad_down) {
                lift.setPosition(0.0f);
            } else if(gamepad2.dpad_right){
                lift.setPosition(0.35);
            }



            flywheel.setPower(flywheelpower);
            intake.setPower(intakePower);


            LFDrive.setPower(lfPower);
            LBDrive.setPower(lbPower);
            RFDrive.setPower(rfPower);
            RBDrive.setPower(rbPower);

            telemetry.addData("Wobble Grab Power: ", gamepad2.left_bumper);
            telemetry.addData("Flywheel Speed: ", flywheelpower);
            telemetry.update();

            elapsedtimer.reset();

            /**          if(gamepad1.y){
             while(elapsedtimer.seconds() < 0.06){
             setWheeldPower(0.4f, 0.4f, -0.4f, -0.4f);

             }
             setWheeldPower(0.0f,0.0f,0.0f,0.0f);

             }
             */


        }


    }
    private void setWheeldPower(float lf, float lb, float rf, float rb) {
        RFDrive.setPower(rf);
        RBDrive.setPower(rb);
        LFDrive.setPower(lf);
        LBDrive.setPower(lb);
    }

}