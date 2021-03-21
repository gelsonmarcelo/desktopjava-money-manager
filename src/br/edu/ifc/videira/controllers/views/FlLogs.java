package br.edu.ifc.videira.controllers.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class FlLogs extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public FlLogs() {
        super("Logs");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 600);

        File file = new File("\\teste.txt");
        FileInputStream fis = null;
        String texto = "";

        try {
            fis = new FileInputStream(file);
            int content;
            while ((content = fis.read()) != -1) {
                texto += (char) content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        JTextArea textArea = new JTextArea(texto);
        textArea.setLineWrap(true); //quebra de linha automática
        getContentPane().add(textArea);
    }
}
