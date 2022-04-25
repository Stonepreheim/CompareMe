package com.github.stonepreheim.compareme.extensions;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.wm.ToolWindow;
import org.jdesktop.swingx.action.ActionManager;

import javax.swing.*;
import java.io.File;

public class FileMetricWindow {
    private JPanel FileMetricWindowContent;
    private JButton selectFileButton;
    private JLabel FileSelectedLabel;
    private final ToolWindow theToolWindowThisFileMetricWindowIsBoundTo;

    private JFileChooser fileChooser;

    public FileMetricWindow( ToolWindow aToolWindowThatIsReferencedFromTheFileMetricWindowFactory ) {

        theToolWindowThisFileMetricWindowIsBoundTo = aToolWindowThatIsReferencedFromTheFileMetricWindowFactory;
//        theToolWindowThisFileMetricWindowIsBoundTo.hide();

        selectFileButton.addActionListener( e -> showFileSelector() );

    }

    void showFileSelector() {

//        fileChooser = new JFileChooser( System.getProperty("user.dir") );
        fileChooser = new JFileChooser( );
        int fileSelectedResult = fileChooser.showOpenDialog( FileMetricWindowContent );

        if( fileSelectedResult == JFileChooser.APPROVE_OPTION ) {

            File file = fileChooser.getSelectedFile();

            FileSelectedLabel.setText("Selected file name: " + file.getName());

            Action myAction = ActionManager.getInstance().getAction(IdeActions.ACTION_COMPARE_CLIPBOARD_WITH_SELECTION);

        }

    }

    public JPanel getContent() { return FileMetricWindowContent; }

}
