package com.sherlockblue.kmpble.ble.extensions

import java.util.regex.Pattern

fun String.isValidUUID(): Boolean =
  (Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$").matcher(this).matches())
