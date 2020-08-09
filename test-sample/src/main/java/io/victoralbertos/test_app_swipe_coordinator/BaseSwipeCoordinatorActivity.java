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

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import io.victoralbertos.swipe_coordinator.SwipeCoordinator;
import io.victoralbertos.swipe_coordinator.SwipeDirection;

public abstract class BaseSwipeCoordinatorActivity extends AppCompatActivity {
  private boolean swiped;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(idLayout());

    final TextView tvProgress = (TextView) findViewById(R.id.tv_progress);
    final View tvAccepted = findViewById(R.id.tv_accepted);
    final View tvPending = findViewById(R.id.tv_pending);
    final View decorator = findViewById(R.id.decorator_view);

    ViewGroup parentSwipeableView = (ViewGroup) findViewById(R.id.vg_parent_swipeable_view);
    SwipeCoordinator coordinator =
        new SwipeCoordinator(parentSwipeableView, swipeDirection());

    coordinator.setThreshold(0.5f);
    coordinator.setVariancePercentage(1f);

    coordinator.setOnActionUpSwipeListener(new SwipeCoordinator.ActionUpSwipeListener() {
      @Override public void onActionUp(boolean thresholdReached) {
        if (thresholdReached) {
          swiped = true;
          Toast.makeText(BaseSwipeCoordinatorActivity.this, getString(R.string.approved),
              Toast.LENGTH_SHORT)
              .show();
        } else {
          swiped = false;
          Toast.makeText(BaseSwipeCoordinatorActivity.this, getString(R.string.pending),
              Toast.LENGTH_SHORT)
              .show();
        }
      }
    });

    coordinator.setProgressListener(new SwipeCoordinator.ProgressListener() {
      @Override public void onProgress(float progress) {
        tvAccepted.setScaleX(progress);
        tvAccepted.setScaleY(progress);
        tvAccepted.setAlpha(progress < 0.3f ? 0.3f : progress);

        float progressInvert = 1 - progress;
        tvPending.setScaleX(progressInvert);
        tvPending.setScaleY(progressInvert);
        tvPending.setAlpha(progressInvert < 0.3f ? 0.3f : progressInvert);

        decorator.setAlpha(progress < 0.3f ? 0.3f : progress);

        String formattedProgress = String.valueOf(Math.round(progress * 100) + "%");
        tvProgress.setText(formattedProgress);
      }
    });

    //Handle config changes
    if (getLastCustomNonConfigurationInstance() == null) {
      swiped = false;
    } else {
      swiped = (Boolean) getLastCustomNonConfigurationInstance();
    }

    if (swiped) {
      coordinator.doSwipe();
    }
  }

  @Override public Object onRetainCustomNonConfigurationInstance() {
    return swiped;
  }

  @LayoutRes
  protected abstract int idLayout();

  protected abstract SwipeDirection swipeDirection();
}
