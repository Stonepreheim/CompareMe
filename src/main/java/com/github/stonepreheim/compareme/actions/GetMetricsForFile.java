package com.github.stonepreheim.compareme.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.pom.Navigatable;
import com.github.stonepreheim.compareme.extensions.FileMetricWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GetMetricsForFile extends AnAction{

    private boolean isShowing = false;

    //opens and closes file metrics window
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project currentProject = e.getProject();
        ToolWindow window = ToolWindowManager.getInstance(currentProject).getToolWindow("MetricsWindow");
        if (isShowing){
            window.hide();
            isShowing = false;
        }else {
            window.show();
            isShowing = true;
        }
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }
}
