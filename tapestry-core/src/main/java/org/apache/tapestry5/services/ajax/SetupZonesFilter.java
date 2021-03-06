// Copyright 2009 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry5.services.ajax;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;

/**
 * Creates a "zones" object in the JSON reply, ready for the
 * {@link org.apache.tapestry5.services.ajax.SingleZonePartialRendererFilter}s
 * to store values into.
 * 
 * @since 5.1.0.1
 */
public class SetupZonesFilter implements PartialMarkupRendererFilter
{
    public void renderMarkup(MarkupWriter writer, JSONObject reply, PartialMarkupRenderer renderer)
    {
        reply.put("zones", new JSONObject());

        renderer.renderMarkup(writer, reply);
    }
}
