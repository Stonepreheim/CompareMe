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
import java.util.Objects;

public class Metrics {
    private int lineCount = 0;
    private ArrayList<String> methodNames = new ArrayList<>();
    private ArrayList<String> variableNames = new ArrayList<>();

    private final String[] dataTypes = {"byte", "short", "int", "long", "float", "double", "boolean", "char", "String"};
    private final String[] memberTypes = {"private", "protected", "public", "package"};

    public void startMetrics(AnActionEvent event) {
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
                lineCount++;
                boolean isMethod = false, isVariable = false;

                if (line.contains("//")) {
                    continue;
                }
                else if (line.contains("{")) {
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
                }
                else {
                    for (String dataType : dataTypes) {
                        if (line.contains(dataType) && !line.contains("\"")) {
                            isVariable = true;
                            break;
                        }
                    }
                }

                if (isMethod) {
                    addMethodName(line);
                }
                else if (isVariable) {
                    addVariableName(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String title = String.format("Project: %s", projectName);
        String message = String.format("The number of lines in %s is %d%nNumber of methods: %d%n%s%n Number of" +
                        " variables: %d%n%s",
                projectName, lineCount, methodNames.size(), getMethodNames(), variableNames.size(), getVariableNames());
        Messages.showMessageDialog(project, message, title, Messages.getInformationIcon());

        resetMetrics();
    }

    private void addMethodName(String methodCall) {
        methodCall = methodCall.replace("{", "");
        methodNames.add(methodCall);
    }

    private void addVariableName(String variableCall) {
        variableCall = variableCall.replace(";", "");
        variableNames.add(variableCall);
    }

    private String getMethodNames() {
        StringBuilder methodName = new StringBuilder();

        for (String name : methodNames) {
            methodName.append(name).append("\n");
        }

        return methodName.toString();
    }

    private String getVariableNames() {
        StringBuilder variableName = new StringBuilder();

        for (int index = 1; index < variableNames.size(); index++) {
            variableName.append(variableNames.get(index)).append("\n");
        }

        return variableName.toString();
    }

    private void resetMetrics() {
        lineCount = 0;
        methodNames = new ArrayList<>();
        variableNames = new ArrayList<>();
    }
}
