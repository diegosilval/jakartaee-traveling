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
package com.apuntesdejava.ai.traveling.api;

import com.apuntesdejava.ai.traveling.api.request.SearchNearbyRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.concurrent.CompletionStage;

/**
 *
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
@RegisterRestClient
@ClientHeaderParam(name = "X-Goog-Api-Key", value = "{com.apuntesdejava.ai.traveling.api.GoogleApiUtil.getApiKey}")
public interface PlacesApi {

    @Path("/v1/places:searchNearby")
    @POST
    @Produces(APPLICATION_JSON)
    @ClientHeaderParam(name = "X-Goog-FieldMask", value = "places.displayName,places.name,places.id,places.types,places.nationalPhoneNumber,places.internationalPhoneNumber,places.formattedAddress,places.location,places.viewport,places.rating,places.googleMapsUri,places.websiteUri,places.iconMaskBaseUri")
    CompletionStage<Response> searchNearby(SearchNearbyRequest request);
}
