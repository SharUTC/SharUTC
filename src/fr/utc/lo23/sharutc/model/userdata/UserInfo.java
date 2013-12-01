package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import java.io.File;
import java.io.Serializable;

/**
 * Represents user's information
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = -5092432482763935439L;
    private Long mPeerId;
    private String mLogin;
    @JsonInclude(NON_NULL)
    private String mPassword;
    private String mFirstName;
    private String mLastName;
    private Integer mAge;
    private File mAvatarFile;

    /**
     * Default constructor
     */
    public UserInfo() {
    }

    /**
     * Constructor - create a UserInfo with an id and a login
     *
     * @param id peer's id
     * @param login peer's login
     */
    public UserInfo(long id, String login) {
        this.mPeerId = id;
        this.mLogin = login;
    }

    /**
     * Return user's id
     *
     * @return user's id
     */
    public Long getPeerId() {
        return mPeerId;
    }

    /**
     * Set user's id
     *
     * @param peerId
     */
    public void setPeerId(Long peerId) {
        this.mPeerId = peerId;
    }

    /**
     * Return user's login
     *
     * @return user's login
     */
    public String getLogin() {
        return mLogin;
    }

    /**
     * Set user's login
     *
     * @param login
     */
    public void setLogin(String login) {
        this.mLogin = login;
    }

    /**
     * Return user's password
     *
     * @return user's password
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * Set user's password
     *
     * @param password
     */
    public void setPassword(String password) {
        this.mPassword = password;
    }

    /**
     * Return user's first name
     *
     * @return user's first name
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * Set user's first name
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    /**
     * Return user's last name
     *
     * @return user's last name
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * Set user's last name
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    /**
     * Return user's age
     *
     * @return user's age
     */
    public Integer getAge() {
        return mAge;
    }

    /**
     * Set user's age
     *
     * @param age
     */
    public void setAge(Integer age) {
        this.mAge = age;
    }

    /**
     * Return user's avatar File
     *
     * @return
     */
    public File getAvatarFile() {
        return mAvatarFile;
    }

    /**
     * Set user's avatar File
     *
     * @param avatarFile
     */
    public void setAvatarFile(File avatarFile) {
        this.mAvatarFile = avatarFile;
    }

    /**
     * Override the method toString
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "UserInfo{" + "mPeerId=" + mPeerId + ", mLogin=" + mLogin + ", mPassword=" + mPassword + ", mFirstName=" + mFirstName + ", mLastName=" + mLastName + ", mAge=" + mAge + ", mAvatarFile=" + mAvatarFile + '}';
    }

    /**
     * Create a new peer from user's peerId and user's login
     *
     * @return the new peer
     */
    public Peer toPeer() {
        return new Peer(mPeerId, mLogin);
    }

    @Override
    public UserInfo clone() {
        UserInfo userInfo = new UserInfo(this.mPeerId, this.mLogin);
        userInfo.setAge(mAge);
        userInfo.setAvatarFile(mAvatarFile);
        userInfo.setFirstName(mFirstName);
        userInfo.setLastName(mLastName);
        userInfo.setPassword(mPassword);
        return userInfo;
    }
}
