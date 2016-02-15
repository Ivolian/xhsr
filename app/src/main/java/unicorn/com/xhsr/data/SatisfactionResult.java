package unicorn.com.xhsr.data;

import java.io.Serializable;
import java.util.List;

import unicorn.com.xhsr.data.greendao.SatisfactionOption;


public class SatisfactionResult implements Serializable {

     String phone;

     String username;

     String departmentId;

     long accessDate;

     String advice;

     List<SatisfactionOption> optionList;

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

    public long getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(long accessDate) {
        this.accessDate = accessDate;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public List<SatisfactionOption> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<SatisfactionOption> optionList) {
        this.optionList = optionList;
    }
}
