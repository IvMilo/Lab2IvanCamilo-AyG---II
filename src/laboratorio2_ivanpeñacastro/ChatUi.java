package laboratorio2_ivanpeñacastro;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

/**
 *
 * @author IdkBu
 */
public class ChatUi extends javax.swing.JFrame {
    
    // Variables para manejar la conversación y el historial
    private final ArrayList<String> historialConversaciones = new ArrayList<>();
    private final ArrayList<String> conversacionActual = new ArrayList<>();
    
    public ChatUi() {
        initComponents();
        setIconImage(new ImageIcon(getClass().getResource("/logo/logo.png")).getImage());
        initializeModels();
    }

    private void initializeModels() {
        Historial.setModel(new DefaultListModel<>());
        Conversacion.setModel(new DefaultListModel<>());
    }
    
    private int indiceConversacionActual = -1;
    
    private void agregarAlHistorial() {
        String pregunta = Pregunta.getText().trim();
        if (!conversacionActual.isEmpty()) {
            // Convertir la conversación actual en un único String para agregar al historial
            StringBuilder conversacionCompleta = new StringBuilder();
            for (String mensaje : conversacionActual) {
                conversacionCompleta.append(mensaje).append("\n");
            }
            historialConversaciones.add(conversacionCompleta.toString());

            // Actualizar la lista de historial en la interfaz
            DefaultListModel<String> modeloHistorial = (DefaultListModel<String>) Historial.getModel();
            modeloHistorial.addElement(pregunta);
            Historial.setModel(modeloHistorial);

            // Limpiar la conversación actual para el nuevo chat
            limpiarConversacion();
        }
    }
    
    // Método para guardar cambios en la conversación actual
    private void guardarCambiosConversacionActual() {
        if (indiceConversacionActual != -1 && !conversacionActual.isEmpty()) {
            // Convertir la conversación actual en un único String para actualizar en el historial
            StringBuilder conversacionCompleta = new StringBuilder();
            for (String mensaje : conversacionActual) {
            conversacionCompleta.append(mensaje).append("\n");
            }
        // Actualizar la conversación en el historial
        historialConversaciones.set(indiceConversacionActual, conversacionCompleta.toString());
        }
    }
    
    private void limpiarConversacion(){
        conversacionActual.clear();
        DefaultListModel<String> modeloConversacion = (DefaultListModel<String>) Conversacion.getModel();
        modeloConversacion.clear();
        Conversacion.setModel(modeloConversacion);
    }
    
    
    // Método para mostrar una conversación del historial al hacer click.
    private void mostrarConversacionHistorial() {
        int indiceSeleccionado = Historial.getSelectedIndex();
        if (indiceSeleccionado != -1) {
            guardarCambiosConversacionActual();
            limpiarConversacion();
            indiceConversacionActual = indiceSeleccionado;
            String conversacionSeleccionada = historialConversaciones.get(indiceSeleccionado);
            DefaultListModel<String> modeloConversacion = (DefaultListModel<String>) Conversacion.getModel();
            modeloConversacion.clear();

            // Dividir la conversación seleccionada y mostrarla en la interfaz
            modeloConversacion.clear();
            conversacionActual.clear();
            String[] lineasConversacion = conversacionSeleccionada.split("\n");
            for (String linea : lineasConversacion) {
                modeloConversacion.addElement(linea);
                conversacionActual.add(linea);
            }
            Conversacion.setModel(modeloConversacion);
        }
    }    
    
    // Método para enviar la pregunta al chatbot
    private void enviarPregunta() {
        String pregunta = Pregunta.getText().trim();
        if (pregunta.isEmpty()) {
            mostrarMensaje("Por favor, ingrese una pregunta.", "Advertencia");
            return;
        }
        agregarConversacion("Tú: " + pregunta);
        solicitarRespuestaApi(pregunta);
    }

    // Método para realizar la solicitud a la API de Ollama
    private void solicitarRespuestaApi(String pregunta) {
        String modelo = "gemma2:2b";
        String url = "http://localhost:11434/api/generate";
        try {
            URL urlObject = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String jsonInputString = String.format("{\"model\": \"%s\", \"prompt\":\"%s\", \"stream\": false}", modelo, pregunta);
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                procesarRespuesta(connection);
            } else {
                mostrarMensaje("Error de conexión: Código " + responseCode, "Error");
            }
        } catch (IOException e) {
            mostrarMensaje("Error al conectar con la API: " + e.getMessage(), "Error");
        }
    }

    // Método para procesar la respuesta de la API
    private void procesarRespuesta(HttpURLConnection connection) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            JSONObject jsonObject = new JSONObject(response.toString());
            String textoRespuesta = jsonObject.getString("response");
            agregarConversacion("Ollama: " + textoRespuesta);
        } catch (Exception e) {
            mostrarMensaje("Error al procesar la respuesta de la API.", "Error");
        }
    }

    // Método para agregar texto a la conversación actual
    private void agregarConversacion(String texto) {
        DefaultListModel<String> modeloConversacion = (DefaultListModel<String>) Conversacion.getModel();
        if (!conversacionActual.contains(texto)){
            conversacionActual.add(texto);
            modeloConversacion.addElement(texto);
            Conversacion.setModel(modeloConversacion);
        }
    }

    // Método para mostrar mensajes al usuario
    private void mostrarMensaje(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Pregunta = new javax.swing.JTextField();
        Enviar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Historial = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        Conversacion = new javax.swing.JList<>();
        NewChat = new javax.swing.JButton();
        Limpiar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        Pregunta.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        Pregunta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreguntaActionPerformed(evt);
            }
        });

        Enviar.setBackground(new java.awt.Color(204, 204, 255));
        Enviar.setFont(new java.awt.Font("Stencil", 0, 12)); // NOI18N
        Enviar.setText("Enviar");
        Enviar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EnviarMouseClicked(evt);
            }
        });
        Enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EnviarActionPerformed(evt);
            }
        });

        Historial.setFont(new java.awt.Font("Stencil", 0, 12)); // NOI18N
        Historial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                HistorialMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Historial);

        jScrollPane2.setViewportView(Conversacion);

        NewChat.setBackground(new java.awt.Color(204, 204, 255));
        NewChat.setFont(new java.awt.Font("Stencil", 0, 12)); // NOI18N
        NewChat.setText("Chat+");
        NewChat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                NewChatMouseClicked(evt);
            }
        });
        NewChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewChatActionPerformed(evt);
            }
        });

        Limpiar.setBackground(new java.awt.Color(204, 204, 255));
        Limpiar.setFont(new java.awt.Font("Stencil", 0, 12)); // NOI18N
        Limpiar.setText("Limpiar");
        Limpiar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LimpiarMouseClicked(evt);
            }
        });
        Limpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LimpiarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Stencil", 0, 32)); // NOI18N
        jLabel1.setText("Chat");

        jLabel2.setFont(new java.awt.Font("Stencil", 0, 18)); // NOI18N
        jLabel2.setText("Historial");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Limpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(Pregunta, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel1)
                            .addGap(77, 77, 77)
                            .addComponent(NewChat))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Enviar)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NewChat)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Pregunta)
                    .addComponent(Enviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(62, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Limpiar)
                .addGap(30, 30, 30))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void EnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EnviarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EnviarActionPerformed

    private void EnviarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EnviarMouseClicked
        enviarPregunta();
    }//GEN-LAST:event_EnviarMouseClicked

    private void NewChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewChatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NewChatActionPerformed

    private void NewChatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NewChatMouseClicked
        agregarAlHistorial();
        guardarCambiosConversacionActual();
        mostrarMensaje("Nuevo chat iniciado.", "Información");
    }//GEN-LAST:event_NewChatMouseClicked

    private void HistorialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HistorialMouseClicked
        mostrarConversacionHistorial();
    }//GEN-LAST:event_HistorialMouseClicked

    
    private void PreguntaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PreguntaActionPerformed
        // TODO add your handling code here:
        EnviarMouseClicked(null);
    }//GEN-LAST:event_PreguntaActionPerformed

    private void LimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LimpiarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LimpiarActionPerformed

    private void LimpiarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LimpiarMouseClicked
        historialConversaciones.clear();
        DefaultListModel<String> modeloHistorial = (DefaultListModel<String>) Historial.getModel();
        modeloHistorial.clear();
        Historial.setModel(modeloHistorial);
        mostrarMensaje("El historial ha sido borrado.", "Información");
    }//GEN-LAST:event_LimpiarMouseClicked

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> Conversacion;
    private javax.swing.JButton Enviar;
    private javax.swing.JList<String> Historial;
    private javax.swing.JButton Limpiar;
    private javax.swing.JButton NewChat;
    private javax.swing.JTextField Pregunta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

}

