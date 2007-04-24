// Copyright 2006 The Apache Software Foundation
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tapestry.internal.test.InternalBaseTestCase;
import org.apache.tapestry.services.Request;
import org.apache.tapestry.services.Session;
import org.testng.annotations.Test;

public class WebRequestImplTest extends InternalBaseTestCase
{
    @Test
    public void get_session_doesnt_exist()
    {
        HttpServletRequest sr = newHttpServletRequest();

        train_getSession(sr, false, null);

        replay();

        Request request = new RequestImpl(sr);

        assertNull(request.getSession(false));

        verify();
    }

    @Test
    public void force_session_create()
    {
        HttpServletRequest sr = newHttpServletRequest();
        HttpSession ss = newHttpSession();

        train_getSession(sr, true, ss);

        train_getAttribute(ss, "foo", "bar");

        replay();

        Request request = new RequestImpl(sr);
        Session session = request.getSession(true);

        assertEquals(session.getAttribute("foo"), "bar");

        verify();
    }
}
