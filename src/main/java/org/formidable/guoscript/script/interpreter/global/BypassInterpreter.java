package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;

public class BypassInterpreter extends Interpreter {
    public BypassInterpreter(){
        setName("bypass");
        setHead("en", "bypass");
        setHead("zh", "越权指令");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String command = scriptLine.substring(head.length() + 1, scriptLine.lastIndexOf(")->"));
        String target = scriptLine.substring(scriptLine.lastIndexOf(")->") + 3);
        if (target.equalsIgnoreCase("*")){
            for (Player player : Bukkit.getOnlinePlayers()){
                boolean isOP = player.isOp();
                try{
                    player.setOp(true);
                    Bukkit.dispatchCommand(player, command);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    player.setOp(isOP);
                }
            }
        }else {
            Player player = Bukkit.getPlayer(target);
            if (player != null && player.isOnline()){
                boolean isOP = player.isOp();
                try{
                    player.setOp(true);
                    Bukkit.dispatchCommand(player, command);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    player.setOp(isOP);
                }
            }
        }
        return new ExecuteResult();
    }
}
