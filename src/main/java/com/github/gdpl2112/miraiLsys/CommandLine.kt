package com.github.gdpl2112.miraiLsys

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand


/**
 * 命令
 * @version 1.0
 * @author github kloping
 * @date 2022/1/3-10:24
 */
object CommandLine : CompositeCommand(
    AdminOwner, "LsysAdminOwner", "AdminOwner",
    description = "kloping的Lsys AdminOwner插件指令"
) {

    @Description("重载配置")
    @SubCommand("reload")
    suspend fun CommandSender.AdminOwnerReload() {
        try {
            JoinPoint.before()
            sendMessage("OK")
        } catch (e: ClassNotFoundException) {
            e.message?.let { sendMessage(it) }
        }
    }
}