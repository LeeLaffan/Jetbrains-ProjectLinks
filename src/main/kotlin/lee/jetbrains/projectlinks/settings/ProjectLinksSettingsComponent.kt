package lee.jetbrains.projectlinks.settings

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
    companion object {
        const val DEF_FONT_SIZE = 16.0
        const val DEF_TITLE_MAX_LENGTH = 100
        const val DEF_URL_MAX_LENGTH = 50
    }

    private val fontSizeSpinner = JSpinner(SpinnerNumberModel(DEF_FONT_SIZE, 2.0, 50.0, 1.0))
    private val titleMaxLengthSpinner = JSpinner(SpinnerNumberModel(DEF_TITLE_MAX_LENGTH, 10, 200, 5))
    private val urlMaxLengthSpinner = JSpinner(SpinnerNumberModel(DEF_URL_MAX_LENGTH, 10, 200, 5))
    private val mainPanel: JPanel = FormBuilder.createFormBuilder()
        .addLabeledComponent(JBLabel("Font size:"), fontSizeSpinner, 1)
        .addLabeledComponent(JBLabel("Title max length:"), titleMaxLengthSpinner, 1)
        .addLabeledComponent(JBLabel("URL max length:"), urlMaxLengthSpinner, 1)
        .addComponentFillVertically(JPanel(), 0)
        .panel

    fun getPanel(): JPanel = mainPanel

    fun getPreferredFocusedComponent(): JComponent = fontSizeSpinner

    var fontSizeValue: Float
        get() = (fontSizeSpinner.value as Double).toFloat()
        set(value) {
            fontSizeSpinner.value = value.toDouble()
        }

    var titleMaxLengthValue: Int
        get() = titleMaxLengthSpinner.value as Int
        set(value) {
            titleMaxLengthSpinner.value = value
        }

    var urlMaxLengthValue: Int
        get() = urlMaxLengthSpinner.value as Int
        set(value) {
            urlMaxLengthSpinner.value = value
        }
}
