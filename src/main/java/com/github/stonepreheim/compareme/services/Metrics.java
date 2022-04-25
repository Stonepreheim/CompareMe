package com.github.stonepreheim.compareme.services;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Metrics {
    private final CalculateFile CF = new CalculateFile();
    private final CalculateClip CC = new CalculateClip();

    public void fileMetrics(AnActionEvent event) {
        Project project = event.getProject();
        assert project != null;
        String projectName = project.getName();

        Document currentDoc = Objects.requireNonNull(FileEditorManager.getInstance(project).getSelectedTextEditor()).getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        assert currentFile != null;
        String fileName = currentFile.getPath();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                CF.calculateMetrics(line);
            }
        }
        catch (Exception e) {
            System.out.println("Something went wrong");
        }

        String title = String.format("Project: %s", projectName);
        String message = String.format("The number of lines in %s is %d%nNumber of methods: %d%n%s%n Number of" +
                        " variables: %d%n%s",
                projectName, CF.getFileCount(), CF.getMethodCount(), CF.getMethodNames(),
                CF.getVariableCount(), CF.getVariableNames());
        Messages.showMessageDialog(project, message, title, Messages.getInformationIcon());

        CF.resetMetrics();

        clipMetrics(projectName);
    }

    private void clipMetrics(String projectName) {
        Transferable toolkit = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        try {
            if (toolkit != null && toolkit.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String) toolkit.getTransferData(DataFlavor.stringFlavor);
                String[] lines = text.split("\\R");

                for (String line : lines) {
                    CC.calculateMetrics(line);
                }

                String message = String.format("The number of lines in %s is %d%nNumber of methods: %d%n%s%n Number of" +
                                " variables: %d%n%s",
                        projectName, CC.getFileCount(), CC.getMethodCount(), CC.getMethodNames(),
                        CC.getVariableCount(), CC.getVariableNames());
                System.out.println(message);
            }
        }
        catch (UnsupportedFlavorException | IOException e) {
            System.out.println("Something went wrong");
        }

        CC.resetMetrics();
    }
}
