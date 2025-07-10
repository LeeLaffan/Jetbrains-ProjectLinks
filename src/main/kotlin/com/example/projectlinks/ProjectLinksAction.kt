package com.example.projectlinks

import com.example.projectlinks.settings.ProjectLinksSettingsState
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

// TODO
// - Allow setting format
// - Allow setting scan all md files + depth

class ProjectLinksAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        // Check settings state
        val settings = ProjectLinksSettingsState.getInstance()

        val projectBasePath = event.project?.basePath ?: return
        val readmeFile = Paths.get(projectBasePath, "README.md")

        if (!Files.exists(readmeFile)) {
            Messages.showMessageDialog("README.md not found at project root", "Uh oh", Messages.getErrorIcon())
            return
        }
        
        val links = getLinks(Files.readAllLines(readmeFile))
        
        if (links.isEmpty()) {
            Messages.showMessageDialog("No links found in README.md", "Info", Messages.getInformationIcon())
            return
        }

        // Create display items that include both title and URL for searching
        val displayItems = links.map { "${it.first} → ${it.second}" }
        val displayMap = displayItems.zip(links).toMap()

        JBPopupFactory.getInstance()
            .createPopupChooserBuilder(displayItems)
            .setTitle("Project Links")
            .setItemChosenCallback { sel ->
                val (_, url) = displayMap[sel] ?: return@setItemChosenCallback
                BrowserUtil.browse(url)
            }
            .setRenderer(object : ColoredListCellRenderer<String>() {
                override fun customizeCellRenderer(
                    list: JList<out String>,
                    value: String,
                    index: Int,
                    selected: Boolean,
                    hasFocus: Boolean
                ) {
                    val link = displayMap[value]
                    if (link != null) {
                        font = list.font.deriveFont(settings.linkFontSize)
                        icon = AllIcons.Ide.External_link_arrow
                        append(link.first, SimpleTextAttributes.REGULAR_ATTRIBUTES)
                        append(" | ", SimpleTextAttributes.GRAYED_ATTRIBUTES)
                        append(link.second, SimpleTextAttributes.GRAYED_ATTRIBUTES)
                    } else {
                        append(value, SimpleTextAttributes.REGULAR_ATTRIBUTES)
                    }
                }
            })
            .setNamerForFiltering { item ->
                val link = displayMap[item]
                if (link != null) {
                    "${link.first} ${link.second}"
                } else {
                    item
                }
            }
            .createPopup()
            .showInFocusCenter()
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