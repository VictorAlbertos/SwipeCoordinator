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

import io.victoralbertos.swipe_coordinator.internal.AnimDurationCalculator;
import io.victoralbertos.swipe_coordinator.internal.ProgressCalculator;
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

public final class AnimDurationCalculatorTest {
  private AnimDurationCalculator animDurationCalculatorUT;
  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock ProgressCalculator progressCalculator;

  @Before public void before() {
    animDurationCalculatorUT = new AnimDurationCalculator(progressCalculator);
  }

  @Test public void Verify_No_Threshold_Reached() {
    when(progressCalculator.isThresholdReached()).thenReturn(false);
    when(progressCalculator.getProgress()).thenReturn(0.4f);

    assertThat(animDurationCalculatorUT.time(), is(171l));

    reset(progressCalculator);
    when(progressCalculator.isThresholdReached()).thenReturn(false);
    when(progressCalculator.getProgress()).thenReturn(0.6f);

    assertThat(animDurationCalculatorUT.time(), is(257l));
  }

  @Test public void Verify_Threshold_Reached() {
    when(progressCalculator.isThresholdReached()).thenReturn(true);
    when(progressCalculator.getProgress()).thenReturn(0.4f);

    assertThat(animDurationCalculatorUT.time(), is(257l));

    reset(progressCalculator);
    when(progressCalculator.isThresholdReached()).thenReturn(true);
    when(progressCalculator.getProgress()).thenReturn(0.6f);

    assertThat(animDurationCalculatorUT.time(), is(171l));
  }

  @Test public void Verify_Variance() {
    when(progressCalculator.isThresholdReached()).thenReturn(false);
    when(progressCalculator.getProgress()).thenReturn(0.4f);

    animDurationCalculatorUT.setVariancePercentage(1.0f);

    assertThat(animDurationCalculatorUT.time(), is(171l));

    animDurationCalculatorUT.setVariancePercentage(0.5f);

    assertThat(animDurationCalculatorUT.time(), is(85l));

    animDurationCalculatorUT.setVariancePercentage(1.5f);

    assertThat(animDurationCalculatorUT.time(), is(256l));
  }
}
