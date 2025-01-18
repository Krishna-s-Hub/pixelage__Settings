/*
 * Copyright (C) 2019-2024 The Evolution X Project
 * Copyright (C) 2024-2025 Project Pixelage
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;

import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class PixelageBuildTypePreferenceController extends BasePreferenceController {

    private static final String TAG = "PixelageBuildTypeCtrl";
    private static final String PROPERTY_BUILDTYPE = "ro.pixelage.releasetype";

    public PixelageBuildTypePreferenceController(Context context, String key) {
        super(context, key);
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public CharSequence getSummary() {
        String mBuildType = SystemProperties.get(PROPERTY_BUILDTYPE,
                this.mContext.getString(R.string.device_info_default));
        return mBuildType;
    }
}
