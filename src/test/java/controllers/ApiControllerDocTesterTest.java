/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import models.QuipDto;
import models.QuipsDto;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import ninja.NinjaDocTester;
import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;
import org.hamcrest.CoreMatchers;

public class ApiControllerDocTesterTest extends NinjaDocTester {
    
    String GET_QUIPS_URL = "/api/{username}/quips.json";
    String GET_QUIP_URL = "/api/{username}/quip/{id}.json";
    String POST_QUIP_URL = "/api/{username}/quip.json";
    String LOGIN_URL = "/login";
    
    String USER = "bob@gmail.com";

    @Test
    public void testGetAndPostQuipViaJson() {

        // /////////////////////////////////////////////////////////////////////
        // Test initial data:
        // /////////////////////////////////////////////////////////////////////
        
        sayNextSection("Retrieving quips for a user (Json)");
        
        say("Retrieving all quips of a user is a GET request to " + GET_QUIPS_URL);
        
        Response response = sayAndMakeRequest(
                Request.GET().url(
                        testServerUrl().path(GET_QUIPS_URL.replace("{username}", "bob@gmail.com"))));

        QuipsDto quipsDto = getGsonWithLongToDateParsing().fromJson(response.payload, QuipsDto.class);

        sayAndAssertThat("We get back all 3 quips of that user",
                quipsDto.quips.size(),
                CoreMatchers.is(3));

        // /////////////////////////////////////////////////////////////////////
        // Post new quip:
        // /////////////////////////////////////////////////////////////////////
        sayNextSection("Posting new quip (Json)");
        
        say("Posting a new quip is a post request to " + POST_QUIP_URL);
        say("Please note that you have to be authenticated in order to be allowed to post.");
        
        QuipDto quipDto = new QuipDto();
        quipDto.content = "contentcontent";
        quipDto.sourceDate = new Date(System.currentTimeMillis());

        response = sayAndMakeRequest(
                Request.POST().url(
                        testServerUrl().path(POST_QUIP_URL.replace("{username}", USER)))
                        .payload(quipDto));
        
        sayAndAssertThat(
                "You have to be authenticated in order to post quips"
                , response.httpStatus 
                , CoreMatchers.is(403));
        
        doLogin();

        say("Now we are authenticated and expect the post to succeed...");
        response = sayAndMakeRequest(Request.POST().url(
                testServerUrl().path(POST_QUIP_URL.replace("{username}", USER)))
                .contentTypeApplicationJson()
                .payload(quipDto));

        sayAndAssertThat("After successful login we are able to post quips"
                , response.httpStatus
                , CoreMatchers.is(200));

        // /////////////////////////////////////////////////////////////////////
        // Fetch quips again => assert we got a new one ...
        // /////////////////////////////////////////////////////////////////////
        
        say("If we now fetch the quips again we are getting a new quip (the one we have posted successfully");
        response = sayAndMakeRequest(Request.GET().url(testServerUrl().path(GET_QUIPS_URL.replace("{username}", "bob@gmail.com"))));

        quipsDto = getGsonWithLongToDateParsing().fromJson(response.payload, QuipsDto.class);
        // one new result:
        sayAndAssertThat("We are now getting 4 quips."
                , quipsDto.quips.size()
                , CoreMatchers.is(4));
        
        
    }



    private Gson getGsonWithLongToDateParsing() {
        // Creates the json object which will manage the information received
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json,
                                    Type typeOfT,
                                    JsonDeserializationContext context)
                    throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();

        return gson;
    }

    private void doLogin() {

        say("To authenticate we send our credentials to " + LOGIN_URL);
        say("We are then issued a cookie from the server that authenticates us in further requests");

        Map<String, String> formParameters = Maps.newHashMap();
        formParameters.put("username", "bob@gmail.com");
        formParameters.put("password", "secret");
        
        makeRequest(
                Request.POST().url(
                    testServerUrl().path(LOGIN_URL))
                .addFormParameter("username", "bob@gmail.com")
                .addFormParameter("password", "secret"));
         }

}
