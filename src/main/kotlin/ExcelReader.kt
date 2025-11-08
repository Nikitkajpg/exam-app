import java.awt.FileDialog

fun openFileDialog(window: java.awt.Window?, title: String, allowedExtension: String): String? {
    val dialog = FileDialog(
        window as? java.awt.Frame, title, FileDialog.LOAD
    ).apply {
        setFilenameFilter { _, name -> name.endsWith(allowedExtension) }
        isVisible = true
    }
    return if (dialog.file != null) dialog.directory + dialog.file else null
}