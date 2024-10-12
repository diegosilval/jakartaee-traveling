/*
 * Copyright 2024 Diego Silva <diego.silva at apuntesdejava.com>.
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
package com.apuntesdejava.ai.traveling.api.request;

import com.apuntesdejava.ai.traveling.model.LatLng;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
public record SearchNearbyRequest(
        String[] includedTypes,
        int maxResultCount,
        LocationRestriction locationRestriction) {

    public SearchNearbyRequest(LatLng center) {
        this(new String[]{"museum",
                "tourist_attraction",
                "cultural_center"}, 10, new LocationRestriction(new Circle(center, 4000)));
    }

    public static record LocationRestriction(Circle circle) {

    }

    public static record Circle(LatLng center, int radius) {

    }
}
