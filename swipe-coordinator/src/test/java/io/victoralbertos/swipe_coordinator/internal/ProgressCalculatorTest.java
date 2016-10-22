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
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public final class ProgressCalculatorTest {
  private ProgressCalculator progressCalculatorUT;
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock View parentView;
  @Mock View targetView;

  @Test public void Verify_Progress_Left_To_Right() {
    progressCalculatorUT =
        new ProgressCalculator(SwipeDirection.LEFT_TO_RIGHT, parentView, targetView);

    when(targetView.getX()).thenReturn(0f);
    when(targetView.getWidth()).thenReturn(100);
    when(parentView.getWidth()).thenReturn(1000);

    assertThat(progressCalculatorUT.getProgress(), is(0.0f));

    reset(targetView);
    when(targetView.getX()).thenReturn(100f);
    when(targetView.getWidth()).thenReturn(100);

    assertThat(progressCalculatorUT.getProgress(), is(0.11111111f));

    reset(targetView);
    when(targetView.getX()).thenReturn(500f);
    when(targetView.getWidth()).thenReturn(100);

    assertThat(progressCalculatorUT.getProgress(), is(0.5555556f));

    reset(targetView);
    when(targetView.getX()).thenReturn(900f);
    when(targetView.getWidth()).thenReturn(100);

    assertThat(progressCalculatorUT.getProgress(), is(1.0f));
  }

  @Test public void Verify_Progress_Top_To_Bottom() {
    progressCalculatorUT =
        new ProgressCalculator(SwipeDirection.TOP_TO_BOTTOM, parentView, targetView);

    when(targetView.getY()).thenReturn(0f);
    when(targetView.getHeight()).thenReturn(100);
    when(parentView.getHeight()).thenReturn(1000);

    assertThat(progressCalculatorUT.getProgress(), is(0.0f));

    reset(targetView);
    when(targetView.getY()).thenReturn(100f);
    when(targetView.getHeight()).thenReturn(100);

    assertThat(progressCalculatorUT.getProgress(), is(0.11111111f));

    reset(targetView);
    when(targetView.getY()).thenReturn(500f);
    when(targetView.getHeight()).thenReturn(100);

    assertThat(progressCalculatorUT.getProgress(), is(0.5555556f));

    reset(targetView);
    when(targetView.getY()).thenReturn(900f);
    when(targetView.getHeight()).thenReturn(100);

    assertThat(progressCalculatorUT.getProgress(), is(1.0f));
  }

  @Test public void Verify_Is_Reached_Threshold_Left_To_Right() {
    progressCalculatorUT =
        new ProgressCalculator(SwipeDirection.LEFT_TO_RIGHT, parentView, targetView);

    progressCalculatorUT.setThreshold(.5f);

    when(targetView.getX()).thenReturn(0f);
    when(targetView.getWidth()).thenReturn(100);
    when(parentView.getWidth()).thenReturn(1000);

    assertThat(progressCalculatorUT.isThresholdReached(), is(false));

    reset(targetView);
    when(targetView.getX()).thenReturn(449f);
    when(targetView.getWidth()).thenReturn(100);
    assertThat(progressCalculatorUT.isThresholdReached(), is(false));

    reset(targetView);
    when(targetView.getX()).thenReturn(450f);
    when(targetView.getWidth()).thenReturn(100);
    assertThat(progressCalculatorUT.isThresholdReached(), is(true));
  }

  @Test public void Verify_Is_Reached_Threshold_Top_To_Bottom() {
    progressCalculatorUT =
        new ProgressCalculator(SwipeDirection.TOP_TO_BOTTOM, parentView, targetView);

    progressCalculatorUT.setThreshold(.5f);

    when(targetView.getY()).thenReturn(0f);
    when(targetView.getHeight()).thenReturn(100);
    when(parentView.getHeight()).thenReturn(1000);

    assertThat(progressCalculatorUT.isThresholdReached(), is(false));

    reset(targetView);
    when(targetView.getY()).thenReturn(449f);
    when(targetView.getHeight()).thenReturn(100);
    assertThat(progressCalculatorUT.isThresholdReached(), is(false));

    reset(targetView);
    when(targetView.getY()).thenReturn(450f);
    when(targetView.getHeight()).thenReturn(100);
    assertThat(progressCalculatorUT.isThresholdReached(), is(true));
  }
}
