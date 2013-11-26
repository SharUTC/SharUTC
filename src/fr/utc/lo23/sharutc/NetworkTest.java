/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utc.lo23.sharutc;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.network.NetworkService;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import fr.utc.lo23.sharutc.controler.service.UserService;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.userdata.Peer;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Audrey
 */
public class NetworkTest {
    private static final Logger log = LoggerFactory
            .getLogger(SharUTC.class);
    @Inject
    private  static AppModel appModel;
   
    @Inject
    private static NetworkService networkService;
    
    public void NetworkTest(){
        
    }
    public static void initTest(){
        networkService.start();
    }
    
    public static void main() {
        initTest();
        System.out.print("initialisation \n");
        UserInfo userInfo = new UserInfo();
        
        userInfo.setLogin("login");
        userInfo.setPassword("password");
        userInfo.setPeerId(1L);
        userInfo.setFirstName("FirstName");
        userInfo.setLastName("LastName");
        userInfo.setAge(22);
        
        connection(userInfo);
        System.out.print("New your are connected with the login : login \n");
        System.out.println("Quelle action souhaitez vous effectuer ? \n");
        System.out.println("1) Envoyer un message broadcast \n");
        System.out.println("2) Envoyer un message unicast \n");
        System.out.println("3) Deconnexion \n");
        int recv = 2;
        try {
            recv = System.in.read();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(NetworkTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        while( recv  != 2){
            if(recv==1){
                networkService.sendBroadcastGetTagMap();
            } else {
                System.out.println("Veuillez saisir l'id du peer \n");
                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
                String str = "";
                try { 
                    str = keyboard.readLine();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(NetworkTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                networkService.sendUnicastGetCatalog(getPeer(Long.parseLong(str)));
            }
        }
        
        networkService.disconnectionBroadcast();
        
        networkService.stop();
        
    }
    
    public static void connection(UserInfo userInfo){
        
        networkService.connectionBroadcast(userInfo);
    }
    
    public static Peer getPeer(Long id){
        return appModel.getActivePeerList().getPeerByPeerId(id);
    }
}

