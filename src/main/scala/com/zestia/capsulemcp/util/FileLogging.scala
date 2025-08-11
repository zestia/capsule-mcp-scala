package com.zestia.capsulemcp.util

trait FileLogging:
  @transient
  protected lazy val logger: FileLogger = new FileLogger(
    this.getClass.getSimpleName.stripSuffix("$")
  )
