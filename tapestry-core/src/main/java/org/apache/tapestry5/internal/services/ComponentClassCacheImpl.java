// Copyright 2008, 2010 The Apache Software Foundation
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

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ClassFabUtils;
import org.apache.tapestry5.ioc.services.ClassFactory;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ComponentLayer;
import org.apache.tapestry5.services.InvalidationListener;

import java.util.Map;

public class ComponentClassCacheImpl implements ComponentClassCache, InvalidationListener
{
    private final Map<String, Class> cache = CollectionFactory.newConcurrentMap();

    private final ClassFactory classFactory;

    private final TypeCoercer typeCoercer;

    public ComponentClassCacheImpl(@ComponentLayer
    ClassFactory classFactory, TypeCoercer typeCoercer)
    {
        this.classFactory = classFactory;
        this.typeCoercer = typeCoercer;
    }

    public void objectWasInvalidated()
    {
        cache.clear();
    }

    @SuppressWarnings("unchecked")
    public Object defaultValueForType(String className)
    {
        Class clazz = forName(className);

        if (!clazz.isPrimitive())
            return null;

        // Remembering that 0 coerces to boolean false, this covers all the primitive
        // types (boolean, int, short, etc.)
        return typeCoercer.coerce(0, clazz);
    }

    public Class forName(String className)
    {
        Class result = cache.get(className);

        if (result == null)
        {
            result = lookupClassForType(className);

            cache.put(className, result);
        }

        return result;
    }

    private Class lookupClassForType(String className)
    {
        if (className.equals("void"))
            return void.class;

        if (ClassFabUtils.isPrimitiveType(className))
            return ClassFabUtils.getPrimitiveType(className);

        // This step is necessary to handle primitives arrays.

        String jvmName = ClassFabUtils.toJVMBinaryName(className);

        ClassLoader componentLoader = classFactory.getClassLoader();

        try
        {
            return Class.forName(jvmName, true, componentLoader);
        }
        catch (ClassNotFoundException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
