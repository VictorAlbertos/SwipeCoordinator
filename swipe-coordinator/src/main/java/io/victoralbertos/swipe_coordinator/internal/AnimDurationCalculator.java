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

public final class AnimDurationCalculator {
  private final ProgressCalculator progressCalculator;
  private static final float AT_70_PROGRESS = 0.7f;
  private static final long ANIM_REARRANGE_DUR_AT_70_PROGRESS = 300;
  private float variancePercentage;

  public AnimDurationCalculator(ProgressCalculator progressCalculator) {
    this.progressCalculator = progressCalculator;
    this.variancePercentage = 1.0f;
  }

  public void setVariancePercentage(float variancePercentage) {
    this.variancePercentage = variancePercentage;
  }

  public long time() {
    float progress = progressCalculator.isThresholdReached() ?
        1.0f - progressCalculator.getProgress()
        : progressCalculator.getProgress();
    return (long) ((long) (((progress * ANIM_REARRANGE_DUR_AT_70_PROGRESS)
            / AT_70_PROGRESS)) * variancePercentage);
  }
}
