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

import com.apuntesdejava.ai.traveling.api.request.GenerateContentRequest;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * @author Diego Silva <diego.silva at apuntesdejava.com>
 */
@RegisterRestClient
public interface GenerativeLanguageApi {

    @POST
    @Path("/v1beta/models/gemini-1.5-flash:generateContent")
    Response generateContent(
            @QueryParam("key") @DefaultValue("{com.apuntesdejava.ai.traveling.api.GoogleApiUtil.getApiKey}") String key,
            GenerateContentRequest request
    );
}
