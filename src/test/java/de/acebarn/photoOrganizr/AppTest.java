package de.acebarn.photoOrganizr;

import java.io.File;

import org.junit.Test;

import de.acebarn.photoOrganizr.io.IOController;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	@Test
	public void testListAllImageFiles() throws Exception {
		IOController controller = new IOController();
		
		
		for (File f : controller.readDirectory()) {
			System.out.println(f.getAbsolutePath());
		}
	}
}
