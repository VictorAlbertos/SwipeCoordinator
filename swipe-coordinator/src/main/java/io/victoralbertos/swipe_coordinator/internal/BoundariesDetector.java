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

public final class BoundariesDetector {
  private final SwipeDirection swipeDirection;
  private final View parentView;
  private final View targetView;
  private final DirectionDetector directionDetector;

  public BoundariesDetector(SwipeDirection swipeDirection, View parentView,
      View targetView, DirectionDetector directionDetector) {
    this.swipeDirection = swipeDirection;
    this.parentView = parentView;
    this.targetView = targetView;
    this.directionDetector = directionDetector;
  }

  public void onActionDown(MotionEvent event) {
    directionDetector.onActionDown(event);
  }

  public boolean onActionMove(MotionEvent event) {
    if (swipeDirection == SwipeDirection.LEFT_TO_RIGHT) {
      if (directionDetector.onActionMove(event) == IntentDirection.LEFT) {
        return targetView.getX() > 0;
      } else {
        return targetView.getX() + targetView.getWidth() < parentView.getWidth();
      }
    } else {
      if (directionDetector.onActionMove(event) == IntentDirection.UP) {
        return targetView.getY() > 0;
      } else {
        return targetView.getY() + targetView.getHeight() < parentView.getHeight();
      }
    }
  }

  public static class DirectionDetector {
    private final SwipeDirection swipeDirection;
    private float x1, x2, y1, y2, dx, dy;

    public DirectionDetector(SwipeDirection swipeDirection) {
      this.swipeDirection = swipeDirection;
    }

    void onActionDown(MotionEvent event) {
      x1 = event.getX();
      y1 = event.getY();
    }

    IntentDirection onActionMove(MotionEvent event) {
      x2 = event.getX();
      y2 = event.getY();

      dx = x2 - x1;
      dy = y2 - y1;

      if (swipeDirection == SwipeDirection.LEFT_TO_RIGHT) {
        if (dx > 0) {
          return IntentDirection.RIGHT;
        } else {
          return IntentDirection.LEFT;
        }
      } else {
        if (dy > 0) {
          return IntentDirection.DOWN;
        } else {
          return IntentDirection.UP;
        }
      }
    }
  }

  enum IntentDirection {
    RIGHT, LEFT, UP, DOWN;
  }
}
