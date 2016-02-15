package unicorn.com.xhsr.satisfaction.model;

import java.io.Serializable;


public class SatisfactionResult implements Serializable {

    String phone;

    String username;

    String departmentId;

    long assessDate;

    String advice;

    //

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public long getAssessDate() {
        return assessDate;
    }

    public void setAssessDate(long assessDate) {
        this.assessDate = assessDate;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

}
