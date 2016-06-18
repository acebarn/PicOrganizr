package de.acebarn.photoOrganizr.view;

import com.drew.imaging.ImageProcessingException;
import de.acebarn.photoOrganizr.io.IOController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by alessio on 18.06.16.
 */
public class POView
{
    private static final String SUPPORTED_FILE_TYPES = "jpg JPG nef NEF orf ORF";
    private static final Logger logger = LoggerFactory.getLogger(POView.class);


    private JButton btnSource;
    private JLabel lblSourceDir;
    private JButton btnTarget;
    private JLabel lblTargetDir;
    private JTextField tfPrefix;
    private JLabel lblPrefix;
    private JLabel lblModus;
    private JComboBox modusComboBox;
    private JButton btnImport;
    private JProgressBar progressBar;
    private JPanel pnlMain;
    private JButton btnAbort;

    private File sourcePath;
    private File targetPath;
    private String prefix;
    private IOController controller;
    private Thread worker;

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("PhotoOrganizr");
        frame.setContentPane(new POView().pnlMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }
    }

    public POView()
    {
        worker = new Thread(new ImageProcessor());

        btnSource.addActionListener(new BtnSourceActionListener());
        btnTarget.addActionListener(new BtnTargetActionListener());
        btnImport.addActionListener(new BtnImportActionListener());
        progressBar.setMinimum(0);

        controller = new IOController();
        controller.setSupportedFileTypes(SUPPORTED_FILE_TYPES);
        controller.readPreviousConfiguration();
        setSourcePath(controller.getSourcePath());
        setTargetPath(controller.getTargetPath());
        setPrefix(controller.getPrefix());

        if (sourcePath!=null | targetPath != null | prefix != null)
        {
            lblSourceDir.setText(sourcePath.getAbsolutePath());
            lblTargetDir.setText(targetPath.getAbsolutePath());
            lblPrefix.setText(prefix);
        }

        btnAbort.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent actionEvent)
            {
                if (worker.isAlive())
                {
                    worker.stop();
                }
            }
        });
    }

    public void setSourcePath(File sourcePath) {
        this.sourcePath = sourcePath;
    }

    public void setTargetPath(File targetPath) {
        this.targetPath = targetPath;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private class BtnSourceActionListener implements ActionListener
    {

        public void actionPerformed(ActionEvent actionEvent)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Bitte Quellordner wählen");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int returnVal = fileChooser.showOpenDialog(btnSource);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                lblSourceDir.setText(fileChooser.getSelectedFile().getAbsolutePath());
                sourcePath = fileChooser.getSelectedFile();
            }
        }
    }

    private class BtnTargetActionListener implements ActionListener
    {

        public void actionPerformed(ActionEvent actionEvent)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Bitte Zielordner wählen");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);

            int returnVal = fileChooser.showOpenDialog(btnSource);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                lblTargetDir.setText(fileChooser.getSelectedFile().getAbsolutePath());
                targetPath = fileChooser.getSelectedFile();
            }
        }
    }

    private class BtnImportActionListener implements ActionListener
    {

        public void actionPerformed(ActionEvent actionEvent)
        {
            if (sourcePath == null || targetPath == null) {
                JOptionPane.showMessageDialog(null, "Bitte Quell- und Zielverzeichnis angeben!",
                        "Bitte Verzeichnisse angeben", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Thread t = new Thread(new ImageProcessor());
            t.start();
        }
    }

    private class ImageProcessor implements Runnable {

        public void run() {

            String prefix = tfPrefix.getText();

            controller.setSourcePath(sourcePath);
            controller.setTargetPath(targetPath);
            controller.setPrefix(prefix);
            controller.writePreviousSourcePath();

            List<File> allImageFiles;
            try {
                allImageFiles = controller.readDirectory();

                int progress = 0;
                progressBar.setValue(progressBar.getMinimum());
                progressBar.setMaximum(allImageFiles.size());

                for (File image : allImageFiles) {
                    controller.organizePictureByDay(image);
                    progress++;
                    progressBar.setValue(progress);

                }

            } catch (FileNotFoundException e1) {
            } catch (ImageProcessingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
