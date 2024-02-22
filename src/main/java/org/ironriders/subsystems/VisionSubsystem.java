package org.ironriders.subsystems;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.common.hardware.VisionLEDMode;

import java.util.Optional;

import static org.ironriders.constants.Vision.CAMERA_POSITION;
import static org.ironriders.constants.Vision.VISION_CAMERA;
import static org.photonvision.PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP_ON_COPROCESSOR;

public class VisionSubsystem extends SubsystemBase {
    private final PhotonCamera camera = new PhotonCamera(VISION_CAMERA);
    private final PhotonPoseEstimator estimator;
    private final AprilTagFieldLayout aprilTagLayout = AprilTagFields.k2024Crescendo.loadAprilTagLayoutField();
    private boolean useVisionForEstimation = false;

    public VisionSubsystem() {
        estimator = new PhotonPoseEstimator(aprilTagLayout, MULTI_TAG_PNP_ON_COPROCESSOR, camera, CAMERA_POSITION);

        camera.setLED(VisionLEDMode.kOff);
        camera.setDriverMode(false);
    }

    @Override
    public void periodic() {
//        if (!RobotBase.isSimulation()) {
//            int[] ids = getResult().getTargets().stream().mapToInt(PhotonTrackedTarget::getFiducialId).toArray();
//            Pose3d[] poses3D = new Pose3d[ids.length];
//            Pose2d[] poses2D = new Pose2d[ids.length];
//            for (int i = 0; i < ids.length; i++) {
//                poses3D[i] = aprilTagLayout.getTagPose(ids[i]).orElse(new Pose3d());
//                poses2D[i] = poses3D[i].toPose2d();
//            }
//
//            visibleAprilTags3D.set(poses3D);
//            visibleAprilTags2D.set(poses2D);
//        }
    }

    public AprilTagFieldLayout getAprilTagLayout() {
        return aprilTagLayout;
    }

    public Optional<EstimatedRobotPose> getPoseEstimate() {
        if (useVisionForEstimation) {
            return estimator.update();
        }
        return Optional.empty();
    }

    public Optional<Pose3d> getTag(int id) {
        return getAprilTagLayout().getTagPose(id);
    }

    public void useVisionForPoseEstimation(boolean useVision) {
        useVisionForEstimation = useVision;
    }
}
