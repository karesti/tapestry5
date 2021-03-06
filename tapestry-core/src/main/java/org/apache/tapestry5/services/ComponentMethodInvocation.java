// Copyright 2008 The Apache Software Foundation
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

import org.apache.tapestry5.ioc.Invocation;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.runtime.ComponentResourcesAware;

/**
 * Encapsulates the parameters, thrown exceptions, and result of a method invocation, allowing a
 * {@link org.apache.tapestry5.services.ComponentMethodAdvice} to encapsulate the invocation. Extends Invocation with
 * the {@link org.apache.tapestry5.ComponentResources} of the component for which a method is being advised.
 */
public interface ComponentMethodInvocation extends Invocation, ComponentResourcesAware
{
    /**
     * Returns the component instance containing the advised method.
     * 
     * @since 5.2.0
     */
    Component getInstance();
}
