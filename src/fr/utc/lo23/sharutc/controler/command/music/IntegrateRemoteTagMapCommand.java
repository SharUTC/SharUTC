package fr.utc.lo23.sharutc.controler.command.music;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.TagMap;

/**
 * Merge TagMap received from Peers
 */
public interface IntegrateRemoteTagMapCommand extends Command {

    /**
     * Return the TagMap received from a peer
     *
     * @return the TagMap received from a peer
     */
    public TagMap getTagMap();

    /**
     * Set the TagMap received from a peer
     *
     * @param tagMap the TagMap received from a peer
     */
    public void setTagMap(TagMap tagMap);
}
