package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.TagMap;

/**
 *
 */
public interface IntegrateRemoteTagMapCommand extends Command {

    /**
     *
     * @return
     */
    public TagMap getTagMap();

    /**
     *
     * @param tagMap
     */
    public void setTagMap(TagMap tagMap);
}
