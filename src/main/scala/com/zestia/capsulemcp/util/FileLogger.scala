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

package com.zestia.capsulemcp.util

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths, StandardOpenOption}
import java.time.Instant

class FileLogger(className: String):

  import FileLogger.*

  def info(msg: String): Unit = log("INFO", msg)

  def warn(msg: String): Unit = log("WARN", msg)

  def error(msg: String): Unit = log("ERROR", msg)

  private def log(level: String, msg: String): Unit =
    val timestamp = Instant.now().toString // ISO-8601 UTC timestamp
    val line = s"$timestamp $level [$className] $msg\n"
    Files.write(logPath, line.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND)

object FileLogger:
  private val logPath = Paths.get("capsule-mcp.log")

  // Initialise log file on startup (create or truncate)
  def init(): Unit =
    Files.write(logPath, Array.emptyByteArray, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)

  def apply(name: String): FileLogger = new FileLogger(name)
