package lee.jetbrains.projectlinks

import lee.jetbrains.projectlinks.settings.ProjectLinksSettingsState
import com.intellij.icons.AllIcons
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.JBPopupFactory
import java.nio.file.Files
import java.nio.file.Paths
import com.intellij.ui.ColoredListCellRenderer
import com.intellij.ui.SimpleTextAttributes
import javax.swing.JList

class ProjectLinksAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        // Check settings state
        val settings = ProjectLinksSettingsState.getInstance()

        val projectBasePath = event.project?.basePath ?: return
        val readmeFile = Paths.get(projectBasePath, "README.md")

        if (!Files.exists(readmeFile)) {
            Messages.showMessageDialog("README.md not found at project root: $readmeFile", "Whoops", Messages.getWarningIcon())
            return
        }

        val links = getLinks(Files.readAllLines(readmeFile))
        if (links.isEmpty()) {
            Messages.showMessageDialog("No links found in: $readmeFile", "Uh oh", Messages.getWarningIcon())
            return
        }

        JBPopupFactory.getInstance()
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
            .showInFocusCenter()
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

    fun getLinks(readmeLines: List<String>): List<Pair<String, String>> {
        val links = mutableListOf<Pair<String, String>>()

        for (line in readmeLines) {
            val result: Pair<String, String>? = getLinkFromLine(line)
            if (result != null) {
                links.add(result)
            }
        }

        return links.toList()
    }

    fun getLinkFromLine(line: String): Pair<String, String>? {
        val openBracket = line.indexOf('[')
        val closeBracket = line.indexOf(']', openBracket + 1)
        if (openBracket == -1 || closeBracket == -1 || closeBracket + 1 >= line.length || line[closeBracket + 1] != '(')
            return null

        val text = line.substring(openBracket + 1, closeBracket).trim()

        val openParen = closeBracket + 1
        val startUrl = openParen + 1
        var depth = 1
        var endUrl = -1

        for (i in startUrl until line.length) {
            when (line[i]) {
                '(' -> depth++
                ')' -> {
                    depth--
                    if (depth == 0) {
                        endUrl = i
                        break
                    }
                }
            }
        }

        if (endUrl == -1) return null

        val url = line.substring(startUrl, endUrl).trim()
        return text to url
    }
}