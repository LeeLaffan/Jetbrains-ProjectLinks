package lee.jetbrains.projectlinks.settings

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
    // Default values from ProjectLinksSettingsComponent
    var linkFontSize: Float = ProjectLinksSettingsComponent.DEF_FONT_SIZE.toFloat()
    var titleMaxLength: Int = ProjectLinksSettingsComponent.DEF_TITLE_MAX_LENGTH
    var urlMaxLength: Int = ProjectLinksSettingsComponent.DEF_URL_MAX_LENGTH

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
