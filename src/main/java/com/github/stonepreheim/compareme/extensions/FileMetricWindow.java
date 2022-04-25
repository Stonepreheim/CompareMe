package com.github.stonepreheim.compareme.extensions;

import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.wm.ToolWindow;
import org.jdesktop.swingx.action.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

            Path filePath = Path.of(file.getPath());

            try{
                StringSelection selection = new StringSelection(Files.readString(filePath));
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }catch (IOException e){
                System.err.println("error when opening file.");
            }

//            need to create an action event somehow to trigger the compare window with
//            Action myAction = ActionManager.getInstance().getAction(IdeActions.ACTION_COMPARE_CLIPBOARD_WITH_SELECTION);
//            int uniqueId = (int) System.currentTimeMillis();
//            String commandName = "fileSelectedTrigger";
//            ActionEvent event = new ActionEvent(file, uniqueId, commandName);
//            myAction.actionPerformed(event);
        }

    }

    public JPanel getContent() { return FileMetricWindowContent; }

}
