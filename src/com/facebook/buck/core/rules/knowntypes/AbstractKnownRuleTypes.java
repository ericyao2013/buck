/*
 * Copyright 2018-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.facebook.buck.core.rules.knowntypes;

import com.facebook.buck.core.description.DescriptionCache;
import com.facebook.buck.core.exceptions.HumanReadableException;
import com.facebook.buck.core.rules.type.RuleType;
import com.facebook.buck.core.util.immutables.BuckStyleImmutable;
import com.google.common.collect.ImmutableMap;
import org.immutables.value.Value;

/** Provides access to rule types. */
@Value.Immutable(builder = false, copy = false)
@BuckStyleImmutable
public abstract class AbstractKnownRuleTypes {

  @Value.Parameter
  public abstract KnownBuildRuleTypes getKnownBuildRuleTypes();

  @Value.Lazy
  public ImmutableMap<String, RuleType> getTypesByName() {
    return getKnownBuildRuleTypes()
        .getDescriptions()
        .stream()
        .map(DescriptionCache::getRuleType)
        .collect(ImmutableMap.toImmutableMap(RuleType::getName, t -> t));
  }

  /**
   * @param name user-facing name of a rule, e.g. "java_library"
   * @return {@link RuleType} that corresponds to the provided name.
   */
  public RuleType getRuleType(String name) {
    RuleType type = getTypesByName().get(name);
    if (type == null) {
      throw new HumanReadableException("Unable to find rule type: %s", name);
    }
    return type;
  }
}
