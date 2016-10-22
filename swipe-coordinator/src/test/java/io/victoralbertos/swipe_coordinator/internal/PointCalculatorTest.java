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
import io.victoralbertos.swipe_coordinator.internal.PointCalculator.Point;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public final class PointCalculatorTest {
  private PointCalculator pointCalculatorUT;
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock View parentView;
  @Mock View targetView;
  @Mock MotionEvent event;
  @Mock ProgressCalculator progressCalculator;

  @Before public void before() {
    when(targetView.getX()).thenReturn(0f);
    when(targetView.getY()).thenReturn(0f);

    when(parentView.getWidth()).thenReturn(1000);
    when(parentView.getHeight()).thenReturn(1000);
    when(targetView.getHeight()).thenReturn(100);
    when(targetView.getWidth()).thenReturn(100);
  }

  @Test public void Verify_Point_onActionMove_Left_To_Right() {
    pointCalculatorUT = new PointCalculator(SwipeDirection.LEFT_TO_RIGHT, parentView, targetView,
        progressCalculator);

    when(event.getRawX()).thenReturn(0f);
    when(event.getRawY()).thenReturn(0f);

    pointCalculatorUT.onActionDown(event);

    reset(event);
    when(event.getRawX()).thenReturn(100f);
    when(event.getRawY()).thenReturn(100f);

    Point point = pointCalculatorUT.onActionMove(event);
    assertThat(point.x, is(100.0f));
    assertThat(point.y, is(0f));
  }

  @Test public void Verify_Point_onActionUp_Threshold_Reached_Left_To_Right() {
    pointCalculatorUT = new PointCalculator(SwipeDirection.LEFT_TO_RIGHT, parentView, targetView,
        progressCalculator);

    when(event.getRawX()).thenReturn(500f);
    when(event.getRawY()).thenReturn(500f);

    pointCalculatorUT.onActionDown(event);

    reset(event);
    when(event.getRawX()).thenReturn(300f);
    when(event.getRawY()).thenReturn(300f);

    when(progressCalculator.isThresholdReached()).thenReturn(true);

    Point point = pointCalculatorUT.onActionUp();
    assertThat(point.x, is(900.0f));
    assertThat(point.y, is(0f));
  }


  @Test public void Verify_Point_onActionUp_No_Threshold_Reached_Left_To_Right() {
    pointCalculatorUT = new PointCalculator(SwipeDirection.LEFT_TO_RIGHT, parentView, targetView,
        progressCalculator);

    when(event.getRawX()).thenReturn(500f);
    when(event.getRawY()).thenReturn(500f);

    pointCalculatorUT.onActionDown(event);

    reset(event);
    when(event.getRawX()).thenReturn(300f);
    when(event.getRawY()).thenReturn(300f);

    when(progressCalculator.isThresholdReached()).thenReturn(false);

    Point point = pointCalculatorUT.onActionUp();
    assertThat(point.x, is(0f));
    assertThat(point.y, is(0f));
  }

  @Test public void Verify_Point_onSwipe_Left_To_Right() {
    pointCalculatorUT = new PointCalculator(SwipeDirection.LEFT_TO_RIGHT, parentView, targetView,
        progressCalculator);

    Point point = pointCalculatorUT.onSwipe();
    assertThat(point.x, is(900.0f));
    assertThat(point.y, is(0f));
  }

  @Test public void Verify_Point_onActionMove_Top_To_Bottom() {
    pointCalculatorUT = new PointCalculator(SwipeDirection.TOP_TO_BOTTOM, parentView, targetView,
        progressCalculator);

    when(event.getRawX()).thenReturn(0f);
    when(event.getRawY()).thenReturn(0f);

    pointCalculatorUT.onActionDown(event);

    reset(event);
    when(event.getRawX()).thenReturn(100f);
    when(event.getRawY()).thenReturn(100f);

    Point point = pointCalculatorUT.onActionMove(event);
    assertThat(point.x, is(0f));
    assertThat(point.y, is(100.0f));
  }

  @Test public void Verify_Point_onActionUp_Threshold_Reached_Top_To_Bottom() {
    pointCalculatorUT = new PointCalculator(SwipeDirection.TOP_TO_BOTTOM, parentView, targetView,
        progressCalculator);

    when(event.getRawX()).thenReturn(500f);
    when(event.getRawY()).thenReturn(500f);

    pointCalculatorUT.onActionDown(event);

    reset(event);
    when(event.getRawX()).thenReturn(300f);
    when(event.getRawY()).thenReturn(300f);

    when(progressCalculator.isThresholdReached()).thenReturn(true);

    Point point = pointCalculatorUT.onActionUp();
    assertThat(point.x, is(0f));
    assertThat(point.y, is(900.0f));
  }


  @Test public void Verify_Point_onActionUp_No_Threshold_Reached_Top_To_Bottom() {
    pointCalculatorUT = new PointCalculator(SwipeDirection.TOP_TO_BOTTOM, parentView, targetView,
        progressCalculator);

    when(event.getRawX()).thenReturn(500f);
    when(event.getRawY()).thenReturn(500f);

    pointCalculatorUT.onActionDown(event);

    reset(event);
    when(event.getRawX()).thenReturn(300f);
    when(event.getRawY()).thenReturn(300f);

    when(progressCalculator.isThresholdReached()).thenReturn(false);

    Point point = pointCalculatorUT.onActionUp();
    assertThat(point.x, is(0f));
    assertThat(point.y, is(0f));
  }

  @Test public void Verify_Point_onSwipe_Top_To_Bottom() {
    pointCalculatorUT = new PointCalculator(SwipeDirection.TOP_TO_BOTTOM, parentView, targetView,
        progressCalculator);

    Point point = pointCalculatorUT.onSwipe();
    assertThat(point.x, is(0f));
    assertThat(point.y, is(900.0f));
  }
}
