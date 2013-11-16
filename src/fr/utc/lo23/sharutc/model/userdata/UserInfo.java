package fr.utc.lo23.sharutc.model.userdata;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import java.io.File;
import java.io.Serializable;

/**
 *
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
     *
     */
    public UserInfo() {
    }

    /**
     *
     * @return
     */
    public Long getPeerId() {
        return mPeerId;
    }

    /**
     *
     * @param peerId
     */
    public void setPeerId(Long peerId) {
        this.mPeerId = peerId;
    }

    /**
     *
     * @return
     */
    public String getLogin() {
        return mLogin;
    }

    /**
     *
     * @param login
     */
    public void setLogin(String login) {
        this.mLogin = login;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.mPassword = password;
    }

    /**
     *
     * @return
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    /**
     *
     * @return
     */
    public Integer getAge() {
        return mAge;
    }

    /**
     *
     * @param age
     */
    public void setAge(Integer age) {
        this.mAge = age;
    }

    /**
     *
     * @return
     */
    public File getAvatarFile() {
        return mAvatarFile;
    }

    /**
     *
     * @param avatarFile
     */
    public void setAvatarFile(File avatarFile) {
        this.mAvatarFile = avatarFile;
    }

    
     /**
     * @param obj
     * 
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserInfo other = (UserInfo) obj;
        if (this.mPeerId != other.mPeerId && (this.mPeerId == null || !this.mPeerId.equals(other.mPeerId))) {
            return false;
        }
        if ((this.mLogin == null) ? (other.mLogin != null) : !this.mLogin.equals(other.mLogin)) {
            return false;
        }
        if ((this.mPassword == null) ? (other.mPassword != null) : !this.mPassword.equals(other.mPassword)) {
            return false;
        }
        if ((this.mFirstName == null) ? (other.mFirstName != null) : !this.mFirstName.equals(other.mFirstName)) {
            return false;
        }
        if ((this.mLastName == null) ? (other.mLastName != null) : !this.mLastName.equals(other.mLastName)) {
            return false;
        }
        if (this.mAge != other.mAge && (this.mAge == null || !this.mAge.equals(other.mAge))) {
            return false;
        }
        if (this.mAvatarFile != other.mAvatarFile && (this.mAvatarFile == null || !this.mAvatarFile.equals(other.mAvatarFile))) {
            return false;
        }
        return true;
    }

    
     /**
     * 
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.mPeerId != null ? this.mPeerId.hashCode() : 0);
        hash = 61 * hash + (this.mLogin != null ? this.mLogin.hashCode() : 0);
        hash = 61 * hash + (this.mPassword != null ? this.mPassword.hashCode() : 0);
        hash = 61 * hash + (this.mFirstName != null ? this.mFirstName.hashCode() : 0);
        hash = 61 * hash + (this.mLastName != null ? this.mLastName.hashCode() : 0);
        hash = 61 * hash + (this.mAge != null ? this.mAge.hashCode() : 0);
        hash = 61 * hash + (this.mAvatarFile != null ? this.mAvatarFile.hashCode() : 0);
        return hash;
    }

    
     /**
     * 
     * @return
     */
    @Override
    public String toString() {
        return "UserInfo{" + "mPeerId=" + mPeerId + ", mLogin=" + mLogin + ", mPassword=" + mPassword + ", mFirstName=" + mFirstName + ", mLastName=" + mLastName + ", mAge=" + mAge + ", mAvatarFile=" + mAvatarFile + '}';
    }
    

}
