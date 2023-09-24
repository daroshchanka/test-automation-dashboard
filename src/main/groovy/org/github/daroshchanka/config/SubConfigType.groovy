package org.github.daroshchanka.config

enum SubConfigType {

  PROVIDER,
  STRATEGY

  String getKey() {
    name().toLowerCase()
  }

}