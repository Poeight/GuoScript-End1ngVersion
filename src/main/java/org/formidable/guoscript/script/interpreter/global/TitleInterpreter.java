package org.formidable.guoscript.script.interpreter.global;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.formidable.guoscript.script.ExecuteResult;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.util.FormatUtil;
import org.formidable.guoscript.wrapper.TitleWrapper;

public class TitleInterpreter extends Interpreter {
    public TitleInterpreter(){
        setName("title");
        setHead("en", "title");
        setHead("zh", "发送标题");
    }
    @Override
    public ExecuteResult execute(String head, String scriptLine, int runningScriptID) throws Exception, Error {
        String[] tt = scriptLine.substring(head.length() + 1, scriptLine.lastIndexOf(")->")).replace("&", "§").split(":");
        String title = FormatUtil.roundAll(tt[0]);
        String subTitle = FormatUtil.roundAll(tt[1]);
        Integer fadeIn = Integer.valueOf(tt[2]);
        Integer stay = Integer.valueOf(tt[3]);
        Integer fadeOut = Integer.valueOf(tt[4]);
        TitleWrapper titleWrapper = new TitleWrapper(title, subTitle, fadeIn, stay, fadeOut);
        String target = scriptLine.substring(scriptLine.lastIndexOf(")->") + 3);
        if (target.equalsIgnoreCase("*")){
            for (Player player : Bukkit.getOnlinePlayers()){
                titleWrapper.send(player);
            }
        }else {
            Player player = Bukkit.getPlayer(target);
            if (player != null && player.isOnline()){
                titleWrapper.send(player);
            }
        }
        return new ExecuteResult();
    }
}
