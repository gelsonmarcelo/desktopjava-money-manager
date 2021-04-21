package br.edu.ifc.videira.controllers.views;

import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.*;
import java.sql.SQLException;
import java.awt.*;
import javax.swing.border.BevelBorder;

import br.edu.ifc.videira.DAOs.UsuarioDao;
import br.edu.ifc.videira.utils.ComboBoxModel;
import br.edu.ifc.videira.utils.JNumberFormatField;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.Action;

public class MainInternalFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	public static JDesktopPane desktop;
	private String tema = "com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme";
	private JMenuItem miEncerrar; // Não podem ser locais pq contém atalhos
	private JMenuItem miEditarDados;
	public static JMenuBar menuBar = new JMenuBar();
	UsuarioDao usDao = new UsuarioDao();

	// Fontes
	static final Font fonte1 = new Font("Sitka Subheading", Font.PLAIN, 40);
	static final Font fonte2 = new Font("Sitka Subheading", Font.PLAIN, 35);
	static final Font fonte3 = new Font("Sitka Subheading", Font.PLAIN, 30);
	static final Font fonte4 = new Font("Sitka Subheading", Font.PLAIN, 25);
	static final Font fonte5 = new Font("Sitka Subheading", Font.PLAIN, 20);
	static final Font fonte6 = new Font("Sitka Subheading", Font.PLAIN, 15);
	static final Font fonte7 = new Font("Sitka Subheading", Font.PLAIN, 10);
	static final Font fonte8 = new Font("Sitka Subheading", Font.PLAIN, 05);
	static final Font fonteBotoes = new Font("Sitka Subheading", Font.PLAIN, 20);
	static final Font fonteTabela = new Font("Trebuchet MS", Font.PLAIN, 15);
	static final Font FonteJNumberFormatField = new Font("Calibri", Font.PLAIN, 20);
	public static JComboBox<Object> cbTema;
	public static JTextField tfSaldoTotal;
	private final Action alternaVisualizacaoSaldo = new SwingAction();
	private JLabel lbSaldo;
	private JButton btAlternaVisualizacaoSaldo;

	public MainInternalFrame() {
		/*
		 * Fazer internal frames pegarem decoração do sistema
		 */
		try {
			UIManager.setLookAndFeel(tema);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Falha ao carregar tema\n" + ex.getMessage(), "Falha",
					JOptionPane.WARNING_MESSAGE);
		}
		
		// Adicionar imagem ícone do sistema
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/icone-principal-32.png")));

		// Make the big window be indented 50 pixels from each edge
		// of the screen.
		int inset = 50;
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset, 2000, 2000);

		// Set up the GUI.
		desktop = new JDesktopPane(); // a specialized layered pane
		desktop.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		login(); // create first "window"
		setContentPane(desktop);
		desktop.setBackground(Color.WHITE);

		/* cbTema.setModel(new DefaultComboBoxModel<Object>( */
		cbTema = new JComboBox<Object>(new ComboBoxModel(new String[] {
				"________ * Core Themes * ________",
				"Flat Light * com.formdev.flatlaf.FlatLightLaf", 
				"Flat Dark * com.formdev.flatlaf.FlatDarkLaf",
				"Flat IntelliJ * com.formdev.flatlaf.FlatIntelliJLaf",
				"Flat Darcula * com.formdev.flatlaf.FlatDarculaLaf", 
				"________ * IntelliJ Themes * ________",
				"Arc * com.formdev.flatlaf.intellijthemes.FlatArcIJTheme",
				"Arc - Orange * com.formdev.flatlaf.intellijthemes.FlatArcOrangeIJTheme",
				"Arc Dark * com.formdev.flatlaf.intellijthemes.FlatArcDarkIJTheme",
				"Arc Dark - Orange * com.formdev.flatlaf.intellijthemes.FlatArcDarkOrangeIJTheme",
				"Carbon * com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme",
				"Cobalt 2 * com.formdev.flatlaf.intellijthemes.FlatCobalt2IJTheme",
				"Cyan light * com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme",
				"Dark Flat * com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme",
				"Dark purple * com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme",
				"Dracula * com.formdev.flatlaf.intellijthemes.FlatDraculaIJTheme",
				"Gradianto Dark Fuchsia * com.formdev.flatlaf.intellijthemes.FlatGradiantoDarkFuchsiaIJTheme",
				"Gradianto Deep Ocean * com.formdev.flatlaf.intellijthemes.FlatGradiantoDeepOceanIJTheme",
				"Gradianto Midnight Blue * com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme",
				"Gradianto Nature Green * com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme",
				"Gray * com.formdev.flatlaf.intellijthemes.FlatGrayIJTheme",
				"Gruvbox Dark Hard * com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkHardIJTheme",
				"Gruvbox Dark Medium * com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkMediumIJTheme",
				"Gruvbox Dark Soft * com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkSoftIJTheme",
				"Hiberbee Dark * com.formdev.flatlaf.intellijthemes.FlatHiberbeeDarkIJTheme",
				"High contrast * com.formdev.flatlaf.intellijthemes.FlatHighContrastIJTheme",
				"Light Flat * com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme",
				"Material Design Dark * com.formdev.flatlaf.intellijthemes.FlatMaterialDesignDarkIJTheme",
				"Monocai * com.formdev.flatlaf.intellijthemes.FlatMonocaiIJTheme",
				"Nord * com.formdev.flatlaf.intellijthemes.FlatNordIJTheme",
				"One Dark * com.formdev.flatlaf.intellijthemes.FlatOneDarkIJTheme",
				"Solarized Dark * com.formdev.flatlaf.intellijthemes.FlatSolarizedDarkIJTheme",
				"Solarized Light * com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme",
				"Spacegray * com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme",
				"Vuesion * com.formdev.flatlaf.intellijthemes.FlatVuesionIJTheme",
				"________ * Material Theme UI Lite * ________",
				"Arc Dark (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkIJTheme",
				"Arc Dark Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatArcDarkContrastIJTheme",
				"Atom One Dark (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkIJTheme",
				"Atom One Dark Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme",
				"Atom One Light (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme",
				"Atom One Light Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme",
				"Dracula (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatDraculaIJTheme",
				"Dracula Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatDraculaContrastIJTheme",
				"GitHub (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme",
				"GitHub Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubContrastIJTheme",
				"GitHub Dark (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkIJTheme",
				"GitHub Dark Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkContrastIJTheme",
				"Light Owl (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlIJTheme",
				"Light Owl Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlContrastIJTheme",
				"Material Darker (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme",
				"Material Darker Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerContrastIJTheme",
				"Material Deep Ocean (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanIJTheme",
				"Material Deep Ocean Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanContrastIJTheme",
				"Material Lighter (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterIJTheme",
				"Material Lighter Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialLighterContrastIJTheme",
				"Material Oceanic (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicIJTheme",
				"Material Oceanic Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialOceanicContrastIJTheme",
				"Material Palenight (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightIJTheme",
				"Material Palenight Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialPalenightContrastIJTheme",
				"Monokai Pro (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProIJTheme",
				"Monokai Pro Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProContrastIJTheme",
				"Moonlight (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme",
				"Moonlight Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightContrastIJTheme",
				"Night Owl (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlIJTheme",
				"Night Owl Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlContrastIJTheme",
				"Solarized Dark (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedDarkIJTheme",
				"Solarized Dark Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedDarkContrastIJTheme",
				"Solarized Light (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedLightIJTheme",
				"Solarized Light Contrast (Material) * com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatSolarizedLightContrastIJTheme" }));
		cbTema.setFont(new Font("Sitka Subheading", Font.PLAIN, 20));
		cbTema.setBounds(182, 24, 363, 26);
		cbTema.addItemListener(new ItemListener() {
			//### - Arrumar bug está executando esse código 2 vezes quando seleciona 1 vez o item.
			public void itemStateChanged(ItemEvent e) {
				try {
					tema = cbTema.getSelectedItem().toString().split("\\* ")[1];

					UIManager.setLookAndFeel(tema);

					usDao.salvarTema(tema);

				} catch (Exception ex) {
					tema = "com.formdev.flatlaf.intellijthemes.FlatLightFlatIJTheme";
					try {
						UIManager.setLookAndFeel(tema);
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException e1) {
						e1.printStackTrace();
					}
					System.out.println("Falha ao carregar tema.");
				}
				// Atualizar sistema com o tema
				SwingUtilities.updateComponentTreeUI(desktop);
			}
		});
		desktop.add(cbTema);
		
		JLabel lbAtribuicao = new JLabel("Photo by Michael Longmire on Unsplash. \u00CDcones feitos por Freepik, xnimrodx, Pixel perfect, Good Ware, Becris, dDara, ultimatearm, Payungkead, Icongeek26, Iconixar, turkkub, xnimrodx. ");
		lbAtribuicao.setForeground(Color.BLACK);
		lbAtribuicao.setHorizontalAlignment(SwingConstants.TRAILING);
		lbAtribuicao.setBounds(10, 0, 1344, 19);
		desktop.add(lbAtribuicao);
		
		JLabel lbTema = new JLabel("Personalizar tema:");
		lbTema.setForeground(Color.BLACK);
		lbTema.setFont(MainInternalFrame.fonte5);
		lbTema.setBounds(10, 24, 184, 26);
		desktop.add(lbTema);
		
		tfSaldoTotal = new JNumberFormatField();
		tfSaldoTotal.setBounds(176, 61, 166, 32);
		tfSaldoTotal.setFont(FonteJNumberFormatField);
		desktop.add(tfSaldoTotal);
		tfSaldoTotal.setColumns(10);
		tfSaldoTotal.setVisible(false);
		
		lbSaldo = new JLabel("Saldo Total:");
		lbSaldo.setFont(fonte5);
		lbSaldo.setBounds(62, 61, 113, 32);
		lbSaldo.setVisible(false);
		desktop.add(lbSaldo);
		
		btAlternaVisualizacaoSaldo = new JButton("");
		btAlternaVisualizacaoSaldo.setBackground(Color.WHITE);
		btAlternaVisualizacaoSaldo.setAction(alternaVisualizacaoSaldo);
		btAlternaVisualizacaoSaldo.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/mostrar.png")));
		btAlternaVisualizacaoSaldo.setBounds(10, 61, 42, 32);
		desktop.add(btAlternaVisualizacaoSaldo);
		
		JLabel lbImagemFundo = new JLabel("");
		lbImagemFundo.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/fundo-money-grande.jpg")));
		lbImagemFundo.setBounds(0, 0, 1364, 721);
		desktop.add(lbImagemFundo);

		menuBar.setFont(fonte5);
		setJMenuBar(createMenuBar());

		setTitle("Money Manager 2.0");
	}

	protected JMenuBar createMenuBar() {

		// Set up the lone menu.
		JMenu mnUsuario = new JMenu("Usu\u00E1rio");
		mnUsuario.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/usuario.png")));
		mnUsuario.setFont(fonte5);
		mnUsuario.setMnemonic(KeyEvent.VK_D);
		menuBar.add(mnUsuario);

		miEditarDados = new JMenuItem("Editar dados");
		miEditarDados.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/editar.png")));
		miEditarDados.setFont(fonte5);
		miEditarDados.setMnemonic(KeyEvent.VK_N);
		miEditarDados.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
		miEditarDados.setActionCommand("editarDados");
		miEditarDados.addActionListener(this);
		mnUsuario.add(miEditarDados);

		JMenuItem miLogout = new JMenuItem("Logout");
		miLogout.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/logout.png")));
		miLogout.setFont(fonte5);
		miLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UsuarioDao.idUser = 0;
				login();
			}
		});
		mnUsuario.add(miLogout);

		// Set up the second menu item.
		miEncerrar = new JMenuItem("Encerrar");
		miEncerrar.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/cancelar.png")));
		miEncerrar.setFont(fonte5);
		miEncerrar.setMnemonic(KeyEvent.VK_Q);
		miEncerrar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		miEncerrar.setActionCommand("quit");
		miEncerrar.addActionListener(this);
		mnUsuario.add(miEncerrar);

		JMenu mnRegistro = new JMenu("Registro");
		mnRegistro.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/registro.png")));
		mnRegistro.setFont(fonte5);
		menuBar.add(mnRegistro);

		JMenuItem miRegistrar = new JMenuItem("Lançar");
		miRegistrar.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/registrar.png")));
		miRegistrar.setFont(fonte5);
		miRegistrar.setActionCommand("registrar");
		miRegistrar.addActionListener(this);
		mnRegistro.add(miRegistrar);

		JMenuItem miVerRegistro = new JMenuItem("Ver lançamentos");
		miVerRegistro.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/ver-registros.png")));
		miVerRegistro.setFont(fonte5);
		miVerRegistro.setActionCommand("verRegistros");
		miVerRegistro.addActionListener(this);
		mnRegistro.add(miVerRegistro);

		JMenuItem miEditarClassificao = new JMenuItem("Editar classifica\u00E7\u00E3o");
		miEditarClassificao.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/editar.png")));
		miEditarClassificao.setFont(fonte5);
		miEditarClassificao.setActionCommand("editarClassificacao");
		miEditarClassificao.addActionListener(this);
		mnRegistro.add(miEditarClassificao);

		JMenu mnInstituicoes = new JMenu("Institui\u00E7\u00F5es");
		mnInstituicoes.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/instituicao.png")));
		mnInstituicoes.setFont(fonte5);
		menuBar.add(mnInstituicoes);

		JMenuItem miTransferencia = new JMenuItem("Transferir valores");
		miTransferencia.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/transferir.png")));
		miTransferencia.setFont(fonte5);
		miTransferencia.setActionCommand("transferencia");
		miTransferencia.addActionListener(this);
		mnInstituicoes.add(miTransferencia);

		JMenuItem miEditarInstituicao = new JMenuItem("Editar");
		miEditarInstituicao.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/editar.png")));
		miEditarInstituicao.setFont(fonte5);
		miEditarInstituicao.setActionCommand("editarInstituicao");
		miEditarInstituicao.addActionListener(this);
		mnInstituicoes.add(miEditarInstituicao);

		JMenu mnContasMensais = new JMenu("Contas Mensais");
		mnContasMensais.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/contas.png")));
		mnContasMensais.setFont(fonte5);
		menuBar.add(mnContasMensais);
		mnContasMensais.setEnabled(false);
		mnContasMensais.setToolTipText("Não disponível");

		JMenuItem miExibir = new JMenuItem("Exibir");
		miExibir.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/ver-registros.png")));
		miExibir.setFont(fonte5);
		miExibir.setActionCommand("verContas");
		miExibir.addActionListener(this);
		mnContasMensais.add(miExibir);

		JMenuItem miCriar = new JMenuItem("Criar");
		miCriar.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/registrar.png")));
		miCriar.setFont(fonte5);
		miCriar.setActionCommand("registrar");
		miCriar.setToolTipText("### - Travar no registro de contas");
		miCriar.addActionListener(this);
		mnContasMensais.add(miCriar);

		JMenu mnPessoas = new JMenu("Pessoas");
		mnPessoas.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/pessoas.png")));
		mnPessoas.setFont(fonte5);
		menuBar.add(mnPessoas);

		JMenuItem miAcessar = new JMenuItem("Acessar");
		miAcessar.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/registrar.png")));
		miAcessar.setFont(fonte5);
		miAcessar.setActionCommand("editarPessoas");
		miAcessar.addActionListener(this);
		mnPessoas.add(miAcessar);

		JMenu mnLogsDoSistema = new JMenu("Logs do sistema");
		mnLogsDoSistema.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/logspng.png")));
		mnLogsDoSistema.setFont(fonte5);
		menuBar.add(mnLogsDoSistema);

		JMenuItem miVer = new JMenuItem("Ver");
		miVer.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/ver-registros.png")));
		miVer.setFont(fonte5);
		miVer.setActionCommand("verLogs");
		miVer.addActionListener(this);
		mnLogsDoSistema.add(miVer);

		return menuBar;
	}

	// React to menu selections.
	public void actionPerformed(ActionEvent e) {
		if ("editarDados".equals(e.getActionCommand())) { // new
			try {
				editarDados();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if ("registrar".equals(e.getActionCommand())) { // new
			registrar();
		} else if ("verRegistros".equals(e.getActionCommand())) { // new
			verRegistros();
		} else if ("editarClassificacao".equals(e.getActionCommand())) { // new
			editarClassificacao();
		} else if ("transferencia".equals(e.getActionCommand())) { // new
			transferencia();
		} else if ("editarInstituicao".equals(e.getActionCommand())) { // new
			editarInstituicao();
		} else if ("verContas".equals(e.getActionCommand())) { // new
			verContas();
		} else if ("editarPessoas".equals(e.getActionCommand())) { // new
			editarPessoas();
		} else if ("verLogs".equals(e.getActionCommand())) { // new
			verLogs();
		} else { // quit
			quit();
		}
	}

	// Create a new internal frame.
	protected void login() {
		IFuLogin frame = new IFuLogin();
		frame.setLocation(312, 144);
		menuBar.setVisible(false);
		frame.setVisible(true);
		frame.centralizarJanela();
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void editarDados() throws SQLException, Exception {
		IFuEditarUsuario frame = new IFuEditarUsuario();
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void registrar() {
		IFrRegistrar frame = new IFrRegistrar();
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void verRegistros() {
		IFrVerRegistros frame = new IFrVerRegistros();
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void editarClassificacao() {
		IFrEditarClassificacao frame = new IFrEditarClassificacao();
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void transferencia() {
		IFiTransferir frame = new IFiTransferir();
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void editarInstituicao() {
		IFiEditarInstituicao frame = new IFiEditarInstituicao();
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void verContas() {
		IFcVerContas frame = new IFcVerContas();
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void editarPessoas() {
		IFpEditarPessoas frame = new IFpEditarPessoas();
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	protected void verLogs() {
		IFlLogs frame = new IFlLogs();
		frame.setVisible(true);
		desktop.add(frame);
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	// Quit the application.
	protected void quit() {
		System.exit(0);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Make sure we have nice window decorations.
		// JFrame.setDefaultLookAndFeelDecorated(false);

		// Create and set up the window.
		MainInternalFrame frame = new MainInternalFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Display the window.
		frame.setVisible(true);

		// Set maximized
		frame.setExtendedState(MAXIMIZED_BOTH);
	}
	
	/**
	 * Ação do botão que alterna visualização do saldo
	 * @param args
	 */
	
	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
	private class SwingAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public SwingAction() {
			putValue(SHORT_DESCRIPTION, "Alternar visualização do saldo total");
		}
		public void actionPerformed(ActionEvent e) {
			if(lbSaldo.isVisible()) {
				lbSaldo.setVisible(false);
				tfSaldoTotal.setVisible(false);
				btAlternaVisualizacaoSaldo.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/mostrar.png")));
			}else {
				lbSaldo.setVisible(true);
				tfSaldoTotal.setVisible(true);
				btAlternaVisualizacaoSaldo.setIcon(new ImageIcon(MainInternalFrame.class.getResource("/br/edu/ifc/videira/imgs/ocultar.png")));
			}
		}
	}
}