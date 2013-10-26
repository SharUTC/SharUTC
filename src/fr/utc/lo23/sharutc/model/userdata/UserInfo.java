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
    private Long peerId;
    private String login;
    @JsonInclude(NON_NULL)
    private String password;
    private String firstName;
    private String lastName;
    private Integer age;
    private File avatarFile;

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
        return peerId;
    }

    /**
     *
     * @param peerId
     */
    public void setPeerId(Long peerId) {
        this.peerId = peerId;
    }

    /**
     *
     * @return
     */
    public String getLogin() {
        return login;
    }

    /**
     *
     * @param login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return
     */
    public Integer getAge() {
        return age;
    }

    /**
     *
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     *
     * @return
     */
    public File getAvatarFile() {
        return avatarFile;
    }

    /**
     *
     * @param avatarFile
     */
    public void setAvatarFile(File avatarFile) {
        this.avatarFile = avatarFile;
    }
}
