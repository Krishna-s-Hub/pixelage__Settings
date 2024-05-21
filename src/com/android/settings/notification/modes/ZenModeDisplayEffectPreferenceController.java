/*
 * Copyright (C) 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.notification.modes;

import static android.service.notification.ZenPolicy.PRIORITY_CATEGORY_ALARMS;
import static android.service.notification.ZenPolicy.PRIORITY_CATEGORY_EVENTS;
import static android.service.notification.ZenPolicy.PRIORITY_CATEGORY_MEDIA;
import static android.service.notification.ZenPolicy.PRIORITY_CATEGORY_REMINDERS;
import static android.service.notification.ZenPolicy.PRIORITY_CATEGORY_SYSTEM;

import android.app.AutomaticZenRule;
import android.content.Context;
import android.service.notification.ZenDeviceEffects;
import android.service.notification.ZenPolicy;
import androidx.preference.Preference;
import androidx.preference.TwoStatePreference;

public class ZenModeDisplayEffectPreferenceController extends AbstractZenModePreferenceController
        implements Preference.OnPreferenceChangeListener {

    public ZenModeDisplayEffectPreferenceController(Context context, String key,
            ZenModesBackend backend) {
        super(context, key, backend);
    }

    @Override
    public void updateState(Preference preference) {
        TwoStatePreference pref = (TwoStatePreference) preference;
        ZenDeviceEffects effects =  getMode().getRule().getDeviceEffects();
        if (effects == null) {
            pref.setChecked(false);
        } else {
            switch (getPreferenceKey()) {
                case "effect_greyscale":
                    pref.setChecked(effects.shouldDisplayGrayscale());
                    break;
                case "effect_aod":
                    pref.setChecked(effects.shouldSuppressAmbientDisplay());
                    break;
                case "effect_wallpaper":
                    pref.setChecked(effects.shouldDimWallpaper());
                    break;
                case "effect_dark_theme":
                    pref.setChecked(effects.shouldUseNightMode());
                    break;
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final boolean allow = (Boolean) newValue;

        ZenDeviceEffects currEffects =  getMode().getRule().getDeviceEffects();
        ZenDeviceEffects.Builder updatedEffects = currEffects == null
                ? new ZenDeviceEffects.Builder()
                : new ZenDeviceEffects.Builder(getMode().getRule().getDeviceEffects());
        switch (getPreferenceKey()) {
            case "effect_greyscale":
                updatedEffects.setShouldDisplayGrayscale(allow);
                break;
            case "effect_aod":
                updatedEffects.setShouldSuppressAmbientDisplay(allow);
                break;
            case "effect_wallpaper":
                updatedEffects.setShouldDimWallpaper(allow);
                break;
            case "effect_dark_theme":
                updatedEffects.setShouldUseNightMode(allow);
                break;
        }
        AutomaticZenRule updatedAzr = new AutomaticZenRule.Builder(getMode().getRule())
                .setDeviceEffects(updatedEffects.build())
                .build();
        getMode().setAzr(updatedAzr);
        mBackend.updateMode(getMode());

        return true;
    }
}
