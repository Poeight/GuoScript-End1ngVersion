package org.formidable.guoscript.script;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

public class RunnableScript {

    @Getter
    private ScriptHandler handler;
    @Getter
    private ArrayList<String> originalScriptLineList;

    private HashMap<String, String> varMap = new HashMap<>();

    public RunnableScript(ArrayList<String> list, ScriptHandler handler) {
        this.originalScriptLineList = list;
        this.handler = handler;
    }

    RunningScript run() {
        int runningScriptID = handler.nextRunningScriptID();
        ScriptBukkitRunnable scriptBukkitRunnable = new ScriptBukkitRunnable(originalScriptLineList, handler, runningScriptID);
        RunningScript runningScript = new RunningScript(originalScriptLineList, handler, runningScriptID, scriptBukkitRunnable);
        for (String key : varMap.keySet()){
            runningScript.setVar(key, varMap.get(key));
        }
        runningScript.run();
        return runningScript;
    }

    public void setVar(String key, String value){
        varMap.put(key, value);
    }
}
