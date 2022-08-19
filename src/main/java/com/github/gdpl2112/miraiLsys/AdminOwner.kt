package com.github.gdpl2112.miraiLsys

import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

/**
 * @author github-kloping
 * @version 1.0
 * @Date 2022/1/2-09
 */
object AdminOwner :
    KotlinPlugin(
        JvmPluginDescriptionBuilder("com.github.gdpl2112.lsys.AdminOwner", 1.4.toString())
            .author("github-kloping")
            .info("lsys群管插件")
            .dependsOn("cn.kloping.Lsys", "1.6", true)
            .build()
    ) {

    override fun PluginComponentStorage.onLoad() {
        starter()
    }

    private fun starter() {
        JoinPoint.run()
    }
}
