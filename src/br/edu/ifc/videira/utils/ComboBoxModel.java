package br.edu.ifc.videira.utils;

import javax.swing.DefaultComboBoxModel;

/**
 * Modelo para ComboBox que bloqueia seleção de itens específicos
 * @Source https://java.docow.com/51093/java-torne-um-item-de-um-jcombobox-nao-selecionavel-como-para-uma-sub-legenda-e-edite-a-fonte-desse-item.html
 *
 */
public class ComboBoxModel extends DefaultComboBoxModel<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ComboBoxModel() {
	}

	public ComboBoxModel(String[] strings) {
		super(strings);
	}

	@Override
	public void setSelectedItem(Object item) {
		if (item.toString().startsWith("__"))
			return;
		super.setSelectedItem(item);
	};
}