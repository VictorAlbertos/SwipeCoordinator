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
import android.support.test.rule.ActivityTestRule;
import io.victoralbertos.device_animation_test_rule.DeviceAnimationTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static io.victoralbertos.test_app_swipe_coordinator.UiAutomatorHelper.rotateDevice;
import static io.victoralbertos.test_app_swipe_coordinator.ViewActions.swipeDownNotReachingThreshold;
import static io.victoralbertos.test_app_swipe_coordinator.ViewActions.swipeDownReachingThreshold;

public final class SwipeCoordinatorTopToBottomTest {
  @ClassRule static public DeviceAnimationTestRule
      deviceAnimationTestRule = new DeviceAnimationTestRule();
  @Rule public ActivityTestRule<TopToBottomActivity> activityRule =
      new ActivityTestRule<>(TopToBottomActivity.class);
  private Context context;

  @Before public void before() {
    context = activityRule.getActivity();
  }

  @Test public void Top_To_Bottom_Not_Signal_Threshold() throws InterruptedException {
    onView(withId(R.id.swipeable_view)).perform(swipeDownNotReachingThreshold(context));
    onView(withId(R.id.tv_progress)).check(matches(withText("0%")));
  }

  @Test public void Top_To_Bottom_Signal_Threshold() throws InterruptedException {
    onView(withId(R.id.swipeable_view)).perform(swipeDownReachingThreshold(context));
    onView(withId(R.id.tv_progress)).check(matches(withText("100%")));
  }

  @Test public void Top_To_Bottom_Survive_Config_Changes() throws InterruptedException {
    onView(withId(R.id.swipeable_view)).perform(swipeDownReachingThreshold(context));
    onView(withId(R.id.tv_progress)).check(matches(withText("100%")));
    rotateDevice();
    onView(withId(R.id.tv_progress)).check(matches(withText("100%")));
  }
}