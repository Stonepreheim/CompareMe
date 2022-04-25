package com.github.stonepreheim.compareme.services;

import java.util.ArrayList;

public class CalculateFile {
    private int lineCount = 0;
    private ArrayList<String> methodNames = new ArrayList<>();
    private ArrayList<String> variableNames = new ArrayList<>();
    private final String[] dataTypes = {"byte", "short", "int", "long", "float", "double", "boolean", "char", "String"};
    private final String[] memberTypes = {"private", "protected", "public", "package"};

    public void calculateMetrics(String line) {
        lineCount++;
        boolean isMethod = false, isVariable = false;

        if (!line.contains("//")) {
            if (line.contains("{")) {
                if (line.contains("(")) {
                    if (!line.contains("class")) {
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
        }

        if (isMethod) {
            addMethodName(line);
        }
        else if (isVariable) {
            addVariableName(line);
        }
    }

    private void addMethodName(String methodCall) {
        methodCall = methodCall.replace("{", "");
        methodNames.add(methodCall);
    }

    private void addVariableName(String variableCall) {
        variableCall = variableCall.replace(";", "");
        variableNames.add(variableCall);
    }

    public int getFileCount() {
        return lineCount;
    }

    public int getMethodCount() {
        return methodNames.size();
    }

    public int getVariableCount() {
        return variableNames.size();
    }

    public String getMethodNames() {
        StringBuilder methodName = new StringBuilder();

        for (String name : methodNames) {
            methodName.append(name).append("\n");
        }

        return methodName.toString();
    }

    public String getVariableNames() {
        StringBuilder variableName = new StringBuilder();

        for (int index = 1; index < variableNames.size(); index++) {
            variableName.append(variableNames.get(index)).append("\n");
        }

        return variableName.toString();
    }

    public void resetMetrics() {
        lineCount = 0;
        methodNames = new ArrayList<>();
        variableNames = new ArrayList<>();
    }
}
