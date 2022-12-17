package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;

public class ConsoleInterpreter extends Interpreter {
    public ConsoleInterpreter(){
        setName("console");
        setHead("en", "console");
        setHead("zh", "后台指令");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String command = scriptLine.substring(head.length() + 1, scriptLine.lastIndexOf(")"));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        return new ExecuteResult();
    }
}
