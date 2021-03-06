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

package org.apache.tapestry5.corelib;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.corelib.components.LinkSubmit;
import org.apache.tapestry5.corelib.components.Submit;

/**
 * Defines the client and server-side behavior of a {@link Submit} or {@link LinkSubmit} component.
 * 
 * @since 5.2.0
 */
public enum SubmitMode
{
    /** Submit the form normally, with full validation on the client and server side. */
    NORMAL,

    /**
     * Cancel the form; bypass client-side form validation. Validation still occurs
     * on the server, though generally the data is discarded by the event listener
     * (listening to the Submit component's {@link EventConstants#SELECTED} event).
     */
    CANCEL;
}
