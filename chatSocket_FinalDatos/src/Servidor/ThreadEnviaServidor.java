package Servidor;

import java.io.IOException;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.SocketException;

public class ThreadEnviaServidor implements Runnable {
    private final VistaServidor main; 
    private DataOutputStream salida;
    private String mensaje;
    private final Socket conexion; 
   
    /**
     * Constructor vacio
     */
    public ThreadEnviaServidor(){
        this.conexion = null;
        this.main = null;
    }
    
    /**
     * Contructor
     * @param conexion
     * @param main 
     */
    public ThreadEnviaServidor(Socket conexion, final VistaServidor main){
        this.conexion = conexion;
        this.main = main;
        
        /**
         * Evento que ocurre cuando se pulsa el boton validar
         */
        VistaServidor.btnValidar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Crea el mensaje que va a enviar
                mensaje = "Validado: " + VistaServidor.textArea2.getSelectedValue();
                // Se llama al metodo que envia el mensaje
                enviarDatos(mensaje);
            }
        });
        
        /**
         * Evento que ocurre cuando se pulsa el boton rechazar
         */
        VistaServidor.btnRechazar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // Crea el mensaje que va a enviar
                mensaje = "Rechazado: " + VistaServidor.textArea2.getSelectedValue();
                
                // Se llama al metodo que envia el mensaje
                enviarDatos(mensaje);
            }
        });
    } 

    @Override
    public void run() {
         try {
            // Abro un flujo de salida de datos hacia el cliente
            salida = new DataOutputStream(conexion.getOutputStream());
        } catch (SocketException | NullPointerException ex) {
            System.out.println("ERROR: " + ex);
        } catch (IOException e) {
            System.out.println("ERROR: " + e);
        }
    }   
   
    /**
    * Metodo que envia el mensaje
    * @param mensaje 
    */
    private void enviarDatos(String mensaje){
        try {
            // Escribe el mensaje
            salida.writeUTF(mensaje);
            
            // Salida de datos al cliente
            salida.flush(); 
        }catch (IOException e){ 
            System.out.println("Error escribiendo Mensaje: " + e);
        }
   }
} 
