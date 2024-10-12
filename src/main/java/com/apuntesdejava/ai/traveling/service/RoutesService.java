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

import com.apuntesdejava.ai.traveling.api.RoutesApi;
import com.apuntesdejava.ai.traveling.api.request.ComputeRouteRequest;
import com.apuntesdejava.ai.traveling.model.Waypoint;
import com.apuntesdejava.ai.traveling.model.LatLng;
import com.apuntesdejava.ai.traveling.model.Location;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
@ApplicationScoped
public class RoutesService {

    private static final Logger LOGGER = Logger.getLogger(RoutesService.class.getName());

    @Inject
    private RoutesApi routesApi;

    public List<LatLng> calculateRoutes(String origin, String destination) {
        return calculateRoutes(new Waypoint(origin), new Waypoint(destination));
    }

    public List<LatLng> calculateRoutes(LatLng origin, LatLng destination) {
        return calculateRoutes(new Waypoint(new Location(origin)), new Waypoint(new Location(destination)));
    }

    private List<LatLng> calculateRoutes(Waypoint origin, Waypoint destination) {
        LOGGER.info("origin:%s destination:%s".formatted(origin, destination));
        try (var response = routesApi.computeRoutes(new ComputeRouteRequest(origin, destination))) {
            JsonObject routes = response.readEntity(JsonObject.class);
            LOGGER.info("getStatus:%s".formatted(response.getStatus()));
            if (routes.containsKey("routes")) {
                var steps = routes.getJsonArray("routes").getJsonObject(0)
                        .getJsonArray("legs").getJsonObject(0)
                        .getJsonArray("steps");
                var stepsCollection = steps.stream().map(JsonValue::asJsonObject)
                        .map(item -> item.getJsonObject("endLocation"))
                        .map(endLocation -> endLocation.getJsonObject("latLng"))
                        .map(latLng -> new LatLng(
                                latLng.getJsonNumber("latitude").doubleValue(),
                                latLng.getJsonNumber("longitude").doubleValue())
                        )
                        .toList();

                LOGGER.info("stepsCollection:%s".formatted(stepsCollection));
                return stepsCollection;
            }
        }
        return Collections.emptyList();
    }

}
