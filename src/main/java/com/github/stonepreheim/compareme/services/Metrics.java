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
import java.util.ArrayList;
import java.util.Objects;

public class Metrics {
    private final CalculateFile CF = new CalculateFile();
    private final CalculateFile CC = new CalculateFile();

    private void fileMetrics(Project project) {
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
    }

    private void clipMetrics() {
        Transferable toolkit = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        try {
            if (toolkit != null && toolkit.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String) toolkit.getTransferData(DataFlavor.stringFlavor);
                String[] lines = text.split("\\R");

                for (String line : lines) {
                    CC.calculateMetrics(line);
                }
            }
        }
        catch (UnsupportedFlavorException | IOException e) {
            System.out.println("Something went wrong");
        }
    }

    public void showMetrics(AnActionEvent event) {
        Project project = event.getProject();
        assert project != null;
        //String projectName = project.getName();
        ArrayList<Integer> CFMetrics = new ArrayList<>();
        ArrayList<Integer> CCMetrics = new ArrayList<>();
        int dup = 0;

        fileMetrics(project);
        clipMetrics();

        CFMetrics.add(CF.getFileCount());
        CFMetrics.add(CF.getMethodCount());
        CFMetrics.add(CF.getVariableCount());

        CCMetrics.add(CC.getFileCount());
        CCMetrics.add(CC.getMethodCount());
        CCMetrics.add(CC.getVariableCount());

        for (int index = 0; index < CFMetrics.size(); index++) {
            if (CFMetrics.get(index).equals(CCMetrics.get(index))) {
                dup++;
            }
        }

        String title = String.format("Duplicate Percentage: %d%%", (int) ((double) dup / CFMetrics.size() * 100));
        String message = String.format("Number of: File Clip%nLines:     %d    %d%nMethods:   %d    %d%nVariables: %d    %d%n",
                CFMetrics.get(0), CCMetrics.get(0),
                CFMetrics.get(1), CCMetrics.get(1),
                CFMetrics.get(2), CCMetrics.get(2));
        Messages.showMessageDialog(project, message, title, Messages.getInformationIcon());

        CF.resetMetrics();
        CC.resetMetrics();
    }
}
