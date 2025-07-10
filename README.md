# JetBrains Plugin: Project Links

This is a plugin for JetBrains editors that will scan the README.md file at the root of the current project, finding links and displaying
them in a tool window.

Selecting an entry will open the link in your default browser.

## Demo

Some test links:

- [Google](https://www.google.com)
- [Wikipedia Function](https://en.wikipedia.org/wiki/Function_(mathematics))
- [JetBrains](https://www.jetbrains.com)
- [Rider IDE](https://www.jetbrains.com/rider/)
- [Markdown Guide](https://www.markdownguide.org/basic-syntax/)

And here is the tool window the plugin provides:

![image](resources/Demo.png)

## Settings

You can configure the plugin in the settings dialog. The following options are available:

| Setting          | Description                                    | Default   |
|------------------|------------------------------------------------|-----------|
| Link Font Size   | The font size of the links in the tool window. | 16  px    |
| Title Max Length | The title will be truncated to this length.    | 100 chars |
| Url Max Length   | The URL will be truncated to this length.      | 50  chars |
