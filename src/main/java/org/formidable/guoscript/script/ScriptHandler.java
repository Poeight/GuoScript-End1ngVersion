package org.formidable.guoscript.script;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScriptHandler {

    protected JavaPlugin plugin;
    @Getter
    private boolean enablePAPI = false;
    @Getter
    @Setter
    private String scriptLang;

    // 注册的插件名 -> 注册的脚本行解释器
    private HashMap<String, List<Interpreter>> interpreterMap = new HashMap<>();
    // 运行中的脚本的ID -> 运行中的脚本对象
    protected HashMap<Integer, RunningScript> runnableScriptMap = new HashMap<>();

    public ScriptHandler(JavaPlugin plugin, String scriptLang) {
        this.plugin = plugin;
        Plugin placeholderAPI = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderAPI != null && placeholderAPI.isEnabled()) {
            this.enablePAPI = true;
        }
        this.scriptLang = scriptLang;
    }

    public RunningScript runScript(ArrayList<String> scriptList) {
        RunnableScript runnableScript = new RunnableScript(scriptList, this);
        return runScript(runnableScript);
    }

    public RunningScript runScript(RunnableScript runnableScript){
        RunningScript runningScript = runnableScript.run();
        runnableScriptMap.put(runningScript.getRunningScriptID(), runningScript);
        return runningScript;
    }

    public RunningScript getRunningScript(int runningScriptID) {
        if (runnableScriptMap.containsKey(runningScriptID)) {
            return runnableScriptMap.get(runningScriptID);
        }
        return null;
    }

    public boolean cancelRunningScript(int runningScriptID) {
        RunningScript runningScript = getRunningScript(runningScriptID);
        if (runningScript != null) {
            return runningScript.cancel();
        }
        return false;
    }

    ExecuteResult interpretScriptLine(String scriptLine, int runningScriptID) {
        ExecuteResult executeResult = new ExecuteResult(false);
        boolean end = false;
        for (List<Interpreter> interpreters : interpreterMap.values()) {
            for (Interpreter interpreter : interpreters) {
                if (scriptLang.equalsIgnoreCase("*")){
                    for (String name : interpreter.getHeads()){
                        if (scriptLine.toLowerCase().startsWith(name.toLowerCase())) {
                            try {
                                executeResult = interpreter.execute(name, scriptLine, runningScriptID);
                            } catch (Exception | Error e) {
                                e.printStackTrace();
                            }
                            end = true;
                            break;
                        }
                    }
                }else {
                    if (scriptLine.toLowerCase().startsWith(interpreter.getHead(scriptLang).toLowerCase())) {
                        try {
                            executeResult = interpreter.execute(interpreter.getHead(scriptLang), scriptLine, runningScriptID);
                        } catch (Exception | Error e) {
                            e.printStackTrace();
                        }
                        end = true;
                        break;
                    }
                }
                if (end) break;
            }
            if (end) break;
        }
        if (!executeResult.isSuccessful()) {
           log("存在运行出错的语句: " + scriptLine);
        }
        return executeResult;
    }

    public void registerInterpreter(Plugin plugin, Interpreter interpreter) {
        List<Interpreter> list = interpreterMap.get(plugin.getName());
        if (list == null) {
            list = new ArrayList<>();
            interpreterMap.put(plugin.getName(), list);
        }
        for (Interpreter i : list) {
            if (i.getName().equalsIgnoreCase(interpreter.getName())){
                return;
            }
        }
        interpreter.setHandler(this);
        list.add(interpreter);
    }

    public void unregisterAllInterpreter(Plugin plugin) {
        if (interpreterMap.containsKey(plugin.getName())) {
            interpreterMap.remove(plugin.getName());
        }
    }

    public void unregisterAllScriptLine() {
        interpreterMap.clear();
    }

    private int lastRunningScriptID = 0;
    int nextRunningScriptID() {
        return ++lastRunningScriptID;
    }

    public void log(String msg){
        plugin.getLogger().warning(msg);
    }
}
