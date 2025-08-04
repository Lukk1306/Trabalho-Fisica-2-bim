package com.mycompany.campomagnetico;

import javax.swing.*;
import java.awt.*;

public class CampoMagnetico {
    private JFrame frame;
    private JTextField distanciaField, correnteField, campoTerraField;
    private JTextArea outputArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CampoMagnetico().criarInterface());
    }

    private void criarInterface() {
        frame = new JFrame("Campo Magnético da Linha de Transmissão");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Centraliza a janela na tela

        JPanel principalPanel = new JPanel(new BorderLayout());

        // Painel central com GridBagLayout para centralizar
        JPanel centralPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        distanciaField = new JTextField(10);
        correnteField = new JTextField(10);
        campoTerraField = new JTextField(10);

        // Adiciona os campos
        adicionarLinha(centralPanel, gbc, 0, "Distância da bússola à linha (m):", distanciaField);
        adicionarLinha(centralPanel, gbc, 1, "Corrente na linha (A):", correnteField);
        adicionarLinha(centralPanel, gbc, 2, "Campo magnético da Terra (μT):", campoTerraField);

        // Botões
        JButton calcularButton = new JButton("Calcular");
        JButton limparButton = new JButton("Limpar");

        calcularButton.setPreferredSize(new Dimension(100, 25));
        limparButton.setPreferredSize(new Dimension(100, 25));

        JPanel botoesPanel = new JPanel();
        botoesPanel.add(calcularButton);
        botoesPanel.add(limparButton);

        calcularButton.addActionListener(e -> resolver());
        limparButton.addActionListener(e -> limparCampos());

        // Área de saída
        outputArea = new JTextArea(5, 40);
        outputArea.setEditable(false);
        outputArea.setMargin(new Insets(5, 5, 5, 5));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Monta o painel principal
        principalPanel.add(centralPanel, BorderLayout.CENTER);
        principalPanel.add(botoesPanel, BorderLayout.SOUTH);
        principalPanel.add(scrollPane, BorderLayout.NORTH);

        frame.getContentPane().add(principalPanel);
        frame.setVisible(true);
    }

    private void adicionarLinha(JPanel panel, GridBagConstraints gbc, int y, String labelText, JTextField textField) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    private void limparCampos() {
        distanciaField.setText("");
        correnteField.setText("");
        campoTerraField.setText("");
        outputArea.setText("");
    }

    private void resolver() {
        try {
            double distancia = Double.parseDouble(distanciaField.getText());
            double corrente = Double.parseDouble(correnteField.getText());
            double campoTerra = Double.parseDouble(campoTerraField.getText()); // em microtesla

            final double mu0 = 4 * Math.PI * 1e-7; // T·m/A

            double Blinha = (mu0 * corrente) / (2 * Math.PI * distancia); // em tesla
            double BlinhaMicroT = Blinha * 1e6; // μT

            StringBuilder resultado = new StringBuilder();
            resultado.append(String.format("(a) Campo magnético da linha: %.3f μT\n", BlinhaMicroT));

            if (BlinhaMicroT > campoTerra * 0.1) {
                resultado.append("(b) Influência significativa na leitura da bússola.");
            } else {
                resultado.append("(b) Influência desprezível na leitura da bússola.");
            }

            outputArea.setText(resultado.toString());

        } catch (NumberFormatException e) {
            outputArea.setText("Erro: Verifique se todos os valores foram preenchidos corretamente.");
        }
    }
}
