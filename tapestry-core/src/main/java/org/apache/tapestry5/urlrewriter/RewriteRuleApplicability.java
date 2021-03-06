// Copyright 2009, 2010 The Apache Software Foundation
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

package org.apache.tapestry5.urlrewriter;

import org.apache.tapestry5.services.linktransform.LinkTransformer;

/**
 * Defines the situations to which a URLRewriterRule will be applied (inbound, outbound, or both)
 * 
 * @deprecated Use {@link LinkTransformer} instead
 */
public enum RewriteRuleApplicability
{
    /** contributed rule applies to inbound requests only */
    INBOUND,
    /** contributed rule applies to "outbound" requests (link rewriting). */
    OUTBOUND,
    /** contributed rule applies to both inbound and outbound requests */
    BOTH
}
