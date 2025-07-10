package com.example.projectlinks.settings

import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

/**
 * Component for ProjectLinks settings UI.
 */
class ProjectLinksSettingsComponent {
    private val testOptionCheckbox = JBCheckBox("Enable test option")
    private val fontSizeSpinner = JSpinner(SpinnerNumberModel(16.0, 2.0, 50.0, 1.0))
    private val mainPanel: JPanel = FormBuilder.createFormBuilder()
        .addComponent(testOptionCheckbox, 1)
        .addLabeledComponent(JBLabel("Link font size:"), fontSizeSpinner, 1)
        .addComponentFillVertically(JPanel(), 0)
        .panel

    fun getPanel(): JPanel = mainPanel

    fun getPreferredFocusedComponent(): JComponent = testOptionCheckbox

    var testOptionEnabled: Boolean
        get() = testOptionCheckbox.isSelected
        set(value) {
            testOptionCheckbox.isSelected = value
        }

            var fontSizeValue: Float
        get() = (fontSizeSpinner.value as Double).toFloat()
        set(value) {
            fontSizeSpinner.value = value.toDouble()
        }
}
