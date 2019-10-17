package cz.mzk.integrity.kramerius_api;

import java.util.List;

/**
 * Created by holmanj on 16.11.15.
 */
public class Process {

    String uuid; // bez předpony "uuid:"
    String pid; // interní pid krameria
    String def; // typ procesu
    String state;
    String batchState;
    String name;
    String started;
    String planned;
    String finished;
    String userid;
    String userFirstname;
    String userSurname;
    List<Process> children;
    String logUrl;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBatchState() {
        return batchState;
    }

    public void setBatchState(String batchState) {
        this.batchState = batchState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarted() {
        return started;
    }

    public void setStarted(String started) {
        this.started = started;
    }

    public String getPlanned() {
        return planned;
    }

    public void setPlanned(String planned) {
        this.planned = planned;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserFirstname() {
        return userFirstname;
    }

    public void setUserFirstname(String userFirstname) {
        this.userFirstname = userFirstname;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public List<Process> getChildren() {
        return children;
    }

    public void setChildren(List<Process> children) {
        this.children = children;
    }

    public void generateLogUrl(String krameriusUrl) {
        this.logUrl = "http://" + krameriusUrl + "/search/inc/admin/_processes_outputs.jsp?uuid=" + this.uuid;
        if (this.children != null && !this.children.isEmpty()) {
            this.children.forEach(ch -> ch.generateLogUrl(krameriusUrl));
        }
    }

    @Override
    public String toString() {
        return "Process{" +
                "uuid='" + uuid + '\'' +
                ", pid='" + pid + '\'' +
                ", def='" + def + '\'' +
                ", state='" + state + '\'' +
                ", batchState='" + batchState + '\'' +
                ", name='" + name + '\'' +
                ", started='" + started + '\'' +
                ", planned='" + planned + '\'' +
                ", finished='" + finished + '\'' +
                ", userid='" + userid + '\'' +
                ", userFirstname='" + userFirstname + '\'' +
                ", userSurname='" + userSurname + '\'' +
                ", children=" + children +
                '}';
    }
}
