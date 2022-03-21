package com.github.stonepreheim.compareme.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GetMetricsForFile extends AnAction{

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("i was pressed");
    }
}
