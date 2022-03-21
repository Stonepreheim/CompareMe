package com.github.stonepreheim.compareme.extensions;

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class FileMetricWindow {
    private JTextField yippeKaYayTextField;
    private JTextField nyaaTextField;
    private JPanel FileMetricWindowContent;

    private final ToolWindow theToolWindowThisFileMetricWindowIsBoundTo;

    public FileMetricWindow( ToolWindow aToolWindowThatIsReferencedFromTheFileMetricWindowFactory ) {

        theToolWindowThisFileMetricWindowIsBoundTo = aToolWindowThatIsReferencedFromTheFileMetricWindowFactory;

    }

    public void showWindow( boolean shouldTheWindowThatHoldsThisFileMetricWindowBeShown ) {

        if( shouldTheWindowThatHoldsThisFileMetricWindowBeShown )
            theToolWindowThisFileMetricWindowIsBoundTo.show(null);
        else
            theToolWindowThisFileMetricWindowIsBoundTo.hide(null);

    }

    public JPanel getContent() { return FileMetricWindowContent; }

}
