package lee.jetbrains.projectlinks

import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import lee.jetbrains.projectlinks.settings.ProjectLinksSettingsState
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.JList

class ProjectLinksAction : AnAction() {
    internal val linkFinder = LinkFinder()

    override fun actionPerformed(event: AnActionEvent) {
        val settings = ProjectLinksSettingsState.getInstance()

        val projectBasePath = event.project?.basePath ?: return
        val readmeFile = Paths.get(projectBasePath, "README.md")

        if (!Files.exists(readmeFile)) {
            Messages.showMessageDialog("README.md not found at project root: $readmeFile", "Whoops", Messages.getWarningIcon())
            return
        }

        val links = linkFinder.getLinks(Files.readAllLines(readmeFile))
        if (links.isEmpty()) {
            Messages.showMessageDialog("No links found in: $readmeFile", "Uh oh", Messages.getWarningIcon())
            return
        }

        val popup = JBPopupFactory.getInstance()
            .createPopupChooserBuilder(links)
            .setTitle("Project Links")
            .setItemChosenCallback { sel ->
                val (_, url) = sel
                BrowserUtil.browse(url)
            }
            .setRenderer(object : ColoredListCellRenderer<Pair<String, String>>() {
                override fun customizeCellRenderer(
                    list: JList<out Pair<String, String>>,
                    value: Pair<String, String>,
                    index: Int,
                    selected: Boolean,
                    hasFocus: Boolean
                ) {
                    font = list.font.deriveFont(settings.linkFontSize)
                    icon = AllIcons.Ide.External_link_arrow
                    getRenderedLink(value)
                }
            })
            .setNamerForFiltering { item -> // Allow filtering by both parts of the link
                "${item.first} ${item.second}"
            }
            .createPopup()

        if (event.project != null) // Ideal opening position
            popup.showCenteredInCurrentWindow(event.project!!)
        else // Fallback
            popup.showInFocusCenter()
    }

    private fun ColoredListCellRenderer<Pair<String, String>>.getRenderedLink(value: Pair<String, String>) {
        val settings = ProjectLinksSettingsState.getInstance()
        val titleMaxLength = settings.titleMaxLength
        val urlMaxLength = settings.urlMaxLength
        val clampedFirst = if (value.first.length > titleMaxLength) "${value.first.take(titleMaxLength - 3)}..." else value.first
        val clampedSecond = if (value.second.length > urlMaxLength) "${value.second.take(urlMaxLength - 3)}..." else value.second

        append(clampedFirst, SimpleTextAttributes.REGULAR_ATTRIBUTES)
        append(" | ", SimpleTextAttributes.GRAYED_ATTRIBUTES)
        append(clampedSecond, SimpleTextAttributes.GRAYED_ATTRIBUTES)
    }
}
