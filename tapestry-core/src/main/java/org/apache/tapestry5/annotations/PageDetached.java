// Copyright 2007, 2009, 2010 The Apache Software Foundation
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

package org.apache.tapestry5.annotations;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import static org.apache.tapestry5.ioc.annotations.AnnotationUseContext.*;
import org.apache.tapestry5.ioc.annotations.UseWith;

/**
 * Method annotation used for methods that should be invoked when the page is detached at the end of a request, before
 * it is returned to the page pool for later reuse.
 * <p/>
 * PageDetached methods should take no parameters and return void. They must either have this annotation, or be named
 * "pageDetached".
 * <p/>
 * To be clear: methods with this annotation (or name) are still invoked even in Tapestry 5.2, which does away with the
 * page pool.
 * 
 * @see PageAttached
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
@UseWith(
{ COMPONENT, MIXIN, PAGE })
public @interface PageDetached
{

}
