package com.github.gdpl2112.miraiLsys

import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.PluginManager
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

/**
 * @author github-kloping
 * @version 1.0
 * @Date 2022/1/2-09
 */
object AdminOwner :
    KotlinPlugin(
        JvmPluginDescriptionBuilder("com.github.gdpl2112.lsys.AdminOwner", 1.0.toString())
            .author("github-kloping")
            .info("lsys群管插件")
            .build()
    ) {

    override fun PluginComponentStorage.onLoad() {
        if (!Utils.isExits()) {
            logger.error("欲使用该插件 必须安装 Lsys 插件")
            logger.error("欲使用该插件 必须安装 Lsys 插件")
            logger.error("欲使用该插件 必须安装 Lsys 插件")
            PluginManager.disablePlugin(AdminOwner)
            return
        }
        starter()
    }

    private fun starter() {
        JoinPoint.run()
    }
}
