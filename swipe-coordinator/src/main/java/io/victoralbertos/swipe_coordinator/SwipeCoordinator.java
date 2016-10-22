/*
 * Copyright 2016 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.victoralbertos.swipe_coordinator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import io.victoralbertos.swipe_coordinator.internal.AnimDurationCalculator;
import io.victoralbertos.swipe_coordinator.internal.AnimatorEndListener;
import io.victoralbertos.swipe_coordinator.internal.BoundariesDetector;
import io.victoralbertos.swipe_coordinator.internal.PointCalculator;
import io.victoralbertos.swipe_coordinator.internal.PointCalculator.Point;
import io.victoralbertos.swipe_coordinator.internal.ProgressCalculator;

/**
 * Given a parent and a child, SwipeCoordinator detects the touch events on the child constraining
 * its translation to the parent boundaries based on the direction supplied by the client.
 */
public class SwipeCoordinator implements View.OnTouchListener, View.OnLayoutChangeListener {
  private final BoundariesDetector boundariesDetector;
  private final PointCalculator pointCalculator;
  private final ProgressCalculator progressCalculator;
  private final AnimDurationCalculator animDurationCalculator;
  private ProgressListener progressListener;
  private ActionUpSwipeListener actionUpSwipeListener;
  private ViewGroup parentViewGroup;

  /**
   * @param parentViewGroup must contain a child view annotated with the id swipeable_view
   * @param swipeDirection the expected direction for the gesture recognition
   */
  public SwipeCoordinator(ViewGroup parentViewGroup,
      SwipeDirection swipeDirection) {
    View swipeableView = parentViewGroup.findViewById(R.id.swipeable_view);

    if (swipeableView == null) {
      throw new IllegalStateException(
          "the parent ViewGroup must has a child annotated with the id swipeable_view");
    }

    this.parentViewGroup = parentViewGroup;

    swipeableView.setOnTouchListener(this);
    swipeableView.addOnLayoutChangeListener(this);

    boundariesDetector =
        new BoundariesDetector(swipeDirection, parentViewGroup, swipeableView, new BoundariesDetector.DirectionDetector(swipeDirection));
    progressCalculator =
        new ProgressCalculator(swipeDirection, parentViewGroup, swipeableView);
    pointCalculator =
        new PointCalculator(swipeDirection, parentViewGroup, swipeableView, progressCalculator);
    animDurationCalculator = new AnimDurationCalculator(progressCalculator);
  }

  /**
   * Set the {@link ProgressListener)
   */
  public void setProgressListener(ProgressListener progressListener) {
    this.progressListener = progressListener;
  }

  /**
   * Set the {@link ActionUpSwipeListener )
   */
  public void setOnActionUpSwipeListener(ActionUpSwipeListener actionUpSwipeListener) {
    this.actionUpSwipeListener = actionUpSwipeListener;
  }

  /**
   * Set the value in percentage of the threshold to signal it as reached when the user drops the
   * swipeable view. Default value is 0.7
   * @param threshold from 0.0 to 1.0
   */
  public void setThreshold(float threshold) {
    progressCalculator.setThreshold(threshold);
  }

  /**
   * Increment (if number greater than 1.0) or decrement (if number less than 1.0) the duration of
   * the rearrange animation performed when the user drops the swipeable view. Default value is 1.0
   */
  public void setVariancePercentage(float variancePercentage) {
    animDurationCalculator.setVariancePercentage(variancePercentage);
  }

  @Override public boolean onTouch(final View view, MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        boundariesDetector.onActionDown(event);
        pointCalculator.onActionDown(event);
        break;
      case MotionEvent.ACTION_MOVE:
        if (!boundariesDetector.onActionMove(event)) {
          break;
        }

        Point nextPoint = pointCalculator.onActionMove(event);
        view.setX(nextPoint.x);
        view.setY(nextPoint.y);

        view.requestLayout();
        break;
      case MotionEvent.ACTION_UP:
        Point endPoint = pointCalculator.onActionUp();

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view,
            PropertyValuesHolder.ofFloat("translationX", endPoint.x),
            PropertyValuesHolder.ofFloat("translationY", endPoint.y))
            .setDuration(animDurationCalculator.time());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
          @Override public void onAnimationUpdate(ValueAnimator valueAnimator) {
            view.requestLayout();
          }
        });

        animator.addListener(new AnimatorEndListener() {
          @Override public void onAnimationEnd(Animator animator) {
            if (actionUpSwipeListener != null) {
              actionUpSwipeListener.onActionUp(progressCalculator.isThresholdReached());
            }
          }
        });

        animator.start();
        break;
      default:
        return false;
    }
    return true;
  }

  @Override
  public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
      int oldTop, int oldRight, int oldBottom) {
    if (progressListener != null) {
      progressListener.onProgress(progressCalculator.getProgress());
    }
  }

  /**
   * This method is meant to be used for config changes once the user has swiped the view. It
   * translates the swipeable view to the final stage and calls ProgressListener::onProgress() with
   * a value of 1.0 to restore all the dependent actions.
   */
  public void doSwipe() {
    parentViewGroup.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            parentViewGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            View swipeableView = parentViewGroup.findViewById(R.id.swipeable_view);

            Point nextPoint = pointCalculator.onSwipe();

            swipeableView.setX(nextPoint.x);
            swipeableView.setY(nextPoint.y);

            swipeableView.requestLayout();
          }
        });
  }

  /**
   * Listen for the progress of the swipe gesture. This callback is the right place to create custom
   * animations (alpha, scale, ...) based on the progress value.
   */
  public interface ProgressListener {
    /**
     * @param progress from 0.0 to 1.0
     */
    void onProgress(float progress);
  }

  /**
   * Listen to action up events.
   */
  public interface ActionUpSwipeListener {
    /**
     * Signal the action up motion.
     *
     * @param thresholdReached true when the threshold is reached, false otherwise.
     */
    void onActionUp(boolean thresholdReached);
  }
}
