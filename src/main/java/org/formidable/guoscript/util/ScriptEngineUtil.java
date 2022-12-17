package org.formidable.guoscript.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptEngineUtil {

    public static final ScriptEngine ENGINE = new ScriptEngineManager().getEngineByName("javascript");

}
