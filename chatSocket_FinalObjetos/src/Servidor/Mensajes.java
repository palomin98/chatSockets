/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

/**
 *
 * @author fp
 */
public class Mensajes {
    
    private String mensajeCliente;
    private int validacion; 
    
    public Mensajes(){
        mensajeCliente = "";
        validacion = 2;
    }
    
    public Mensajes(String mensajeCliente){
        this.mensajeCliente = mensajeCliente;
    }
    
    public String getMensajeCliente(){
        return mensajeCliente;
    }
    
    /**
     * Paso a String
     * @return 
     */
    @Override
    public String toString(){
        return ", Mensaje: " + mensajeCliente;
    }
}
