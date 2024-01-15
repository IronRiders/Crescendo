package org.ironriders.subsystems;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.common.hardware.VisionLEDMode;
import org.photonvision.targeting.PhotonPipelineResult;

import java.util.Optional;

import static org.ironriders.constants.Vision.CAMERA;
import static org.ironriders.constants.Vision.LIMELIGHT_POSITION;
import static org.photonvision.PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR;

public class VisionSubsystem extends SubsystemBase {
    private final PhotonCamera camera = new PhotonCamera(CAMERA);
    private final PhotonPoseEstimator estimator;
    private final AprilTagFieldLayout tagLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();
    private boolean useVisionForEstimation = false;

    public VisionSubsystem() {
        estimator = new PhotonPoseEstimator(tagLayout, MULTI_TAG_PNP_ON_COPROCESSOR, camera, LIMELIGHT_POSITION);

        camera.setLED(VisionLEDMode.kOff);
        camera.setDriverMode(false);
    }

    public AprilTagFieldLayout getTagLayout() {
        return tagLayout;
    }

    public Optional<EstimatedRobotPose> getPoseEstimate() {
        if (useVisionForEstimation) {
            return estimator.update();
        }
        return Optional.empty();
    }

    public Optional<Pose3d> getTag(int id) {
        return getTagLayout().getTagPose(id);
    }

    public void useVisionForPoseEstimation(boolean useVision) {
        useVisionForEstimation = useVision;
    }

    public PhotonPipelineResult getResult() {
        return camera.getLatestResult();
    }
}
