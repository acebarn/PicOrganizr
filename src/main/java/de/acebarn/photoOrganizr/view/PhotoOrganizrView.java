package de.acebarn.photoOrganizr.view;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;

import de.acebarn.photoOrganizr.io.IOController;

public class PhotoOrganizrView {

	private static final String SUPPORTED_FILE_TYPES = "jpg JPG nef NEF orf ORF";
	private static final Logger logger = LoggerFactory.getLogger(PhotoOrganizrView.class);

	private JFrame frmPhotoorganizr;
	private JTextField tfPrefix;

	private File sourcePath;
	private File targetPath;
	private String prefix;
	private IOController controller;
	private JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PhotoOrganizrView window = new PhotoOrganizrView();
					window.frmPhotoorganizr.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PhotoOrganizrView() {
		controller = new IOController();
		controller.setSupportedFileTypes(SUPPORTED_FILE_TYPES);
		controller.readPreviousConfiguration();
		setSourcePath(controller.getSourcePath());
		setTargetPath(controller.getTargetPath());
		setPrefix(controller.getPrefix());
		initialize();
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPhotoorganizr = new JFrame();
		frmPhotoorganizr.setTitle("PhotoOrganizr");
		frmPhotoorganizr.setBounds(100, 100, 450, 300);
		frmPhotoorganizr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		frmPhotoorganizr.getContentPane().setLayout(gridBagLayout);

		final JButton btnSource = new JButton("Quellordner");
		GridBagConstraints gbc_btnSource = new GridBagConstraints();
		gbc_btnSource.insets = new Insets(0, 0, 5, 5);
		gbc_btnSource.gridx = 0;
		gbc_btnSource.gridy = 0;
		frmPhotoorganizr.getContentPane().add(btnSource, gbc_btnSource);

		final JLabel lblSourceDir = new JLabel(sourcePath == null ? "..." : sourcePath.getAbsolutePath());
		GridBagConstraints gbc_lblSourceDir = new GridBagConstraints();
		gbc_lblSourceDir.insets = new Insets(0, 0, 5, 0);
		gbc_lblSourceDir.gridx = 1;
		gbc_lblSourceDir.gridy = 0;
		frmPhotoorganizr.getContentPane().add(lblSourceDir, gbc_lblSourceDir);

		JButton btnTarget = new JButton("Zielordner");
		GridBagConstraints gbc_btnTarget = new GridBagConstraints();
		gbc_btnTarget.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTarget.insets = new Insets(0, 0, 5, 5);
		gbc_btnTarget.gridx = 0;
		gbc_btnTarget.gridy = 1;
		frmPhotoorganizr.getContentPane().add(btnTarget, gbc_btnTarget);

		final JLabel lblTargetDir = new JLabel(targetPath == null ? "..." : targetPath.getAbsolutePath());
		GridBagConstraints gbc_lblTargetDir = new GridBagConstraints();
		gbc_lblTargetDir.insets = new Insets(0, 0, 5, 0);
		gbc_lblTargetDir.gridx = 1;
		gbc_lblTargetDir.gridy = 1;
		frmPhotoorganizr.getContentPane().add(lblTargetDir, gbc_lblTargetDir);

		JLabel lblPrefix = new JLabel("Präfix");
		GridBagConstraints gbc_lblPrefix = new GridBagConstraints();
		gbc_lblPrefix.anchor = GridBagConstraints.EAST;
		gbc_lblPrefix.insets = new Insets(0, 0, 5, 5);
		gbc_lblPrefix.gridx = 0;
		gbc_lblPrefix.gridy = 2;
		frmPhotoorganizr.getContentPane().add(lblPrefix, gbc_lblPrefix);

		tfPrefix = new JTextField(prefix == null ? "" : prefix);
		GridBagConstraints gbc_tfPrefix = new GridBagConstraints();
		gbc_tfPrefix.insets = new Insets(0, 0, 5, 0);
		gbc_tfPrefix.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfPrefix.gridx = 1;
		gbc_tfPrefix.gridy = 2;
		frmPhotoorganizr.getContentPane().add(tfPrefix, gbc_tfPrefix);
		tfPrefix.setColumns(10);

		JLabel lblModus = new JLabel("Sortierung");
		GridBagConstraints gbc_lblModus = new GridBagConstraints();
		gbc_lblModus.anchor = GridBagConstraints.EAST;
		gbc_lblModus.insets = new Insets(0, 0, 5, 5);
		gbc_lblModus.gridx = 0;
		gbc_lblModus.gridy = 3;
		frmPhotoorganizr.getContentPane().add(lblModus, gbc_lblModus);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "Jahr -> Monat -> Tag" }));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		frmPhotoorganizr.getContentPane().add(comboBox, gbc_comboBox);

		JButton btnImport = new JButton("Importieren!");
		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.insets = new Insets(0, 0, 5, 0);
		gbc_btnImport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnImport.gridwidth = 2;
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 4;
		frmPhotoorganizr.getContentPane().add(btnImport, gbc_btnImport);

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setStringPainted(true);
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 2;
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.gridx = 0;
		gbc_progressBar.gridy = 5;
		frmPhotoorganizr.getContentPane().add(progressBar, gbc_progressBar);

		btnSource.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
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
		});

		btnTarget.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
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
		});

		btnImport.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (sourcePath == null || targetPath == null) {
					JOptionPane.showMessageDialog(null, "Bitte Quell- und Zielverzeichnis angeben!",
							"Bitte Verzeichnisse angeben", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				Thread t = new Thread(new ImageProcessor());
				t.start();
			}
		});
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
