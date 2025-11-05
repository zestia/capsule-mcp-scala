/*
 * Copyright 2025 Zestia Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zestia.capsulemcp.server.tools.common

import com.zestia.capsulemcp.server.tools.common.FilterOperators.Operator.*

/**
 * Filter operators for use in search/filter schemas
 */
object FilterOperators:

  /**
   * Common filter operators
   */
  enum Operator(val value: String):
    case Is extends Operator("is")
    case IsNot extends Operator("is not")
    case Contains extends Operator("contains")
    case StartsWith extends Operator("starts with")
    case EndsWith extends Operator("ends with")
    case IsGreaterThan extends Operator("is greater than")
    case IsLessThan extends Operator("is less than")
    case IsAfter extends Operator("is after")
    case IsBefore extends Operator("is before")

  val numberOperators = List(Is, IsNot, IsGreaterThan, IsLessThan)
  val stringOperators = List(Is, IsNot, StartsWith, EndsWith, Contains)
  val dateOperators = List(Is, IsNot, IsAfter, IsBefore)
  val booleanOperators = List(Is, IsNot)
  
  /**
   * Helper to convert operators to their string values
   */
  extension (operators: List[Operator])
    def toStrings: List[String] = operators.map(_.value)
