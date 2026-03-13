package com.schedula.schedula.core;

import java.util.Map;

public record ErrorResponseCustom(int status,String message,long timestamp,Map<String, String> errors ) {

}
