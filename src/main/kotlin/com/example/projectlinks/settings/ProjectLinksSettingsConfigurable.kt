package com.example.projectlinks.settings

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
        return component.testOptionEnabled != settings.enableTestOption ||
               component.fontSizeValue != settings.linkFontSize
    }

    override fun apply() {
        val settings = ProjectLinksSettingsState.getInstance()
        settingsComponent?.let {
            settings.enableTestOption = it.testOptionEnabled
            settings.linkFontSize = it.fontSizeValue
        }
    }

    override fun reset() {
        val settings = ProjectLinksSettingsState.getInstance()
        settingsComponent?.let {
            it.testOptionEnabled = settings.enableTestOption
            it.fontSizeValue = settings.linkFontSize
        }
    }

    override fun disposeUIResources() {
        settingsComponent = null
    }
}
