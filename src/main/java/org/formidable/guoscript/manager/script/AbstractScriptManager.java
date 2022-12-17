package org.formidable.guoscript.manager.script;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.formidable.guoscript.GuoScript;
import org.formidable.guoscript.script.Interpreter;
import org.formidable.guoscript.script.ScriptHandler;
import org.formidable.guoscript.script.interpreter.global.*;
import org.formidable.guoscript.wrapper.ConfigWrapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public abstract class AbstractScriptManager {

    @Getter
    private ScriptHandler scriptHandler;
    protected Plugin plugin;
    protected Map<String, ArrayList<String>> scriptListMap = new LinkedHashMap<>();
    private String folderName;

    public AbstractScriptManager(JavaPlugin plugin, String folderName){
        this.plugin = plugin;
        this.folderName = folderName;
        scriptHandler = new ScriptHandler(plugin, plugin.getConfig().getString("settings.scriptLang"));
        reload();
    }

    public void reload(){
        GuoScript plugin = GuoScript.getInstance();

        scriptHandler.setScriptLang(plugin.getConfig().getString("settings.scriptLang"));

        if (plugin.getConfig().getBoolean("settings.createExample")){
            new ConfigWrapper(plugin, "script\\example.yml");
        }

        // 载入全局脚本解释器
        scriptHandler.unregisterAllInterpreter(plugin);

        // 运行控制
        scriptHandler.registerInterpreter(plugin, new GotoInterpreter());
        scriptHandler.registerInterpreter(plugin, new PauseInterpreter());
        scriptHandler.registerInterpreter(plugin, new StopInterpreter());

        // 变量
        scriptHandler.registerInterpreter(plugin, new SetVarInterpreter());
        scriptHandler.registerInterpreter(plugin, new LoadPAPIToVarInterpreter());
        scriptHandler.registerInterpreter(plugin, new LoadDataToVarInterpreter());

        // 数据
        scriptHandler.registerInterpreter(plugin, new SetDataInterpreter());

        // 条件判断
        scriptHandler.registerInterpreter(plugin, new IfInterpreter());
        scriptHandler.registerInterpreter(plugin, new ChanceInterpreter());
        scriptHandler.registerInterpreter(plugin, new HasItemInterpreter());
        scriptHandler.registerInterpreter(plugin, new HasMoneyInterpreter());
        scriptHandler.registerInterpreter(plugin, new HasPermInterpreter());
        scriptHandler.registerInterpreter(plugin, new RelationInterpreter());
        scriptHandler.registerInterpreter(plugin, new WeightedInterpreter());

        // 游戏中执行
        scriptHandler.registerInterpreter(plugin, new AddItemInterpreter());
        scriptHandler.registerInterpreter(plugin, new AddMoneyInterpreter());
        scriptHandler.registerInterpreter(plugin, new BypassInterpreter());
        scriptHandler.registerInterpreter(plugin, new CommandInterpreter());
        scriptHandler.registerInterpreter(plugin, new ConsoleInterpreter());
        scriptHandler.registerInterpreter(plugin, new MessageInterpreter());
        scriptHandler.registerInterpreter(plugin, new TitleInterpreter());

        loadFile(plugin.getDataFolder().getAbsolutePath());
        loadFile(plugin.getDataFolder().getAbsolutePath() + "\\" + folderName);
    }

    private void loadFile(String path){
        // 载入脚本
        File main = new File( path + "\\" + "script");
        File[] files = main.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                if (!file.getName().endsWith(".yml")){
                    continue;
                }
                ConfigWrapper configWrapper = new ConfigWrapper(file);
                for (String key : configWrapper.getConfig().getKeys(false)){
                    scriptListMap.put(key, new ArrayList<>(configWrapper.getConfig().getStringList(key)));
                }
            }
        }

        // 动态加载解释器
        main = new File(path + "\\" + "interpreter");
        if (!main.exists()){
            main.mkdirs();
        }
        files = main.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    continue;
                }
                if (!file.getName().endsWith(".jar")){
                    return;
                }
                try {
                    URL url = file.toURI().toURL();
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, Interpreter.class.getClassLoader());
                    JarInputStream jarInputStream = new JarInputStream(url.openStream());
                    while (true) {
                        JarEntry j = jarInputStream.getNextJarEntry();
                        if (j == null) {
                            break;
                        }
                        String name = j.getName();
                        if (name == null || name.isEmpty()) {
                            continue;
                        }
                        if (name.endsWith(".class")) {
                            name = name.replace("/", ".");
                            String cname = name.substring(0, name.lastIndexOf(".class"));
                            Class<?> c = classLoader.loadClass(cname);
                            if (!Interpreter.class.isAssignableFrom(c)) {
                                continue;
                            }
                            if (Modifier.isAbstract(c.getModifiers())){
                                continue;
                            }
                            Interpreter interpreter = (Interpreter) c.newInstance();
                            scriptHandler.registerInterpreter(plugin, interpreter);
                        }
                    }
                    classLoader.close();
                    jarInputStream.close();
                } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<String> getScript(String name){
        return scriptListMap.get(name);
    }

    public void showScriptNames(CommandSender sender, int page){
        int maxPage = scriptListMap.size() / 8;
        page = page - 1;
        if (page > maxPage){
            page = maxPage;
        }
        int index = page * 8;
        int amount = 8;
        List<String> list = new ArrayList<>();
        for (String key : scriptListMap.keySet()){
            if (index > 0){
                index--;
            }else {
                list.add(key);
                amount--;
                if (amount == 0){
                    break;
                }
            }
        }
        page = page + 1;
        maxPage = maxPage + 1;
        sender.sendMessage("§7§l----====以下是载入的脚本§e(" + page + "/" + maxPage + ")§7§l====----");
        for (String key : list){
            sender.sendMessage("§7- " + key);
        }
        sender.sendMessage("§7§l----====以上是载入的脚本§e(" + page + "/" + maxPage + ")§7§l====----");
        for (int i = 0; i < 8 - list.size(); ++i){
            sender.sendMessage("");
        }
    }
}
