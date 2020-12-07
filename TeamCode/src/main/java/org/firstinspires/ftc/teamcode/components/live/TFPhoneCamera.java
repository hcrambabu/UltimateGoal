package org.firstinspires.ftc.teamcode.components.live;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.teamcode.components.Component;
import org.firstinspires.ftc.teamcode.robots.Robot;

import java.util.List;

class TFPhoneCameraConfig {
    public static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    public static final String LABEL_ZERO_ELEMENT = "None";
    public static final String LABEL_FIRST_ELEMENT = "Quad";
    public static final String LABEL_SECOND_ELEMENT = "Single";

    public static final String VUFORIA_KEY =
            "AXx8B3P/////AAABmUvCh01qr0NYuiZsl4XR5wxHpJiI+AQBbtiTScffb3UpHwbjqT0gTnTtblgQyH6abrP5hIOA/Y4wgs8bU+1LwD/bor01NOM30m6KKqBS0hrGh0Z8IZu+1sNQyNzgm5dZNUKFI7UzEGUTlEL0L8r1v2++74NQkE8ZZFs6WyUjEowkDBpYQQE0ANXA5qDl0g2Rd7S3Y4rk9HgRJrJaZ0ojGT0uNzHdjkO7gpPYFsEDfAPVz7Pguzw7psyDlPvRmnKajnomWiCVEortJir77e1fgPSCnLobhrXL8b9PN3vaLu8ow0GxMbmJJN1ni0m+vzguiRaNy4JhDDgevKJuN4bv5CLqIt1EDMDG9ROrxcq3OJMR";
}

public class TFPhoneCamera extends Component {

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private boolean running = true;
    private String currentLable = TFPhoneCameraConfig.LABEL_ZERO_ELEMENT;

    {
        name = "Phone Camera (TF)";
    }

    public TFPhoneCamera(Robot robot) {
        super(robot);
    }

    @Override
    public void registerHardware(HardwareMap hwmap) {
        super.registerHardware(hwmap);

        initVuforia();
        initTfod(hwmap);
    }

    @Override
    public void update(OpMode opmode) {
        super.update(opmode);

        if (running) {
            if (tfod != null) {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    for (Recognition recognition : updatedRecognitions) {
                        currentLable = recognition.getLabel();
                        stopRunning();
                    }
                }
            }
        }
    }

    @Override
    public void startup() {
        super.startup();

        if (tfod != null) {
            tfod.activate();
        }
        running = true;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        stopRunning();
    }

    @Override
    public void updateTelemetry(Telemetry telemetry) {
        super.updateTelemetry(telemetry);

        telemetry.addData("OBJ LABEL", currentLable);
        telemetry.addData("TF RUNNING", running);
    }

    public void stopRunning() {
        running = false;
    }

    public String getCurrentLable() {
        return currentLable;
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = TFPhoneCameraConfig.VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod(HardwareMap hardwareMap) {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFPhoneCameraConfig.TFOD_MODEL_ASSET, TFPhoneCameraConfig.LABEL_FIRST_ELEMENT, TFPhoneCameraConfig.LABEL_SECOND_ELEMENT);
    }
}
