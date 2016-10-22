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

package io.victoralbertos.swipe_coordinator.internal;

import android.view.View;
import io.victoralbertos.swipe_coordinator.SwipeDirection;

public class ProgressCalculator {
  private static final float DEF_THRESHOLD_PROGRESS = 0.7f;
  private final SwipeDirection swipeDirection;
  private final View parentView;
  private final View targetView;
  private float threshold;

  public ProgressCalculator(SwipeDirection swipeDirection, View parentView,
      View targetView) {
    this.swipeDirection = swipeDirection;
    this.parentView = parentView;
    this.targetView = targetView;
    this.threshold = DEF_THRESHOLD_PROGRESS;
  }

  public void setThreshold(float threshold) {
    this.threshold = threshold;
  }

  public float getProgress() {
    float output;

    switch (swipeDirection) {
      case LEFT_TO_RIGHT:
        output = targetView.getX() / (parentView.getWidth() - targetView.getWidth());
        output = output > 1.0f ? 1.0f : output;
        output = output < 0.0f ? 0.0f : output;
        return output;
      case TOP_TO_BOTTOM:
        output = targetView.getY() / (parentView.getHeight() - targetView.getHeight());
        output = output > 1.0f ? 1.0f : output;
        output = output < 0.0f ? 0.0f : output;
        return output;
    }

    throw new IllegalStateException();
  }

  public boolean isThresholdReached() {
    return getProgress() >= threshold;
  }
}
