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

package org.apache.tapestry5.services;

import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.runtime.ComponentEvent;

/**
 * Interface used with {@link ClassTransformation#addComponentEventHandler(String, int, String, ComponentEventHandler)}.
 * 
 * @since 5.2.0
 */
public interface ComponentEventHandler
{
    /** Handles the event. */
    void handleEvent(Component instance, ComponentEvent event);
}
