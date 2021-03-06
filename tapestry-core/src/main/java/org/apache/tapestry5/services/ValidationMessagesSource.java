// Copyright 2006, 2008, 2010 The Apache Software Foundation
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

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.UsesOrderedConfiguration;
import org.apache.tapestry5.services.messages.ComponentMessagesSource;

import java.util.Locale;

/**
 * Source for validation messages, within a particular locale.
 * <p/>
 * The service's configuration are paths, within the classpath, to bundles to load as part of the validation messages.
 * <p>
 * Starting with Tapestry 5.2, the returned Messages reflects all the contributed paths, with the
 * {@linkplain ComponentMessagesSource#getApplicationCatalog(java.util.Locale) global application catalog} ordered to
 * <em>override</em> any of the messages supplied by a contributed resource. This service will be elminated entirely in
 * Tapestry 5.3.
 * 
 * @deprecated Contribute to the {@link ComponentMessagesSource} service instead
 */
@UsesOrderedConfiguration(String.class)
public interface ValidationMessagesSource
{
    Messages getValidationMessages(Locale locale);
}
