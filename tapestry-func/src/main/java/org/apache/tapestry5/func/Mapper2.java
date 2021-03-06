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

package org.apache.tapestry5.func;

/**
 * A generalization of {@link Mapper} for a two-input function.
 */
public abstract class Mapper2<A, B, C>
{
    /**
     * Take, as input, two values (from two flows) and return a computed value of
     * the third type.
     */
    public abstract C map(A first, B second);
}
