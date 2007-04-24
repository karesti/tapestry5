// Copyright 2007 The Apache Software Foundation
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

package org.apache.tapestry.corelib.components;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import org.apache.tapestry.ComponentResources;
import org.apache.tapestry.MarkupWriter;
import org.apache.tapestry.test.TapestryTestCase;
import org.testng.annotations.Test;

public class OutputTest extends TapestryTestCase
{
    private final Number _value = new Double(22.7d);

    private final Format _format = new DecimalFormat("0.00");

    @Test
    public void simple_output()
    {
        MarkupWriter writer = createMarkupWriter();
        ComponentResources resources = newComponentResources();

        replay();

        Output component = new Output();

        component.setup(_value, _format, null, resources);

        writer.element("root");
        assertFalse(component.beginRender(writer));
        writer.end();

        verify();

        assertEquals(writer.toString(), "<root>22.70</root>");
    }

    @Test
    public void output_with_element_and_informals()
    {
        String elementName = "span";

        MarkupWriter writer = createMarkupWriter();

        ComponentResources resources = newComponentResources();

        train_renderInformalParameters(resources, writer, "foo", "bar");

        replay();

        Output component = new Output();

        component.setup(_value, _format, elementName, resources);

        assertFalse(component.beginRender(writer));

        verify();

        assertEquals(writer.toString(), "<span foo=\"bar\">22.70</span>");
    }

    @Test
    public void null_format_is_a_noop()
    {
        String elementName = "span";

        MarkupWriter writer = createMarkupWriter();

        ComponentResources resources = newComponentResources();

        Format format = new Format()
        {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
            {
                return toAppendTo;
            }

            @Override
            public Object parseObject(String source, ParsePosition pos)
            {
                return null;
            }
        };

        replay();

        Output component = new Output();

        component.setup(_value, format, elementName, resources);

        writer.element("root");
        assertFalse(component.beginRender(writer));
        writer.end();

        verify();

        assertEquals(writer.toString(), "<root></root>");
    }
}
