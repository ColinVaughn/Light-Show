import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.7.1" // Check for new versions at https://plugins.gradle.org/plugin/io.papermc.paperweight.userdev
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "net.exenco.lightshow"
version = "1.2.4"
description = "Display a Light-Show in Minecraft."

dependencies {
    paperweight.paperDevBundle("1.21-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(21)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}
paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION



bukkit {
    main = "net.exenco.lightshow.LightShow"
    apiVersion = "1.19"
    author = "Exenco"
    commands {
        register("show") {
            description = "Controls the plugin."
            permission = "lightshow.show"
            usage = "/show"
        }
    }

    permissions {
        register("lightshow.*") {
            description = "Gives access to all lightshow comamnds."
            children = listOf("lightshow.show", "lightshow.check", "lightshow.reload", "lightshow.start", "lightshow.stop", "lightshow.toggle", "lightshow.warning")
        }
        register("lightshow.show") {
            description = "Allows to control the lightshow."
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("lightshow.check") {
            description = "Allows to check connectivity."
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("lightshow.reload") {
            description = "Allows to reload the plugin."
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("lightshow.start") {
            description = "Allows to start ArtNet connection."
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("lightshow.stop") {
            description = "Allows to stop ArtNet connection."
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("lightshow.toggle") {
            description = "Allows to toggle show visibility."
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
        register("lightshow.warning") {
            description = "Allows to view warning message."
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}
