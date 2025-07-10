package lee.jetbrains.projectlinks.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

/**
 * Configurable for ProjectLinks settings.
 * Provides a UI for modifying settings in the IDE's Settings dialog.
 */
class ProjectLinksSettingsConfigurable : Configurable {
    private var settingsComponent: ProjectLinksSettingsComponent? = null

    override fun getDisplayName(): String = "Project Links"

    override fun getPreferredFocusedComponent(): JComponent? {
        return settingsComponent?.getPreferredFocusedComponent()
    }

    override fun createComponent(): JComponent {
        settingsComponent = ProjectLinksSettingsComponent()
        return settingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = ProjectLinksSettingsState.getInstance()
        val component = settingsComponent ?: return false
        return component.fontSizeValue != settings.linkFontSize ||
               component.titleMaxLengthValue != settings.titleMaxLength ||
               component.urlMaxLengthValue != settings.urlMaxLength
    }

    override fun apply() {
        val settings = ProjectLinksSettingsState.getInstance()
        settingsComponent?.let {
            settings.linkFontSize = it.fontSizeValue
            settings.titleMaxLength = it.titleMaxLengthValue
            settings.urlMaxLength = it.urlMaxLengthValue
        }
    }

    override fun reset() {
        val settings = ProjectLinksSettingsState.getInstance()
        settingsComponent?.let {
            it.fontSizeValue = settings.linkFontSize
            it.titleMaxLengthValue = settings.titleMaxLength
            it.urlMaxLengthValue = settings.urlMaxLength
        }
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}
