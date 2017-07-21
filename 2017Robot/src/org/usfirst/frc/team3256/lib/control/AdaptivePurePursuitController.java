package org.usfirst.frc.team3256.lib.control;

import java.util.Optional;
import java.util.Set;

import org.usfirst.frc.team3256.lib.RigidTransform;
import org.usfirst.frc.team3256.lib.Rotation;
import org.usfirst.frc.team3256.lib.Translation;

/**
 * Implements an adaptive pure pursuit controller. See:
 * https://www.ri.cmu.edu/pub_files/pub1/kelly_alonzo_1994_4/kelly_alonzo_1994_4
 * .pdf
 * 
 * Basically, we find a spot on the path we'd like to follow and calculate the
 * wheel speeds necessary to make us land on that spot. The target spot is a
 * specified distance ahead of us, and we look further ahead the greater our
 * tracking error.
 */
public class AdaptivePurePursuitController {
    private static final double kEpsilon = 1E-9;

    double mFixedLookahead;
    Path mPath;
    RigidTransform.Delta mLastCommand;
    double mLastTime;
    double mMaxAccel;
    double mDt;
    boolean mReversed;
    double mPathCompletionTolerance;
    public boolean firstIteration;

    public AdaptivePurePursuitController(double fixed_lookahead, double max_accel, double nominal_dt, Path path,
            boolean reversed, double path_completion_tolerance) {
        mFixedLookahead = fixed_lookahead;
        mMaxAccel = max_accel;
        mPath = path;
        mDt = nominal_dt;
        mLastCommand = null;
        mReversed = reversed;
        mPathCompletionTolerance = path_completion_tolerance;
        firstIteration = true;
    }

    public boolean isDone() {
        double remainingLength = mPath.getRemainingLength();
        return remainingLength <= mPathCompletionTolerance;
    }

    public RigidTransform.Delta update(RigidTransform robot_pose, double now) {
    	firstIteration = false;
        RigidTransform pose = robot_pose;
        if (mReversed) {
            pose = new RigidTransform(robot_pose.getTranslation(),
                    robot_pose.getRotation().rotateBy(Rotation.fromRadians(Math.PI)));
        }

        double distance_from_path = mPath.update(robot_pose.getTranslation());
        if (this.isDone()) {
            return new RigidTransform.Delta(0, 0, 0);
        }

        PathSegment.Sample lookahead_point = mPath.getLookaheadPoint(robot_pose.getTranslation(),
                distance_from_path + mFixedLookahead);
        Optional<Circle> circle = joinPath(pose, lookahead_point.translation);

        double speed = lookahead_point.speed;
        if (mReversed) {
            speed *= -1;
        }
        // Ensure we don't accelerate too fast from the previous command
        double dt = now - mLastTime;
        if (mLastCommand == null) {
            mLastCommand = new RigidTransform.Delta(0, 0, 0);
            dt = mDt;
        }
        double accel = (speed - mLastCommand.dx) / dt;
        if (accel < -mMaxAccel) {
            speed = mLastCommand.dx - mMaxAccel * dt;
        } else if (accel > mMaxAccel) {
            speed = mLastCommand.dx + mMaxAccel * dt;
        }

        // Ensure we slow down in time to stop
        // vf^2 = v^2 + 2*a*d
        // 0 = v^2 + 2*a*d
        double remaining_distance = mPath.getRemainingLength();
        double max_allowed_speed = Math.sqrt(2 * mMaxAccel * remaining_distance);
        if (Math.abs(speed) > max_allowed_speed) {
            speed = max_allowed_speed * Math.signum(speed);
        }
        RigidTransform.Delta rv;
        if (circle.isPresent()) {
            rv = new RigidTransform.Delta(speed, 0,
                    (circle.get().turn_right ? -1 : 1) * Math.abs(speed) / circle.get().radius);
        } else {
            rv = new RigidTransform.Delta(speed, 0, 0);
        }
        mLastTime = now;
        mLastCommand = rv;
        return rv;
    }

    public Set<String> getMarkersCrossed() {
        return mPath.getMarkersCrossed();
    }

    public static class Circle {
        public final Translation center;
        public final double radius;
        public final boolean turn_right;

        public Circle(Translation center, double radius, boolean turn_right) {
            this.center = center;
            this.radius = radius;
            this.turn_right = turn_right;
        }
    }

    public static Optional<Circle> joinPath(RigidTransform robot_pose, Translation lookahead_point) {
        double x1 = robot_pose.getTranslation().getX();
        double y1 = robot_pose.getTranslation().getY();
        double x2 = lookahead_point.getX();
        double y2 = lookahead_point.getY();

        Translation pose_to_lookahead = robot_pose.getTranslation().inverse().translateBy(lookahead_point);
        double cross_product = pose_to_lookahead.getX() * robot_pose.getRotation().sin()
                - pose_to_lookahead.getY() * robot_pose.getRotation().cos();
        if (Math.abs(cross_product) < kEpsilon) {
            return Optional.empty();
        }

        double dx = x1 - x2;
        double dy = y1 - y2;
        double my = (cross_product > 0 ? -1 : 1) * robot_pose.getRotation().cos();
        double mx = (cross_product > 0 ? 1 : -1) * robot_pose.getRotation().sin();

        double cross_term = mx * dx + my * dy;

        if (Math.abs(cross_term) < kEpsilon) {
            return Optional.empty();
        }

        return Optional.of(new Circle(
                new Translation((mx * (x1 * x1 - x2 * x2 - dy * dy) + 2 * my * x1 * dy) / (2 * cross_term),
                        (-my * (-y1 * y1 + y2 * y2 + dx * dx) + 2 * mx * y1 * dx) / (2 * cross_term)),
                .5 * Math.abs((dx * dx + dy * dy) / cross_term), cross_product > 0));
    }

}
