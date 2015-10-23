package models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danielgek on 23/10/15.
 */
public class CurrentRewardsResult implements Serializable{
    int rewardsId;
    int rewardsIdProject;
    String rewardsDescription;
    double rewardsValue;
    int projectsId;
    int projectsIdAdmin;
    String projectsTitle;
    String projectsDescription;
    double projectsObjective;
    Date projectsLimite;
    boolean projectsSoftDeleted;
    int pledgesId;
    int pledgesIdProject ;
    int pledgesIdUser   ;
    double pledgesAmount ;

    public CurrentRewardsResult(int rewardsId, int rewardsIdProject, String rewardsDescription, double rewardsValue, int projectsId, int projectsIdAdmin, String projectsTitle, String projectsDescription, double projectsObjective, Date projectsLimite, boolean projectsSoftDeleted, int pledgesId, int pledgesIdProject, int pledgesIdUser, double pledgesAmount) {
        this.rewardsId = rewardsId;
        this.rewardsIdProject = rewardsIdProject;
        this.rewardsDescription = rewardsDescription;
        this.rewardsValue = rewardsValue;
        this.projectsId = projectsId;
        this.projectsIdAdmin = projectsIdAdmin;
        this.projectsTitle = projectsTitle;
        this.projectsDescription = projectsDescription;
        this.projectsObjective = projectsObjective;
        this.projectsLimite = projectsLimite;
        this.projectsSoftDeleted = projectsSoftDeleted;
        this.pledgesId = pledgesId;
        this.pledgesIdProject = pledgesIdProject;
        this.pledgesIdUser = pledgesIdUser;
        this.pledgesAmount = pledgesAmount;
    }

    public int getRewardsId() {
        return rewardsId;
    }

    public void setRewardsId(int rewardsId) {
        this.rewardsId = rewardsId;
    }

    public int getRewardsIdProject() {
        return rewardsIdProject;
    }

    public void setRewardsIdProject(int rewardsIdProject) {
        this.rewardsIdProject = rewardsIdProject;
    }

    public String getRewardsDescription() {
        return rewardsDescription;
    }

    public void setRewardsDescription(String rewardsDescription) {
        this.rewardsDescription = rewardsDescription;
    }

    public double getRewardsValue() {
        return rewardsValue;
    }

    public void setRewardsValue(double rewardsValue) {
        this.rewardsValue = rewardsValue;
    }

    public int getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(int projectsId) {
        this.projectsId = projectsId;
    }

    public int getProjectsIdAdmin() {
        return projectsIdAdmin;
    }

    public void setProjectsIdAdmin(int projectsIdAdmin) {
        this.projectsIdAdmin = projectsIdAdmin;
    }

    public String getProjectsTitle() {
        return projectsTitle;
    }

    public void setProjectsTitle(String projectsTitle) {
        this.projectsTitle = projectsTitle;
    }

    public String getProjectsDescription() {
        return projectsDescription;
    }

    public void setProjectsDescription(String projectsDescription) {
        this.projectsDescription = projectsDescription;
    }

    public double getProjectsObjective() {
        return projectsObjective;
    }

    public void setProjectsObjective(double projectsObjective) {
        this.projectsObjective = projectsObjective;
    }

    public Date getProjectsLimite() {
        return projectsLimite;
    }

    public void setProjectsLimite(Date projectsLimite) {
        this.projectsLimite = projectsLimite;
    }

    public boolean isProjectsSoftDeleted() {
        return projectsSoftDeleted;
    }

    public void setProjectsSoftDeleted(boolean projectsSoftDeleted) {
        this.projectsSoftDeleted = projectsSoftDeleted;
    }

    public int getPledgesId() {
        return pledgesId;
    }

    public void setPledgesId(int pledgesId) {
        this.pledgesId = pledgesId;
    }

    public int getPledgesIdProject() {
        return pledgesIdProject;
    }

    public void setPledgesIdProject(int pledgesIdProject) {
        this.pledgesIdProject = pledgesIdProject;
    }

    public int getPledgesIdUser() {
        return pledgesIdUser;
    }

    public void setPledgesIdUser(int pledgesIdUser) {
        this.pledgesIdUser = pledgesIdUser;
    }

    public double getPledgesAmount() {
        return pledgesAmount;
    }

    public void setPledgesAmount(double pledgesAmount) {
        this.pledgesAmount = pledgesAmount;
    }

    @Override
    public String toString() {
        return "CurrentRewardsResult{" +
                "rewardsId=" + rewardsId +
                ", rewardsIdProject=" + rewardsIdProject +
                ", rewardsDescription='" + rewardsDescription + '\'' +
                ", rewardsValue=" + rewardsValue +
                ", projectsId=" + projectsId +
                ", projectsIdAdmin=" + projectsIdAdmin +
                ", projectsTitle='" + projectsTitle + '\'' +
                ", projectsDescription='" + projectsDescription + '\'' +
                ", projectsObjective=" + projectsObjective +
                ", projectsLimite=" + projectsLimite +
                ", projectsSoftDeleted=" + projectsSoftDeleted +
                ", pledgesId=" + pledgesId +
                ", pledgesIdProject=" + pledgesIdProject +
                ", pledgesIdUser=" + pledgesIdUser +
                ", pledgesAmount=" + pledgesAmount +
                '}';
    }
}
