package com.github.stonepreheim.compareme.services;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import java.io.BufferedReader;
import java.io.FileReader;

public class Metrics {
    private int lineCount = 0;

    public void startMetrics(AnActionEvent event) {
        Project project = event.getProject();
        String projectName = project.getName();

        Document currentDoc = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        String fileName = currentFile.getPath();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                countLines();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int lineCount = getLineCount();

        String title = String.format("Project: %s", projectName);
        String message = String.format("The number of lines in %s is %d", projectName, lineCount);
        Messages.showMessageDialog(project, message, title, Messages.getInformationIcon());
    }

    public void countLines() {
        lineCount++;
    }

    public int getLineCount() {
        return lineCount;
    }
}
