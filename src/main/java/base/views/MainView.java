package base.views;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import base.controllers.MainViewService;

public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private JPanel contentPane;
	private JTextField placaInput;
	private JTextField modeloInput;
	private JTextField marcaInput;
	private JTextField corInput;
	private JTable tabelaVeiculos;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		setResizable(false);
		setTitle("Sistema de gestão de veículos");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 768, 395);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel placaLabel = new JLabel("Placa");
		placaLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
		placaLabel.setBounds(20, 21, 46, 14);
		contentPane.add(placaLabel);

		JLabel modeloLabel = new JLabel("Modelo");
		modeloLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
		modeloLabel.setBounds(20, 59, 54, 14);
		contentPane.add(modeloLabel);

		JLabel marcaLabel = new JLabel("Marca");
		marcaLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
		marcaLabel.setBounds(20, 90, 46, 14);
		contentPane.add(marcaLabel);

		JLabel corLabel = new JLabel("Cor");
		corLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
		corLabel.setBounds(194, 90, 46, 14);
		contentPane.add(corLabel);

		JLabel fabricacaoLabel = new JLabel("Data Fabricação");
		fabricacaoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		fabricacaoLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
		fabricacaoLabel.setBounds(194, 21, 129, 14);
		contentPane.add(fabricacaoLabel);

		placaInput = new JTextField();
		placaInput.setToolTipText("Insira a placa do veiculo a ser buscado ou salvo");
		placaInput.setBounds(84, 19, 86, 20);
		contentPane.add(placaInput);
		placaInput.setColumns(10);

		modeloInput = new JTextField();
		modeloInput.setToolTipText("Insira o modelo do veiculo a ser salvo");
		modeloInput.setColumns(10);
		modeloInput.setBounds(84, 57, 86, 20);
		contentPane.add(modeloInput);

		marcaInput = new JTextField();
		marcaInput.setToolTipText("Insira a marca do veiculo a ser salvo");
		marcaInput.setColumns(10);
		marcaInput.setBounds(84, 88, 86, 20);
		contentPane.add(marcaInput);

		corInput = new JTextField();
		corInput.setToolTipText("Insira a cor do veiculo a ser salvo");
		corInput.setColumns(10);
		corInput.setBounds(237, 88, 86, 20);
		contentPane.add(corInput);

		SimpleDateFormat inputDataPattern = new SimpleDateFormat("yyyy-MM-dd");
		JFormattedTextField dateInput = new JFormattedTextField(inputDataPattern);
		dateInput.setToolTipText("Insira a data de fabricação do veiculo a ser salvo, no formato ano-mes-dia");
		dateInput.setBounds(194, 57, 129, 20);
		contentPane.add(dateInput);

		JLabel formatoDataLabel = new JLabel("Formato: 1999-12-31");
		formatoDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
		formatoDataLabel.setFont(new Font("Monospaced", Font.BOLD, 10));
		formatoDataLabel.setBounds(194, 35, 129, 14);
		contentPane.add(formatoDataLabel);

		JTextPane mensagens = new JTextPane();
		mensagens.setBackground(SystemColor.inactiveCaption);
		mensagens.setEditable(false);
		mensagens.setBounds(30, 285, 285, 49);
		contentPane.add(mensagens);

		JLabel labelModoEditor = new JLabel("Modo atual: Criando novo registro");
		labelModoEditor.setFont(new Font("Monospaced", Font.BOLD, 14));
		labelModoEditor.setBounds(20, 230, 303, 20);
		contentPane.add(labelModoEditor);

		JButton btnExcluir = new JButton("Excluir");
		JButton btnNovo = new JButton("Novo");
		btnNovo.setEnabled(false);
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				placaInput.setText("");
				marcaInput.setText("");
				modeloInput.setText("");
				dateInput.setText("");
				corInput.setText("");

				labelModoEditor.setText("Modo atual: Criando novo registro");

				btnNovo.setEnabled(false);
				btnExcluir.setEnabled(false);
			}
		});
		btnNovo.setFont(new Font("Monospaced", Font.BOLD, 14));
		btnNovo.setBackground(SystemColor.inactiveCaption);
		btnNovo.setBounds(20, 164, 134, 23);
		contentPane.add(btnNovo);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mensagens.setText("");

				var placa = placaInput.getText();
				var marca = marcaInput.getText();
				var modelo = modeloInput.getText();
				var cor = corInput.getText();
				var fabricacao = dateInput.getText();

				try {
					MainViewService.salvar(placa, marca, modelo, fabricacao, cor);
					mensagens.setText("Salvo com sucesso !");
					btnNovo.doClick();
				} catch (RuntimeException ex) {
					mensagens.setText(ex.getMessage());
				}

			}
		});
		btnSalvar.setFont(new Font("Monospaced", Font.BOLD, 14));
		btnSalvar.setBackground(SystemColor.inactiveCaption);
		btnSalvar.setBounds(20, 130, 134, 23);
		contentPane.add(btnSalvar);

		JButton btnListarTodos = new JButton("Listar todos");
		btnListarTodos.setFont(new Font("Monospaced", Font.BOLD, 14));
		btnListarTodos.setBackground(SystemColor.inactiveCaption);
		btnListarTodos.setBounds(189, 130, 134, 23);
		contentPane.add(btnListarTodos);

		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				var placa = placaInput.getText();

				try {
					MainViewService.excluir(placa);
					btnNovo.doClick();

					mensagens.setText("Veículo excluído com sucesso !");

					btnNovo.setEnabled(false);
					btnExcluir.setEnabled(false);

				} catch (RuntimeException re) {
					mensagens.setText(re.getMessage());
				}
			}
		});
		btnExcluir.setFont(new Font("Monospaced", Font.BOLD, 14));
		btnExcluir.setBackground(SystemColor.inactiveCaption);
		btnExcluir.setBounds(20, 198, 134, 23);
		contentPane.add(btnExcluir);

		JButton btnBuscarPlaca = new JButton("Buscar placa");
		btnBuscarPlaca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnNovo.doClick();

				var placa = placaInput.getText();

				try {
					var veiculo = MainViewService.buscar(placa);

					marcaInput.setText(veiculo.getMarca());
					modeloInput.setText(veiculo.getModelo());
					corInput.setText(veiculo.getCor());
					dateInput.setText(veiculo.getFabricacao().format(DATE_FORMATTER));

					mensagens.setText("Veículo encontrado !");

					btnNovo.setEnabled(true);
					btnExcluir.setEnabled(true);

				} catch (RuntimeException re) {
					mensagens.setText(re.getMessage());
				}
			}
		});
		btnBuscarPlaca.setFont(new Font("Monospaced", Font.BOLD, 14));
		btnBuscarPlaca.setBackground(SystemColor.inactiveCaption);
		btnBuscarPlaca.setBounds(189, 164, 134, 23);
		contentPane.add(btnBuscarPlaca);

		tabelaVeiculos = new JTable();
		tabelaVeiculos.setToolTipText("visualização dos veículos cadastrados");
		tabelaVeiculos.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tabelaVeiculos.setBackground(Color.LIGHT_GRAY);
		tabelaVeiculos.setBounds(333, 11, 419, 333);
		contentPane.add(tabelaVeiculos);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(20, 258, 303, 86);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel mensagensLabel = new JLabel("Mensagens do sistema:");
		mensagensLabel.setBounds(10, 11, 147, 17);
		panel.add(mensagensLabel);
		mensagensLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
		mensagensLabel.setToolTipText("Mensagens do sistema");
	}
}
