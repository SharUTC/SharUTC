/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.controler.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Amandine
 */
public class IntegrateMusicSearchCommandImpl implements IntegrateMusicSearchCommand {
  
     private static final Logger log = LoggerFactory
            .getLogger(IntegrateMusicSearchCommandImpl.class);
     private Catalog mCatalog;
     private final MusicService musicService;
     
     public IntegrateMusicSearchCommandImpl (MusicService musicService) {
         this.musicService = musicService;
                       }
     
     @Override
    public void setResultsCatalog(Catalog catalog) {
                
    }
    
   //   public Catalog getResultsCatalog(){}
    
    @Override
    public void execute () {
     log.info("IntegrateMusicSearchCommand ...");

     musicService.integrateMusicSearch(mCatalog);
     log.info("IntegrateMusicSearchCommandDONE");    
    }

}
