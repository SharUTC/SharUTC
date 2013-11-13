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
}
