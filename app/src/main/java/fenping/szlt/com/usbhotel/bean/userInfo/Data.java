/**
  * Copyright 2021 json.cn 
  */
package fenping.szlt.com.usbhotel.bean.userInfo;

/**
 * Auto-generated: 2021-04-11 11:33:36
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Data {

    private int id;
    private String name;
    private String nickname;
    private String head_image;
    private String gold_bean;
    private String lock_gold_bean;
    private String silver_bean;
    private String state;
    private String created_at;
    private String deleted_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getGold_bean() {
        return gold_bean;
    }

    public void setGold_bean(String gold_bean) {
        this.gold_bean = gold_bean;
    }

    public String getLock_gold_bean() {
        return lock_gold_bean;
    }

    public void setLock_gold_bean(String lock_gold_bean) {
        this.lock_gold_bean = lock_gold_bean;
    }

    public String getSilver_bean() {
        return silver_bean;
    }

    public void setSilver_bean(String silver_bean) {
        this.silver_bean = silver_bean;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", head_image='" + head_image + '\'' +
                ", gold_bean=" + gold_bean +
                ", lock_gold_bean='" + lock_gold_bean + '\'' +
                ", silver_bean='" + silver_bean + '\'' +
                ", state='" + state + '\'' +
                ", created_at='" + created_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                '}';
    }
}