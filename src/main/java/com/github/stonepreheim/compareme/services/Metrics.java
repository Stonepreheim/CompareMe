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
import java.util.ArrayList;

public class Metrics {
    private int lineCount = 0, methodCount = 0;
    private ArrayList<String> methodNames = new ArrayList<>();

    private final String[] dataTypes = {"byte", "short", "int", "long", "float", "double", "boolean", "char", "String"};
    private final String[] memberTypes = {"private", "protected", "public", "package"};

    public void startMetrics(AnActionEvent event) {
        Project project = event.getProject();
        String projectName = project.getName();

        Document currentDoc = FileEditorManager.getInstance(project).getSelectedTextEditor().getDocument();
        VirtualFile currentFile = FileDocumentManager.getInstance().getFile(currentDoc);
        String fileName = currentFile.getPath();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                lineCount++;
                boolean isMethod = false;

                if (line.contains("{")) {
                    if (line.contains("(")) {
                        if (line.contains("class")) {
                            continue;
                        }
                        else {
                            for (String memberType : memberTypes) {
                                if (line.contains(memberType)) {
                                    isMethod = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (isMethod) {
                        methodCount++;
                        addMethodName(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String title = String.format("Project: %s", projectName);
        String message = String.format("The number of lines in %s is %d%nNumber of methods: %d%n%s",
                projectName, lineCount, methodCount, getMethodNames());
        Messages.showMessageDialog(project, message, title, Messages.getInformationIcon());
    }

    private void addMethodName(String methodCall) {
        methodCall = methodCall.replace("{", "");
        methodNames.add(methodCall);
    }

    private String getMethodNames() {
        StringBuilder methodName = new StringBuilder();

        for (int index = 1; index < methodNames.size(); index++) {
            methodName.append(methodNames.get(index)).append("\n");
        }

        return methodName.toString();
    }
}
