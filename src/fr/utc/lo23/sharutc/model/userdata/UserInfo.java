package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import java.io.File;
import java.io.Serializable;
import java.security.MessageDigest;
import org.slf4j.LoggerFactory;

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
    
    @Override
    public boolean equals(Object obj){
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final UserInfo userInfo = (UserInfo) obj;
        boolean avatarPath = true;
        if(mAvatarFile != null)
            avatarPath = mAvatarFile.getPath().equals(userInfo.mAvatarFile.getPath());
        return (mPeerId.equals(userInfo.mPeerId) 
                && mLogin.equals(userInfo.mLogin)
                && mPassword.equals(userInfo.mPassword)
                && mFirstName.equals(userInfo.mFirstName)
                && mLastName.equals(userInfo.mLastName)
                && mAge.equals(userInfo.mAge)
                && avatarPath);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.mPeerId != null ? this.mPeerId.hashCode() : 0);
        hash = 97 * hash + (this.mLogin != null ? this.mLogin.hashCode() : 0);
        hash = 97 * hash + (this.mPassword != null ? this.mPassword.hashCode() : 0);
        hash = 97 * hash + (this.mFirstName != null ? this.mFirstName.hashCode() : 0);
        hash = 97 * hash + (this.mLastName != null ? this.mLastName.hashCode() : 0);
        hash = 97 * hash + (this.mAge != null ? this.mAge.hashCode() : 0);
        hash = 97 * hash + (this.mAvatarFile != null ? this.mAvatarFile.hashCode() : 0);
        return hash;
    }
    
    /**
     * Return the SHA-1 of a string
     * 
     * @param s the string
     * @return the SHA-1 of s
     */
    public static String sha1(String s) {
        String sha1 = "";
        
        try {
            MessageDigest d = MessageDigest.getInstance("SHA-1");
            d.reset();
            d.update(s.getBytes());
            sha1 = byteArrayToHexString(d.digest());
        } catch (Exception ex) {
            LoggerFactory.getLogger(UserInfo.class).error("Error during creation of the SHA-1 of: " + s + " <> error: ", ex.toString());
        }
        
        return sha1;
    }
    
    /**
     * Convert a bite array to a string
     * 
     * @param b the bite array
     * @return the string corresponding to the byte array
     */
    private static String byteArrayToHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result +=
                    Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }
}
