package Servidor;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadRecibeServidor implements Runnable {
    private final VistaServidor main; // Frame del servidor
    private String mensaje; // Mensaje que recibe
    private DataInputStream entrada; // Flujo de entrada de objetos
    private final Socket cliente; // Socket del cliente
   
    /**
     * Constructor vacio
     */
    public ThreadRecibeServidor(){
        this.cliente = null;
        this.main = null;
    }
    
    /**
     * Constructor
     * @param cliente
     * @param main 
     */
    public ThreadRecibeServidor(Socket cliente, VistaServidor main){
        this.cliente = cliente;
        this.main = main;
    }  
   
    @Override
    public void run() {
        try {
            // Se abre un flujo de entrada de datos
            entrada = new DataInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ThreadRecibeServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Recorre hasta que se cierra el servidor
        do {
            // Intenta
            try {
                // Lee un nuevo mensaje
                mensaje = (String) entrada.readUTF();
                // Llama al metodo que te muestra el mensaje
                main.mostrarMensaje(mensaje);
            }catch (EOFException ex) {
                System.out.println("Fin de la conexion: " + ex);
            }catch (IOException ex) {
                Logger.getLogger(ThreadRecibeServidor.class.getName()).log(Level.SEVERE, null, ex);
            }         
        } while (true);
    } 
} 
