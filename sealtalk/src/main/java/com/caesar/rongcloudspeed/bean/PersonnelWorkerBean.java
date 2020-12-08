package com.caesar.rongcloudspeed.bean;

import java.util.List;

/**
 * Created by mac on 2018/4/5.
 */

public class PersonnelWorkerBean  {


    private List<RecruitJobBean> jobList;

    private List<RecruitItemBean> personnelList;

    private List<PeopleItemBean> peopleList;

    public List<PeopleItemBean> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(List<PeopleItemBean> peopleList) {
        this.peopleList = peopleList;
    }

    public List<RecruitJobBean> getJobList() {
        return jobList;
    }

    public void setJobList(List<RecruitJobBean> jobList) {
        this.jobList = jobList;
    }

    public List<RecruitItemBean> getPersonnelList() {
        return personnelList;
    }

    public void setPersonnelList(List<RecruitItemBean> personnelList) {
        this.personnelList = personnelList;
    }
}

