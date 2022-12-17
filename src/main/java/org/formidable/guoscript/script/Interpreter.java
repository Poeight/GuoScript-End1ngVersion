package org.formidable.guoscript.script;

import lombok.Getter;

import java.util.Collection;
import java.util.HashMap;

public abstract class Interpreter {

    @Getter
    private ScriptHandler handler;
    @Getter
    private String name;
    private HashMap<String, String> headMap = new HashMap<>();

    public Interpreter(){}

    void setHandler(ScriptHandler handler){
        this.handler = handler;
    }

    protected void setName(String name){
        this.name = name;
    }

    protected void setHead(String lang, String head){
        headMap.put(lang, head);
    }

    protected String getHead(String lang){
        return headMap.get(lang);
    }

    protected Collection<String> getHeads(){
        return headMap.values();
    }

    public abstract ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error;

}
