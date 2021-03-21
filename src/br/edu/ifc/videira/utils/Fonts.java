package br.edu.ifc.videira.utils;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class Fonts {
	public static void main(String[] args) {

		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts();

		for (Font fontTmp : fonts) {
			System.out.println(fontTmp.getFontName());
		}
	}
}