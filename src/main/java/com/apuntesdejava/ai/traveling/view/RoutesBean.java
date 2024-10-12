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
package com.apuntesdejava.ai.traveling.view;

import com.apuntesdejava.ai.traveling.api.GoogleApiUtil;
import com.apuntesdejava.ai.traveling.model.LatLng;
import com.apuntesdejava.ai.traveling.service.RoutesService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
@Named(value = "routesBean")
@SessionScoped
public class RoutesBean implements Serializable {

    @Inject
    private RoutesService routesService;


    private String origin;
    private String destination;
    private List<LatLng> steps;
    private LatLng originLatLng;
    private LatLng destinationLatLng;

    /**
     * Creates a new instance of RoutesBean
     */
    public RoutesBean() {
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void calculateRoutes() {
        this.steps = routesService.calculateRoutes(origin, destination);
        this.originLatLng = steps.getFirst();
        this.destinationLatLng = steps.getLast();
    }

    public boolean showRoutes() {
        return steps != null && !steps.isEmpty();
    }


    public String getApiKey() {
        return GoogleApiUtil.getApiKey();
    }

    public String getDestinationEncoded() {
        return URLEncoder.encode(destination, UTF_8);
    }

    public LatLng getOriginLatLng() {
        return originLatLng;
    }

    public LatLng getDestinationLatLng() {
        return destinationLatLng;
    }
}
