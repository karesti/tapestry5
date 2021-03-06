// Copyright 2007, 2008, 2010 The Apache Software Foundation
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

package org.apache.tapestry5.internal.services;

import java.io.IOException;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.services.ajax.AjaxFormUpdateController;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.services.ComponentEventResultProcessor;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;

/**
 * Processor for objects that implement {@link RenderCommand} (such as
 * {@link org.apache.tapestry5.internal.structure.BlockImpl}).
 * 
 * @see AjaxPartialResponseRenderer#renderPartialPageMarkup()
 */
public class RenderCommandComponentEventResultProcessor implements ComponentEventResultProcessor<RenderCommand>,
        PartialMarkupRendererFilter
{
    private final PageRenderQueue pageRenderQueue;

    private final AjaxFormUpdateController ajaxFormUpdateController;

    public RenderCommandComponentEventResultProcessor(PageRenderQueue pageRenderQueue,
            AjaxFormUpdateController ajaxFormUpdateController)
    {
        this.pageRenderQueue = pageRenderQueue;
        this.ajaxFormUpdateController = ajaxFormUpdateController;
    }

    public void processResultValue(final RenderCommand value) throws IOException
    {
        pageRenderQueue.addPartialMarkupRendererFilter(this);
        pageRenderQueue.initializeForPartialPageRender(value);
    }

    /**
     * As a filter, this class does three things:
     * <ul>
     * <li>It creates an outer element to capture the partial page content that will be rendered</li>
     * <li>It does setup and cleanup with the {@link AjaxFormUpdateController}</li>
     * <li>It extracts the child markup and stuff it into the reply's "content" property.</li>
     * </ul>
     */
    public void renderMarkup(MarkupWriter writer, JSONObject reply, PartialMarkupRenderer renderer)
    {
        // The partial will quite often contain multiple elements (or just a block of plain text),
        // so those must be enclosed in a root element.

        Element root = writer.element("ajax-partial");

        ajaxFormUpdateController.setupBeforePartialZoneRender(writer);

        renderer.renderMarkup(writer, reply);

        ajaxFormUpdateController.cleanupAfterPartialZoneRender();

        writer.end();

        String content = root.getChildMarkup().trim();

        reply.put("content", content);
    }
}
