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

package io.victoralbertos.test_app_swipe_coordinator;

import android.content.Context;
import android.graphics.Point;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

final class ViewActions {

  static ViewAction swipeRightNotReachingThreshold(Context context) {
    final float x = getWidthScreen(context) * 0.3f;
    return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.TOP_LEFT, new CoordinatesProvider() {
      @Override public float[] calculateCoordinates(View view) {
        return new float[] {x, 0f};
      }
    }, Press.FINGER);
  }

  static ViewAction swipeRightReachingThreshold(Context context) {
    final float x = getWidthScreen(context) * 0.8f;
    return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.TOP_LEFT, new CoordinatesProvider() {
      @Override public float[] calculateCoordinates(View view) {
        return new float[] {x, 0f};
      }
    }, Press.FINGER);
  }

  static ViewAction swipeDownNotReachingThreshold(Context context) {
    final float y = getHeightScreen(context) * 0.3f;
    return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.TOP_LEFT, new CoordinatesProvider() {
      @Override public float[] calculateCoordinates(View view) {
        return new float[] {0f, y};
      }
    }, Press.FINGER);
  }

  static ViewAction swipeDownReachingThreshold(Context context) {
    final float y = getHeightScreen(context) * 0.8f;
    return new GeneralSwipeAction(Swipe.SLOW, GeneralLocation.TOP_LEFT, new CoordinatesProvider() {
      @Override public float[] calculateCoordinates(View view) {
        return new float[] {0f, y};
      }
    }, Press.FINGER);
  }

  private static float getWidthScreen(Context context) {
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = windowManager.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.x;
  }

  private static float getHeightScreen(Context context) {
    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = windowManager.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    return size.y;
  }
}
