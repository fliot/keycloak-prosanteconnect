/*
 * (c) Copyright 1998-2023, ANS. All rights reserved.
 */
package fr.ans.keycloak.providers.prosanteconnect;

public enum EidasLevel {
  EIDAS1;

  public static final String EIDAS_LEVEL_PROPERTY_NAME = "eidas_values";

  @Override
  public String toString() {
    return name().toLowerCase();
  }

  public static EidasLevel getOrDefault(String eidasLevelName, EidasLevel defaultEidasLevel) {
    for (var eidasLevel : EidasLevel.values()) {
      if (eidasLevel.name().equalsIgnoreCase(eidasLevelName)) {
        return eidasLevel;
      }
    }
    
    if (eidasLevelName == null) {
    	return defaultEidasLevel;
    }
    
    return null;
  }
}
