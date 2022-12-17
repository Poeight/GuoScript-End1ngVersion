package org.formidable.guoscript.script;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

class ScriptBukkitRunnable extends BukkitRunnable {

    public ScriptBukkitRunnable(ArrayList<String> originalScriptLineList, ScriptHandler handler, int runningScriptID) {
        this.runningScriptID = runningScriptID;
        this.runningScriptList = new ArrayList<>(originalScriptLineList);
        this.handler = handler;
    }

    @Getter
    private int runningScriptID;
    @Getter
    private ScriptHandler handler;
    @Getter
    private ArrayList<String> runningScriptList;
    @Getter
    @Setter
    private Integer pause = 0;
    @Getter
    @Setter
    private Integer scriptIndex = 0;
    private HashMap<String, Boolean> conditionMap = new HashMap<>();
    private HashMap<String, String> varMap = new HashMap<>();

    @Override
    public void run() {
        while (runningScriptList.size() > scriptIndex) {
            if (pause > 0) {
                pause--;
                return;
            }
            String scriptLine = runningScriptList.get(scriptIndex);
            scriptIndex++;
            for (String var : varMap.keySet()) {
                scriptLine = scriptLine.replace("%" + var + "%", varMap.get(var));
            }
            ExecuteResult executeResult = handler.interpretScriptLine(scriptLine, runningScriptID);

            while (executeResult.getNextAction() == NextAction.REPEAT) {
                executeResult = handler.interpretScriptLine(executeResult.getScriptLine(), runningScriptID);
            }
            if (executeResult.getNextAction() == NextAction.NEXT_LINE){
                continue;
            }
            if (executeResult.getNextAction() == NextAction.STOP){
                Bukkit.getScheduler().cancelTask(getTaskId());
                break;
            }
        }
        Bukkit.getScheduler().cancelTask(getTaskId());
        handler.runnableScriptMap.remove(runningScriptID);
    }

    void setCondition(String conditionName, boolean b) {
        conditionMap.put(conditionName, b);
    }

    boolean getCondition(String conditionName) {
        if (conditionMap.containsKey(conditionName)) {
            return conditionMap.get(conditionName);
        }
        return false;
    }

    void setVar(String varName, String value) {
        varMap.put(varName, value);
    }

    String getVar(String varName) {
        if (varMap.containsKey(varName)) {
            return varMap.get(varName);
        }
        return null;
    }
}
