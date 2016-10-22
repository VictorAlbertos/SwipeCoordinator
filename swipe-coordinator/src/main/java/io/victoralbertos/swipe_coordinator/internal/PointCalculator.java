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

import android.view.MotionEvent;
import android.view.View;
import io.victoralbertos.swipe_coordinator.SwipeDirection;

public final class PointCalculator {
  private final SwipeDirection swipeDirection;
  private final View target;
  private final View parentView;
  private final ProgressCalculator progressCalculator;
  private float dX, dY;

  public PointCalculator(SwipeDirection swipeDirection, View parentView, View target,
      ProgressCalculator progressCalculator) {
    this.swipeDirection = swipeDirection;
    this.parentView = parentView;
    this.target = target;
    this.progressCalculator = progressCalculator;
  }

  public void onActionDown(MotionEvent event) {
    dX = target.getX() - event.getRawX();
    dY = target.getY() - event.getRawY();
  }

  public Point onActionMove(MotionEvent event) {
    if (swipeDirection == SwipeDirection.LEFT_TO_RIGHT) {
      return new Point(event.getRawX() + dX, target.getY());
    } else {
      return new Point(target.getX(), event.getRawY() + dY);
    }
  }

  public Point onActionUp() {
    if (swipeDirection == SwipeDirection.LEFT_TO_RIGHT) {
      if (progressCalculator.isThresholdReached()) {
        return new Point(parentView.getWidth() - target.getWidth(), target.getY());
      } else {
        return new Point(0, target.getY());
      }
    } else {
      if (progressCalculator.isThresholdReached()) {
        return new Point(target.getX(), parentView.getHeight() - target.getHeight());
      } else {
        return new Point(target.getX(), 0);
      }
    }
  }

  public Point onSwipe() {
    if (swipeDirection == SwipeDirection.LEFT_TO_RIGHT) {
      return new Point(parentView.getWidth() - target.getWidth(), target.getY());
    } else {
      return new Point(target.getX(), parentView.getHeight() - target.getHeight());
    }
  }

  public static class Point {
    public final float x, y;

    public Point(float x, float y) {
      this.x = x;
      this.y = y;
    }
  }
}
