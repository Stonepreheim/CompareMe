package com.github.stonepreheim.compareme.actions;

import com.github.stonepreheim.compareme.services.Metrics;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
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

    //opens file metrics window
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project currentProject = e.getProject();
        ToolWindow window = ToolWindowManager.getInstance(currentProject).getToolWindow("MetricsWindow");
        window.show();
        AnAction showCompare = ActionManager.getInstance().getAction(IdeActions.ACTION_COMPARE_CLIPBOARD_WITH_SELECTION);
        showCompare.actionPerformed(e);
        Metrics metricsService = ApplicationManager.getApplication().getService(Metrics.class);
        metricsService.showMetrics(e);
    }

    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project.isInitialized() == true);
    }
}
