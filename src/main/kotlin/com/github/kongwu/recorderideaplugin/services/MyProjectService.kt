package com.github.kongwu.recorderideaplugin.services

import com.github.kongwu.recorderideaplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
