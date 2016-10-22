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
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public final class BoundariesDetectorTest {
  private BoundariesDetector boundariesDetectorUT;
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock View parentView;
  @Mock View targetView;
  @Mock BoundariesDetector.DirectionDetector directionDetector;
  @Mock MotionEvent motionEvent;

  @Test public void Verify_Left_To_Right_Intent_Left() {
    boundariesDetectorUT = new BoundariesDetector(SwipeDirection.LEFT_TO_RIGHT,
        parentView, targetView, directionDetector);
    when(directionDetector.onActionMove(any(MotionEvent.class)))
        .thenReturn(BoundariesDetector.IntentDirection.LEFT);

    when(targetView.getX()).thenReturn(0f);

    assertThat(boundariesDetectorUT.onActionMove(motionEvent), is(false));

    reset(targetView);
    when(targetView.getX()).thenReturn(1f);

    assertThat(boundariesDetectorUT.onActionMove(motionEvent), is(true));
  }

  @Test public void Verify_Left_To_Right_Intent_Right() {
    boundariesDetectorUT = new BoundariesDetector(SwipeDirection.LEFT_TO_RIGHT,
        parentView, targetView, directionDetector);
    when(directionDetector.onActionMove(any(MotionEvent.class)))
        .thenReturn(BoundariesDetector.IntentDirection.RIGHT);

    when(targetView.getX()).thenReturn(50f);
    when(targetView.getWidth()).thenReturn(100);
    when(parentView.getWidth()).thenReturn(1000);

    assertThat(boundariesDetectorUT.onActionMove(motionEvent), is(true));

    reset(targetView);

    when(targetView.getX()).thenReturn(900f);
    when(targetView.getWidth()).thenReturn(100);

    assertThat(boundariesDetectorUT.onActionMove(motionEvent), is(false));
  }

  @Test public void Verify_Top_To_Bottom_Intent_Up() {
    boundariesDetectorUT = new BoundariesDetector(SwipeDirection.TOP_TO_BOTTOM,
        parentView, targetView, directionDetector);
    when(directionDetector.onActionMove(any(MotionEvent.class)))
        .thenReturn(BoundariesDetector.IntentDirection.UP);

    when(targetView.getY()).thenReturn(0f);

    assertThat(boundariesDetectorUT.onActionMove(motionEvent), is(false));

    reset(targetView);
    when(targetView.getY()).thenReturn(1f);

    assertThat(boundariesDetectorUT.onActionMove(motionEvent), is(true));
  }

  @Test public void Verify_Top_To_Bottom_Intent_Down() {
    boundariesDetectorUT = new BoundariesDetector(SwipeDirection.TOP_TO_BOTTOM,
        parentView, targetView, directionDetector);
    when(directionDetector.onActionMove(any(MotionEvent.class)))
        .thenReturn(BoundariesDetector.IntentDirection.DOWN);

    when(targetView.getY()).thenReturn(50f);
    when(targetView.getHeight()).thenReturn(100);
    when(parentView.getHeight()).thenReturn(1000);

    assertThat(boundariesDetectorUT.onActionMove(motionEvent), is(true));

    reset(targetView);

    when(targetView.getY()).thenReturn(900f);
    when(targetView.getHeight()).thenReturn(100);

    assertThat(boundariesDetectorUT.onActionMove(motionEvent), is(false));
  }
}
