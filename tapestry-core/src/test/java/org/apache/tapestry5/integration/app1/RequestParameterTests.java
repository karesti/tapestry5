// Copyright 2010 The Apache Software Foundation
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

package org.apache.tapestry5.integration.app1;

import org.apache.tapestry5.integration.TapestryCoreTestCase;
import org.testng.annotations.Test;

public class RequestParameterTests extends TapestryCoreTestCase
{
    @Test
    public void successful_use_of_query_parameter_annotation()
    {
        clickThru("RequestParameter Annotation Demo", "Working Link");

        assertText("id=current", "97");
    }

    @Test
    public void null_value_when_not_allowed()
    {
        clickThru("RequestParameter Annotation Demo", "Null Link");

        assertTextPresent(
                "Unable process query parameter 'gnip' as parameter #1 of event handler method void onFrob(int) (in class org.apache.tapestry5.integration.app1.pages.RequestParameterDemo)",
                "The value for query parameter 'gnip' was blank, but a non-blank value is needed.");
    }

    @Test
    public void null_for_primitive_when_allowed()
    {
        clickThru("RequestParameter Annotation Demo", "Null Allowed Link");

        assertTextPresent(
                "Unable process query parameter 'gnip' as parameter #1 of event handler method void onFrobNullAllowed(int) (in class org.apache.tapestry5.integration.app1.pages.RequestParameterDemo)",
                "Query parameter 'gnip' evaluates to null, but the event method parameter is type int, a primitive.");
    }

    @Test
    public void type_mismatch_for_method_parameter()
    {
        clickThru("RequestParameter Annotation Demo", "Broken Link");

        assertTextPresent(
                "Unable process query parameter 'gnip' as parameter #1 of event handler method void onFrob(int) (in class org.apache.tapestry5.integration.app1.pages.RequestParameterDemo)",
                "Coercion of frodo to type java.lang.Integer");
    }
}
