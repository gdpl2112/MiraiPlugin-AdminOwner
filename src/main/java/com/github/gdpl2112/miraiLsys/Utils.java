package com.github.gdpl2112.miraiLsys;

import net.mamoe.mirai.console.plugin.Plugin;
import net.mamoe.mirai.console.plugin.PluginManager;
import net.mamoe.mirai.console.plugin.description.PluginDescription;
import net.mamoe.mirai.console.plugin.loader.PluginLoader;
import net.mamoe.mirai.contact.MemberPermission;

/**
 * @author github-kloping
 */
public class Utils {
    public static boolean isExits() {
        for (Plugin plugin : PluginManager.INSTANCE.getPlugins()) {
            PluginLoader<Plugin, PluginDescription> loader = (PluginLoader<Plugin, PluginDescription>) plugin.getLoader();
            String id = loader.getPluginDescription(plugin).getId();
            if ("cn.kloping.Lsys".equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static String getPermissionName(MemberPermission permission) {
        switch (permission) {
            case ADMINISTRATOR:
                return "管理员";
            case OWNER:
                return "群主";
            case MEMBER:
                return "群员";
        }
        return "未知身份";
    }

}
