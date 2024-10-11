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
package com.apuntesdejava.ai.traveling.service;

import com.apuntesdejava.ai.traveling.api.PlacesApi;
import com.apuntesdejava.ai.traveling.api.request.SearchNearbyRequest;
import com.apuntesdejava.ai.traveling.model.LatLng;
import com.apuntesdejava.ai.traveling.model.Place;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
@ApplicationScoped
public class PlacesService {

    private static final Logger LOGGER = Logger.getLogger(PlacesService.class.getName());

    @Inject
    private PlacesApi placesApi;

    public CompletionStage<List<Place>> getRecommendations(LatLng latLng) {
        LOGGER.info("searching recommendations for %s:".formatted(latLng));
        return placesApi.searchNearby(new SearchNearbyRequest(latLng)).thenApply(response -> {
            var jsonResponse = response.readEntity(JsonObject.class);

            if (jsonResponse.containsKey("places")) {
                return jsonResponse.getJsonArray("places").stream()
                        .map(JsonValue::asJsonObject)
                        .map(item -> new Place(item.getString("id"),
                                item.getJsonObject("displayName").getString("text"),
                                item.getString("formattedAddress"),
                                item)
                        )
                        .toList();
            }
            return Collections.emptyList();
        });


    }

}
