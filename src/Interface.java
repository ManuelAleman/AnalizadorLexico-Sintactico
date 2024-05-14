import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Interface extends JFrame implements ActionListener {
    private AnalizadorLexico analizadorLexico = new AnalizadorLexico();

    private JButton btnOpen;

    private JFileChooser fileChooser;
    private JButton btnLexico;

    private JButton btnSintactico;
    private JLabel lblLexicoStatus;

    private JLabel lblSintacticoStatus;

    private JTextArea txtCode;

    private JLabel lblConsoleInfo;

    private JTextArea txtTablaSimbolos;

    public Interface() {
        super("Automatas");

        setSize(1200, 1000);
        getContentPane().setBackground(new Color(251, 254, 187));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        fileChooser = new JFileChooser();
        makeInterface();
        addListeners();
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
        btnOpen.setBounds(950, 30, 150, 50);
        btnOpen.setBackground(new Color(192, 199, 200));
        btnOpen.setForeground(Color.BLACK);
        btnOpen.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnOpen.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(btnOpen);

        btnLexico = new JButton("Lexico");
        btnLexico.setBounds(950, 130, 150, 50);
        btnLexico.setBackground(new Color(192, 199, 200));
        btnLexico.setForeground(Color.BLACK);
        btnLexico.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnLexico.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(btnLexico);

        lblLexicoStatus = new JLabel();
        lblLexicoStatus.setBounds(1120, 135, 40, 40);
        lblLexicoStatus.setOpaque(true);
        lblLexicoStatus.setBackground(Color.GRAY);
        add(lblLexicoStatus);

        btnSintactico = new JButton("Sintactico");
        btnSintactico.setBounds(950, 200, 150, 50);
        btnSintactico.setBackground(new Color(192, 199, 200));
        btnSintactico.setForeground(Color.BLACK);
        btnSintactico.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnSintactico.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(btnSintactico);

        lblSintacticoStatus = new JLabel();
        lblSintacticoStatus.setBounds(1120, 205, 40, 40);
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


        JPanel tree = new JPanel();
        tree.setLayout(null);
        tree.setBounds(850, 300, 300, 650);
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
        btnLexico.addActionListener(this);
        btnSintactico.addActionListener(this);
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
                } catch (FileNotFoundException ex) {
                    txtCode.setText("Error al leer el archivo");
                    throw new RuntimeException(ex);
                }
            }
        }
        if (e.getSource() == btnLexico) {
            ArrayList<Token> tokensLeidos = analizadorLexico.analizar(txtCode.getText());
            StringBuilder sb = new StringBuilder();
            for (Token token : tokensLeidos) {
                sb.append(token);
                sb.append("\n");
            }
            txtTablaSimbolos.setText(sb.toString());
            revalidate();
            repaint();
        }
    }
}
