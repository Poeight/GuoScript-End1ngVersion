package org.formidable.guoscript.script;

import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class RunningScript {

    private boolean cancel;
    @Getter
    private ArrayList<String> originalScriptLineList;
    @Getter
    private ScriptHandler handler;
    @Getter
    private int runningScriptID;

    private ScriptBukkitRunnable scriptBukkitRunnable;
    private BukkitTask bukkitTask;

    RunningScript(ArrayList<String> originalScriptLineList, ScriptHandler handler, int runningScriptID, ScriptBukkitRunnable scriptBukkitRunnable) {
        this.originalScriptLineList = originalScriptLineList;
        this.handler = handler;
        this.runningScriptID = runningScriptID;
        this.scriptBukkitRunnable = scriptBukkitRunnable;
    }

    protected void run(){
        this.bukkitTask = scriptBukkitRunnable.runTaskTimer(handler.plugin, 0, 1);
    }

    public boolean isCancel() {
        return this.cancel || bukkitTask.isCancelled();
    }

    public boolean cancel() {
        if (isCancel()) {
            return false;
        }
        if (bukkitTask.isCancelled()) {
            return false;
        }
        bukkitTask.cancel();
        this.cancel = true;
        return true;
    }

    public void setCondition(String conditionName, boolean b) {
        scriptBukkitRunnable.setCondition(conditionName, b);
    }

    public boolean getCondition(String conditionName) {
        return scriptBukkitRunnable.getCondition(conditionName);
    }

    public void setVar(String varName, String value) {
        scriptBukkitRunnable.setVar(varName, value);
    }

    public String getVar(String varName) {
        return scriptBukkitRunnable.getVar(varName);
    }

    public void addPause(int pause) {
        scriptBukkitRunnable.setPause(scriptBukkitRunnable.getPause() + pause);
        if (scriptBukkitRunnable.getPause() < 0) {
            scriptBukkitRunnable.setPause(0);
        }
    }

    public int getPause() {
        return scriptBukkitRunnable.getPause();
    }

    public Integer getScriptIndex(){
        return scriptBukkitRunnable.getScriptIndex();
    }

    public void setScriptIndex(int index){
        if (index > scriptBukkitRunnable.getRunningScriptList().size()){
            index = scriptBukkitRunnable.getRunningScriptList().size() - 1;
        }
        scriptBukkitRunnable.setScriptIndex(index);
    }
}
