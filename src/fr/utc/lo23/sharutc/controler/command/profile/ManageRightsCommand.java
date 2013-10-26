package fr.utc.lo23.sharutc.controler.command.profile;

import fr.utc.lo23.sharutc.controler.command.Command;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.userdata.Category;

/**
 *
 */
public interface ManageRightsCommand extends Command {

    /**
     *
     * @return
     */
    public Category getCategory();

    /**
     *
     * @param category
     */
    public void setCategory(Category category);

    /**
     *
     * @return
     */
    public Music getMusic();

    /**
     *
     * @param music
     */
    public void setMusic(Music music);

    /**
     *
     * @return
     */
    public boolean getMayReadInfo();

    /**
     *
     * @return
     */
    public boolean getMayListen();

    /**
     *
     * @return
     */
    public boolean getMayCommentAndScore();

    /**
     *
     * @param mayReadInfo
     */
    public void setMayReadInfo(boolean mayReadInfo);

    /**
     *
     * @param mayListen
     */
    public void setMayListen(boolean mayListen);

    /**
     *
     * @param mayCommentAndScore
     */
    public void setMayCommentAndScore(boolean mayCommentAndScore);
}
