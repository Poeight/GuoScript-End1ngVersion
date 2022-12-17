package org.formidable.guoscript.manager.script;

import org.bukkit.plugin.java.JavaPlugin;
import org.formidable.guoscript.script.RunnableScript;

public class CommandScriptManager extends AbstractScriptManager{

    public CommandScriptManager(JavaPlugin plugin) {
        super(plugin, "command");
    }

    public void runScript(String name){
        getScriptHandler().runScript(getScript(name));
    }

    public void runScript(String name, String playerName){
        RunnableScript runnableScript = new RunnableScript(getScript(name), getScriptHandler());
        runnableScript.setVar("player", playerName);
        getScriptHandler().runScript(runnableScript);
    }
}
