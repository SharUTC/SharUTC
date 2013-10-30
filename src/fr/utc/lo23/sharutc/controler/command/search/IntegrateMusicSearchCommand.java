package fr.utc.lo23.sharutc.controler.command.search;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Catalog;

/**
 *
 */
public interface IntegrateMusicSearchCommand extends Command {

    /**
     *
     * @return
     */
 //   public Catalog getResultsCatalog();

    /**
     *
     * @param catalog
     */
    public void setResultsCatalog(Catalog catalog);
}
