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
package com.apuntesdejava.ai.traveling.resource;

import com.apuntesdejava.ai.traveling.model.LatLng;
import com.apuntesdejava.ai.traveling.model.Place;
import com.apuntesdejava.ai.traveling.service.PlacesService;
import com.apuntesdejava.ai.traveling.service.RoutesService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.concurrent.CompletionStage;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
@Path("routes")
@Produces(APPLICATION_JSON)
public class RoutesResource {

    private static final Logger LOGGER = Logger.getLogger(RoutesResource.class.getName());

    @Inject
    private RoutesService routesService;

    @Inject
    private PlacesService placesService;

    @GET
    @Operation(summary = "Get all the tourist places that exist on the route between two locations")
    public void getRoutes(
            @Suspended AsyncResponse asyncResponse,
            @QueryParam("originLat") double originLat,
            @QueryParam("originLng") double originLng,
            @QueryParam("destinationLat") double destinationLat,
            @QueryParam("destinationLng") double destinationLng) {

        var origin = new LatLng(originLat, originLng);
        var destination = new LatLng(destinationLat, destinationLng);
        var routes = routesService.calculateRoutes(origin, destination);
        LOGGER.info("searching recommendations");
        var places = routes.stream()
                .map(latLng -> placesService.getRecommendations(latLng)
                .thenApply(recommendations -> recommendations)).toList();
        var placesArray = (CompletableFuture<List<Place>>[]) places.toArray(new CompletableFuture[0]);

        CompletableFuture.allOf(placesArray)
                .thenApply(v -> places.stream()
                .map(CompletionStage::toCompletableFuture)
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toMap(Place::id, Function.identity(),
                        (existing, replacement) -> existing))
                )
                .thenAccept(asyncResponse::resume)
                .exceptionally(ex -> {
                    asyncResponse.resume(ex);
                    return null;
                });

        LOGGER.info("end searching recommendations");

    }

}
