package org.studieren_ohne_grenzen.balancetobudget;

import java.io.File;

import javax.swing.JFileChooser;

public class Util {
    public static File pickFile(boolean isSave) {
        JFileChooser fc = new JFileChooser();
        int returnVal;
        if (isSave) {
            returnVal = fc.showSaveDialog(null);
        } else {
            returnVal = fc.showOpenDialog(null);
        }

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return file;
        } else {
            System.err.println("Open command cancelled by user.");
            System.exit(-1); // todo better exception
            return null; // to satisfy
        }
    }
}