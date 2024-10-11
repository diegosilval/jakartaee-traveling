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

import com.apuntesdejava.ai.traveling.api.GenerativeLanguageApi;
import com.apuntesdejava.ai.traveling.api.request.GenerateContentRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

import jakarta.json.JsonObject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
@ApplicationScoped
public class GenerateTextService {

    private static final Logger LOGGER = Logger.getLogger(GenerateTextService.class.getName());

    @Inject
    private GenerativeLanguageApi generativeLanguageApi;

    @ConfigProperty(name = "google.api.key")
    @Inject
    private String apiKey;

    @ConfigProperty(name = "generate.content.template")
    @Inject
    private String generateContentTemplate;

    public String getTextGenerate(String displayName, String location) {
        String prompt = generateContentTemplate.formatted(displayName, location);
        try (var response = generativeLanguageApi.generateContent(apiKey, new GenerateContentRequest(prompt))) {
            var jsonResponse = response.readEntity(JsonObject.class);
            LOGGER.info("IA: %s -> %s".formatted(prompt, jsonResponse));
            return jsonResponse.getJsonArray("candidates")
                    .getJsonObject(0)
                    .getJsonObject("content")
                    .getJsonArray("parts")
                    .getJsonObject(0)
                    .getString("text");
        }
    }
}
