package de.acebarn.photoOrganizr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageProcessingException;

import de.acebarn.photoOrganizr.io.IOController;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		// Properties lesen
		// Quellordner wählen
		// Fotos kopieren
		//

		String sourcePath = "/home/alessio/Pictures/2015/01";
		String targetPath = "/home/alessio/Pictures/Collection";
		String prefix = "ACB";

		String supportedFileTypes = "jpg JPG";

		IOController ioc = new IOController(sourcePath, supportedFileTypes, targetPath, prefix);

		try {
			List<File> allImageFiles = ioc.readDirectory();
			for (File image : allImageFiles) {
				ioc.organizePictureByDay(image);

			}

		} catch (FileNotFoundException e) {
			logger.error("Konnte Verzeichnis mit den Bildern nicht laden...", e);
		} catch (ImageProcessingException e) {
			logger.error("Konnte Metadaten der Datei nicht lesen", e);
		} catch (IOException e) {
			logger.error("Konnte Bilder nicht verarbeiten", e);
		}

	}
}
