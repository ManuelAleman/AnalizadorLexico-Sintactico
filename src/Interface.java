import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Interface extends JFrame implements ActionListener, KeyListener {

    private AnalizadorLexico analizadorLexico = new AnalizadorLexico();

    private AnalizadorSintactico analizadorSintactico = new AnalizadorSintactico();

    private JButton btnOpen, btnLexico, btnSintactico, btnGuardarCodigo;

    private JFileChooser fileChooser;

    private JLabel lblLexicoStatus, lblSintacticoStatus, lblConsoleInfo;

    private JTextArea txtCode, txtTablaSimbolos, txtTokens;

    private int idBotonActual;

    public Interface() {
        super("Automatas");

        setSize(1500, 1000);
        getContentPane().setBackground(new Color(251, 254, 187));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        fileChooser = new JFileChooser();
        makeInterface();
        addListeners();
        idBotonActual = -1;
        setVisible(true);
    }

    public void makeInterface() {
        JPanel codeArea = new JPanel();
        codeArea.setLayout(null);
        codeArea.setBounds(30, 30, 800, 600);
        codeArea.setBackground(new Color(184, 228, 250));
        codeArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(codeArea);

        JLabel lblCode = new JLabel("Codigo");
        lblCode.setFont(new Font("Arial", Font.PLAIN, 20));
        lblCode.setHorizontalAlignment(SwingConstants.CENTER);
        lblCode.setBounds(0, 0, 800, 50);
        codeArea.add(lblCode);

        txtCode = new JTextArea();
        txtCode.setBounds(10, 50, 780, 540);
        txtCode.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JScrollPane scroll = new JScrollPane(txtCode);
        scroll.setBounds(10, 50, 780, 540);
        codeArea.add(scroll);

        btnOpen = new JButton("Abrir Archivo");
        btnOpen.setBounds(1000, 30, 150, 50);
        //btnOpen.setBounds(1090, 30, 150, 50);
        btnOpen.setBackground(new Color(192, 199, 200));
        btnOpen.setForeground(Color.BLACK);
        btnOpen.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnOpen.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(btnOpen);

        btnGuardarCodigo = new JButton("Guardar Codigo");
        btnGuardarCodigo.setBounds(1180, 30, 150, 50);
        btnGuardarCodigo.setBackground(new Color(192,199,200));
        btnGuardarCodigo.setForeground(Color.BLACK);
        btnGuardarCodigo.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnGuardarCodigo.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
        add(btnGuardarCodigo);

        btnLexico = new JButton("Lexico");
        btnLexico.setBounds(1090, 130, 150, 50);
        btnLexico.setBackground(new Color(192, 199, 200));
        btnLexico.setForeground(Color.BLACK);
        btnLexico.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnLexico.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(btnLexico);

        lblLexicoStatus = new JLabel();
        lblLexicoStatus.setBounds(1260, 135, 40, 40);
        lblLexicoStatus.setOpaque(true);
        lblLexicoStatus.setBackground(Color.GRAY);
        add(lblLexicoStatus);

        btnSintactico = new JButton("Sintactico");
        btnSintactico.setBounds(1090, 200, 150, 50);
        btnSintactico.setBackground(new Color(192, 199, 200));
        btnSintactico.setForeground(Color.BLACK);
        btnSintactico.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnSintactico.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        btnSintactico.setEnabled(false);
        add(btnSintactico);

        lblSintacticoStatus = new JLabel();
        lblSintacticoStatus.setBounds(1260, 205, 40, 40);
        lblSintacticoStatus.setOpaque(true);
        lblSintacticoStatus.setBackground(Color.GRAY);
        add(lblSintacticoStatus);

        JPanel console = new JPanel();
        console.setLayout(null);
        console.setBounds(30, 650, 800, 300);
        console.setBackground(new Color(241, 190, 250));
        console.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(console);

        JLabel lblConsole = new JLabel("Consola");
        lblConsole.setFont(new Font("Arial", Font.PLAIN, 20));
        lblConsole.setHorizontalAlignment(SwingConstants.CENTER);
        lblConsole.setBounds(0, 0, 800, 50);
        console.add(lblConsole);

        lblConsoleInfo = new JLabel();
        lblConsoleInfo.setBounds(10, 50, 780, 240);
        lblConsoleInfo.setBackground(Color.WHITE);
        lblConsoleInfo.setOpaque(true);
        lblConsoleInfo.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JScrollPane scrollConsole = new JScrollPane(lblConsoleInfo);
        scrollConsole.setBounds(10, 50, 780, 240);
        console.add(scrollConsole);

        JPanel tokensPanel = new JPanel();
        tokensPanel.setLayout(null);
        tokensPanel.setBounds(850, 300, 300, 650);
        tokensPanel.setBackground(new Color(252, 206, 159));
        tokensPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(tokensPanel);

        JLabel lblTokens = new JLabel("Tokens");
        lblTokens.setFont(new Font("Arial", Font.PLAIN, 20));
        lblTokens.setHorizontalAlignment(SwingConstants.CENTER);
        lblTokens.setBounds(0, 0, 300, 50);
        tokensPanel.add(lblTokens);

        txtTokens = new JTextArea();
        txtTokens.setBounds(10, 50, 280, 590);
        txtTokens.setBackground(Color.WHITE);
        txtTokens.setOpaque(true);
        txtTokens.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JScrollPane scrollTokens = new JScrollPane(txtTokens);
        scrollTokens.setBounds(10, 50, 280, 590);
        tokensPanel.add(scrollTokens);

        JPanel tree = new JPanel();
        tree.setLayout(null);
        tree.setBounds(1170, 300, 300, 650);
        tree.setBackground(new Color(197, 253, 167));
        tree.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(tree);

        JLabel lblTree = new JLabel("Tabla de simbolos");
        lblTree.setFont(new Font("Arial", Font.PLAIN, 20));
        lblTree.setHorizontalAlignment(SwingConstants.CENTER);
        lblTree.setBounds(0, 0, 300, 50);
        tree.add(lblTree);

        txtTablaSimbolos = new JTextArea();
        txtTablaSimbolos.setBounds(10, 50, 280, 590);
        txtTablaSimbolos.setBackground(Color.WHITE);
        txtTablaSimbolos.setOpaque(true);
        txtTablaSimbolos.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JScrollPane scrollTree = new JScrollPane(txtTablaSimbolos);
        scrollTree.setBounds(10, 50, 280, 590);
        tree.add(scrollTree);
    }

    public void addListeners(){
        btnOpen.addActionListener(this);
        btnGuardarCodigo.addActionListener(this);
        btnLexico.addActionListener(this);
        btnSintactico.addActionListener(this);
        txtCode.addKeyListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnOpen){
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                System.out.println(file.getAbsolutePath());
                try {
                    String code = FileReader.readFile(file);
                    txtCode.setText(code);
                    idBotonActual = -1;
                    lblLexicoStatus.setBackground(Color.GRAY);
                    lblSintacticoStatus.setBackground(Color.GRAY);
                    btnSintactico.setEnabled(false);
                } catch (FileNotFoundException ex) {
                    txtCode.setText("Error al leer el archivo");
                    throw new RuntimeException(ex);
                }
            }
        }
        if (e.getSource() == btnLexico) {
            analizadorLexico.analizar(txtCode.getText());
            txtTokens.setText(analizadorLexico.obtenerStringTokens());
            txtTablaSimbolos.setText(analizadorLexico.obtenerStringTablaSimbolos());
            idBotonActual = 0;
            if(analizadorLexico.exito()) {
                lblLexicoStatus.setBackground(Color.green);
                btnSintactico.setEnabled(true);
            }else {
                lblLexicoStatus.setBackground(Color.red);
            }
            revalidate();
            repaint();
        }
        if(e.getSource() == btnSintactico) {
            if(idBotonActual != 0) {
                return;
            }
            if(analizadorSintactico.analizar(analizadorLexico.getTokens())) {
                lblSintacticoStatus.setBackground(Color.green);
            }else {
                lblSintacticoStatus.setBackground(Color.red);
                btnSintactico.setEnabled(false);
            }
        }
        if(e.getSource() == btnGuardarCodigo) {
            String codigo = txtCode.getText();
            fileChooser.showSaveDialog(null);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            File archivo = fileChooser.getSelectedFile();

            FileWriter writer;
            try {
                writer = new FileWriter(archivo,true);
                writer.write(codigo);
                writer.close();
            }catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null,"Error al guardar, poner nombre al archivo");
            }catch(IOException ex) {
                JOptionPane.showMessageDialog(null,"Error al guardar, en la salida");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        lblLexicoStatus.setBackground(Color.GRAY);
        idBotonActual = -1;
        btnSintactico.setEnabled(false);
        lblSintacticoStatus.setBackground(Color.GRAY);
        revalidate();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
