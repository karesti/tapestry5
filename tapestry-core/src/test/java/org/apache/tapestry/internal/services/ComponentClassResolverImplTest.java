// Copyright 2006, 2007 The Apache Software Foundation
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

import static org.easymock.EasyMock.isA;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry.internal.test.InternalBaseTestCase;
import org.apache.tapestry.services.ComponentClassResolver;
import org.apache.tapestry.services.LibraryMapping;
import org.testng.annotations.Test;

public class ComponentClassResolverImplTest extends InternalBaseTestCase
{
    private static final String APP_ROOT_PACKAGE = "org.example.app";

    private static final String CORE_PREFIX = "core";

    private static final String CORE_ROOT_PACKAGE = "org.apache.tapestry.corelib";

    private static final String LIB_PREFIX = "lib";

    private static final String LIB_ROOT_PACKAGE = "org.example.lib";

    private ComponentClassResolverImpl create(ComponentInstantiatorSource source,
            ComponentClassLocator locator, LibraryMapping... mappings)
    {
        List<LibraryMapping> list = Arrays.asList(mappings);

        ComponentClassResolverImpl resolver = new ComponentClassResolverImpl(source, locator, list);

        resolver.setApplicationPackage(APP_ROOT_PACKAGE);

        return resolver;
    }

    @Test
    public void simple_page_name()
    {
        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_app_packages(source);

        String className = APP_ROOT_PACKAGE + ".pages.SimplePage";

        train_locateComponentClassNames(locator, APP_ROOT_PACKAGE + ".pages", className);

        replay();

        ComponentClassResolver resolver = create(source, locator);

        assertEquals(resolver.resolvePageNameToClassName("SimplePage"), className);

        verify();
    }

    @Test
    public void is_page_name()
    {
        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_app_packages(source);

        String className = APP_ROOT_PACKAGE + ".pages.SimplePage";

        train_locateComponentClassNames(locator, APP_ROOT_PACKAGE + ".pages", className);

        replay();

        ComponentClassResolver resolver = create(source, locator);

        assertTrue(resolver.isPageName("SimplePage"));
        assertTrue(resolver.isPageName("simplepage"));
        assertFalse(resolver.isPageName("UnknownPage"));

        verify();
    }

    @Test
    public void is_page_name_for_core_page()
    {
        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_app_packages(source);
        train_for_packages(source, CORE_ROOT_PACKAGE);

        String className = CORE_ROOT_PACKAGE + ".pages.CorePage";

        train_locateComponentClassNames(locator, CORE_ROOT_PACKAGE + ".pages", className);

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping(CORE_PREFIX,
                CORE_ROOT_PACKAGE));

        // Can look like an application page, but still resolves to the core library class name.

        assertTrue(resolver.isPageName("CorePage"));

        // Or we can give it its true name

        assertTrue(resolver.isPageName("core/corepage"));

        assertFalse(resolver.isPageName("UnknownPage"));

        verify();
    }

    protected final ComponentClassLocator newComponentClassLocator()
    {
        ComponentClassLocator locator = newMock(ComponentClassLocator.class);

        stub_locateComponentClassNames(locator);

        return locator;
    }

    private void stub_locateComponentClassNames(ComponentClassLocator locator)
    {
        Collection<String> noMatches = Collections.emptyList();

        expect(locator.locateComponentClassNames(isA(String.class))).andStubReturn(noMatches);
    }

    protected final void train_locateComponentClassNames(ComponentClassLocator locator,
            String packageName, String... classNames)
    {
        expect(locator.locateComponentClassNames(packageName)).andReturn(Arrays.asList(classNames));
    }

    @Test
    public void class_name_to_simple_page_name()
    {
        String className = APP_ROOT_PACKAGE + ".pages.SimplePage";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_app_packages(source);

        train_locateComponentClassNames(locator, APP_ROOT_PACKAGE + ".pages", className);

        replay();

        ComponentClassResolver resolver = create(source, locator);

        assertEquals(resolver.resolvePageClassNameToPageName(className), "SimplePage");

        verify();
    }

    /** All of the caches are handled identically, so we just test the pages for caching. */
    @Test
    public void resolved_page_names_are_cached()
    {

        String pageClassName = APP_ROOT_PACKAGE + ".pages.SimplePage";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_app_packages(source);

        train_locateComponentClassNames(locator, APP_ROOT_PACKAGE + ".pages", pageClassName);

        replay();

        ComponentClassResolverImpl resolver = create(source, locator);

        assertEquals(resolver.resolvePageNameToClassName("SimplePage"), pageClassName);

        verify();

        // No more training, because it's already cached.

        replay();

        assertEquals(resolver.resolvePageNameToClassName("SimplePage"), pageClassName);

        verify();

        // After clearing the cache, redoes the work.

        train_locateComponentClassNames(locator, APP_ROOT_PACKAGE + ".pages", pageClassName);
        stub_locateComponentClassNames(locator);

        replay();

        resolver.objectWasInvalidated();

        assertEquals(resolver.resolvePageNameToClassName("SimplePage"), pageClassName);

        verify();
    }

    @Test
    public void page_found_in_core_lib()
    {
        String className = CORE_ROOT_PACKAGE + ".pages.CorePage";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        train_locateComponentClassNames(locator, CORE_ROOT_PACKAGE + ".pages", className);

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping(CORE_PREFIX,
                CORE_ROOT_PACKAGE));

        assertEquals(resolver.resolvePageNameToClassName("CorePage"), className);

        verify();
    }

    @Test
    public void page_class_name_resolved_to_core_page()
    {
        String className = CORE_ROOT_PACKAGE + ".pages.CorePage";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        train_locateComponentClassNames(locator, CORE_ROOT_PACKAGE + ".pages", className);

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping(CORE_PREFIX,
                CORE_ROOT_PACKAGE));

        assertEquals(resolver.resolvePageClassNameToPageName(className), "core/CorePage");

        verify();
    }

    @Test
    public void page_found_in_library()
    {
        String className = LIB_ROOT_PACKAGE + ".pages.LibPage";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, LIB_ROOT_PACKAGE);
        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        train_locateComponentClassNames(locator, LIB_ROOT_PACKAGE + ".pages", className);

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping(LIB_PREFIX,
                LIB_ROOT_PACKAGE), new LibraryMapping(CORE_PREFIX, CORE_ROOT_PACKAGE));

        assertEquals(resolver.resolvePageNameToClassName("lib/LibPage"), className);

        verify();
    }

    @Test
    public void lookup_by_logical_name_is_case_insensitive()
    {
        String className = LIB_ROOT_PACKAGE + ".pages.LibPage";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, LIB_ROOT_PACKAGE);
        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        train_locateComponentClassNames(locator, LIB_ROOT_PACKAGE + ".pages", className);

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping(LIB_PREFIX,
                LIB_ROOT_PACKAGE), new LibraryMapping(CORE_PREFIX, CORE_ROOT_PACKAGE));

        assertEquals(resolver.resolvePageNameToClassName("lib/libpage"), className);
        assertEquals(resolver.resolvePageNameToClassName("LIB/LIBPAGE"), className);

        verify();
    }

    @Test
    public void class_name_does_not_resolve_to_page_name()
    {
        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping(CORE_PREFIX,
                CORE_ROOT_PACKAGE));

        String className = LIB_ROOT_PACKAGE + ".pages.LibPage";

        try
        {
            resolver.resolvePageClassNameToPageName(className);
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals(ex.getMessage(), "Unable to resolve class name " + className
                    + " to a logical page name.");
        }

        verify();
    }

    @Test
    public void class_name_not_in_a_pages_package()
    {
        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping(CORE_PREFIX,
                CORE_ROOT_PACKAGE));

        String className = CORE_ROOT_PACKAGE + ".foo.CorePage";

        try
        {
            resolver.resolvePageClassNameToPageName(className);
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertEquals(ex.getMessage(), "Unable to resolve class name " + className
                    + " to a logical page name.");
        }

        verify();
    }

    @Test
    public void multiple_mappings_for_same_prefix()
    {
        String secondaryLibPackage = "org.examples.addon.lib";
        String className = secondaryLibPackage + ".pages.LibPage";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, LIB_ROOT_PACKAGE);
        train_for_packages(source, secondaryLibPackage);
        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        train_locateComponentClassNames(locator, secondaryLibPackage + ".pages", className);

        replay();

        ComponentClassResolver resolver = create(
                source,
                locator,
                new LibraryMapping(LIB_PREFIX, LIB_ROOT_PACKAGE),
                new LibraryMapping(LIB_PREFIX, secondaryLibPackage),
                new LibraryMapping(CORE_PREFIX, CORE_ROOT_PACKAGE));

        assertEquals(resolver.resolvePageNameToClassName("lib/LibPage"), className);

        verify();
    }

    @Test
    public void complex_prefix_search_fails()
    {
        String deepPackage = "org.deep";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, deepPackage);
        train_for_packages(source, LIB_ROOT_PACKAGE);
        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        // Is this test even needed any more with the new algorithm?

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping("lib/deep",
                deepPackage), new LibraryMapping(LIB_PREFIX, LIB_ROOT_PACKAGE), new LibraryMapping(
                CORE_PREFIX, CORE_ROOT_PACKAGE));

        try
        {
            resolver.resolvePageNameToClassName("lib/deep/DeepPage");
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertTrue(ex.getMessage().contains(
                    "Unable to resolve page 'lib/deep/DeepPage' to a component class name."));
        }

        verify();
    }

    private void train_for_packages(ComponentInstantiatorSource source, String packageName)
    {
        source.addPackage(packageName + ".pages");
        source.addPackage(packageName + ".components");
        source.addPackage(packageName + ".mixins");
        source.addPackage(packageName + ".base");
    }

    /**
     * The logic for searching is pretty much identical for both components and pages, so even a
     * cursory test of component types should nail it.
     */
    @Test
    public void simple_component_type()
    {
        String className = APP_ROOT_PACKAGE + ".components.SimpleComponent";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_app_packages(source);

        train_locateComponentClassNames(locator, APP_ROOT_PACKAGE + ".components", className);

        replay();

        ComponentClassResolver resolver = create(source, locator);

        assertEquals(resolver.resolveComponentTypeToClassName("SimpleComponent"), className);

        verify();
    }

    /**
     * Likewise for mixins; it's all just setup for a particular method.
     */

    @Test
    public void simple_mixin_type()
    {
        String expectedClassName = APP_ROOT_PACKAGE + ".mixins.SimpleMixin";

        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_app_packages(source);

        train_locateComponentClassNames(locator, APP_ROOT_PACKAGE + ".mixins", expectedClassName);

        replay();

        ComponentClassResolver resolver = create(source, locator);

        assertEquals(resolver.resolveMixinTypeToClassName("SimpleMixin"), expectedClassName);

        verify();
    }

    @Test
    public void mixin_type_not_found()
    {
        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping(CORE_PREFIX,
                CORE_ROOT_PACKAGE));

        try
        {
            resolver.resolveMixinTypeToClassName("SimpleMixin");
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertTrue(ex.getMessage().contains(
                    "Unable to resolve mixin type 'SimpleMixin' to a component class name."));
        }

        verify();

    }

    @Test
    public void component_type_not_found()
    {
        ComponentInstantiatorSource source = newComponentInstantiatorSource();
        ComponentClassLocator locator = newComponentClassLocator();

        train_for_packages(source, CORE_ROOT_PACKAGE);
        train_for_app_packages(source);

        replay();

        ComponentClassResolver resolver = create(source, locator, new LibraryMapping(CORE_PREFIX,
                CORE_ROOT_PACKAGE));

        try
        {
            resolver.resolveComponentTypeToClassName("SimpleComponent");
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            assertTrue(ex
                    .getMessage()
                    .contains(
                            "Unable to resolve component type 'SimpleComponent' to a component class name."));
        }

        verify();

    }

    private void train_for_app_packages(ComponentInstantiatorSource source)
    {
        train_for_packages(source, APP_ROOT_PACKAGE);
    }
}
