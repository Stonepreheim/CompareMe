package com.github.stonepreheim.compareme.extensions;

import com.intellij.openapi.wm.ToolWindow;
import javax.swing.*;

public class FileMetricWindow {
    private JPanel FileMetricWindowContent;
    private final ToolWindow theToolWindowThisFileMetricWindowIsBoundTo;

    public FileMetricWindow( ToolWindow aToolWindowThatIsReferencedFromTheFileMetricWindowFactory ) {

        theToolWindowThisFileMetricWindowIsBoundTo = aToolWindowThatIsReferencedFromTheFileMetricWindowFactory;

    }

    public JPanel getContent() { return FileMetricWindowContent; }

}
