// Copyright 2006, 2007, 2008, 2010 The Apache Software Foundation
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

package org.apache.tapestry5.ioc.internal;

import java.util.Collection;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ObjectLocator;

/**
 * Wraps a {@link java.util.Collection} as a {@link org.apache.tapestry5.ioc.Configuration} and perform validation that
 * collected value are of the correct type.
 */
public class ValidatingConfigurationWrapper<T> extends AbstractConfigurationImpl<T> implements Configuration<T>
{
    private final String serviceId;

    private final Class<T> expectedType;

    private final Collection<T> collection;

    public ValidatingConfigurationWrapper(Class<T> expectedType, ObjectLocator locator, Collection<T> collection,
            String serviceId)
    {
        super(expectedType, locator);

        this.collection = collection;
        this.serviceId = serviceId;
        this.expectedType = expectedType;
    }

    public void add(T object)
    {
        if (object == null)
            throw new NullPointerException(IOCMessages.contributionWasNull(serviceId));

        // Sure, we say it is type T ... but is it really?

        if (!expectedType.isInstance(object))
            throw new IllegalArgumentException(IOCMessages.contributionWrongValueType(serviceId, object.getClass(),
                    expectedType));

        collection.add(object);
    }

    public void addInstance(Class<? extends T> clazz)
    {
        add(instantiate(clazz));
    }
}
