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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import models.QuipDto;
import models.QuipsDto;
import ninja.NinjaTest;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ApiControllerTest extends NinjaTest {
    
    @Before
    public void setup() {
        
        ninjaTestBrowser.makeRequest(getServerAddress() + "setup");
        
    }

    @Test
    public void testGetAndPostQuipViaJson() throws Exception {

        // /////////////////////////////////////////////////////////////////////
        // Test initial data:
        // /////////////////////////////////////////////////////////////////////
        String response = ninjaTestBrowser.makeJsonRequest(getServerAddress()
                + "api/bob@gmail.com/quips.json");
        System.out.println("response: " + response);

        QuipsDto quipsDto = getGsonWithLongToDateParsing().fromJson(
                response, QuipsDto.class);

        assertEquals(3, quipsDto.quips.size());

        // /////////////////////////////////////////////////////////////////////
        // Post new quip:
        // /////////////////////////////////////////////////////////////////////
        QuipDto quipDto = new QuipDto();
        quipDto.content = "contentcontent";
        quipDto.sourceDate = new Date(System.currentTimeMillis());

        response = ninjaTestBrowser.postJson(getServerAddress()
                + "api/bob@gmail.com/quip.json", quipDto);

        assertTrue(response.contains("Error. Forbidden."));

        doLogin();

        response = ninjaTestBrowser.postJson(getServerAddress()
                + "api/bob@gmail.com/quip.json", quipDto);

        assertFalse(response.contains("Error. Forbidden."));

        // /////////////////////////////////////////////////////////////////////
        // Fetch quips again => assert we got a new one ...
        // /////////////////////////////////////////////////////////////////////
        response = ninjaTestBrowser.makeJsonRequest(getServerAddress()
                + "api/bob@gmail.com/quips.json");

        quipsDto = getGsonWithLongToDateParsing().fromJson(response, QuipsDto.class);
        // one new result:
        assertEquals(4, quipsDto.quips.size());

    }

    @Test
    public void testGetAndPostQuipViaXml() throws Exception {

        // /////////////////////////////////////////////////////////////////////
        // Test initial data:
        // /////////////////////////////////////////////////////////////////////
        String response = ninjaTestBrowser.makeXmlRequest(getServerAddress()
                + "api/bob@gmail.com/quips.xml");
        System.out.println("response xml: " + response);
        
        JacksonXmlModule module = new JacksonXmlModule();
        // and then configure, for example:
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        

        QuipsDto quipsDto = xmlMapper.readValue(response, QuipsDto.class);

        assertEquals(3, quipsDto.quips.size());

        // /////////////////////////////////////////////////////////////////////
        // Post new quip:
        // /////////////////////////////////////////////////////////////////////
        QuipDto quipDto = new QuipDto();
        quipDto.content = "contentcontent";
        quipDto.sourceDate = new Date(System.currentTimeMillis());

        response = ninjaTestBrowser.postXml(getServerAddress()
                + "api/bob@gmail.com/quip.xml", quipDto);

        assertTrue(response.contains("Error. Forbidden."));

        doLogin();

        response = ninjaTestBrowser.postXml(getServerAddress()
                + "api/bob@gmail.com/quip.xml", quipDto);

        assertFalse(response.contains("Error. Forbidden."));

        // /////////////////////////////////////////////////////////////////////
        // Fetch quips again => assert we got a new one ...
        // /////////////////////////////////////////////////////////////////////
        response = ninjaTestBrowser.makeXmlRequest(getServerAddress()
                + "api/bob@gmail.com/quips.xml");

        quipsDto = xmlMapper.readValue(response, QuipsDto.class);
        // one new result:
        assertEquals(4, quipsDto.quips.size());

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

        Map<String, String> headers = Maps.newHashMap();

        Map<String, String> formParameters = Maps.newHashMap();
        formParameters.put("username", "bob@gmail.com");
        formParameters.put("password", "secret");

        ninjaTestBrowser.makePostRequestWithFormParameters(getServerAddress()
                + "login", headers, formParameters);

    }

}
