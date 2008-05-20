// Copyright 2007, 2008 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.internal.services;

import org.apache.tapestry.internal.InternalConstants;
import org.apache.tapestry.services.ComponentEventResultProcessor;
import org.apache.tapestry.services.Response;
import org.apache.tapestry5.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;


/**
 * Implemention of {@link ComponentEventResultProcessor} for {@link org.apache.tapestry5.json.JSONObject}, allowing a
 * component event handler to return a JSONObject that will be sent directly to the client as the reply. This is often
 * used with custom components that need a custom JSON response.
 */
public class JSONObjectEventResultProcessor implements ComponentEventResultProcessor<JSONObject>
{
    private final Response response;

    public JSONObjectEventResultProcessor(Response response)
    {
        this.response = response;
    }

    public void processResultValue(JSONObject value) throws IOException
    {
        PrintWriter pw = response.getPrintWriter(InternalConstants.JSON_MIME_TYPE);

        pw.print(value.toString());

        pw.flush();
    }
}