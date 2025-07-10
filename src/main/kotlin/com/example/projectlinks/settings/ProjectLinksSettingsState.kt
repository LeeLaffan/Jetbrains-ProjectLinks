package com.example.projectlinks.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Persistent state component that stores ProjectLinks settings.
 */
@State(
    name = "com.example.projectlinks.settings.ProjectLinksSettingsState",
    storages = [Storage("ProjectLinksSettings.xml")]
)
class ProjectLinksSettingsState : PersistentStateComponent<ProjectLinksSettingsState> {
    // Add your settings properties here
    var enableTestOption: Boolean = false
    var linkFontSize: Float = 12.0f

    override fun getState(): ProjectLinksSettingsState = this

    override fun loadState(state: ProjectLinksSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): ProjectLinksSettingsState {
            return ApplicationManager.getApplication().getService(ProjectLinksSettingsState::class.java)
        }
    }
}
