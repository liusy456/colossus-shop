package com.colossus.sso.credential;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apereo.cas.authentication.Credential;

import java.io.Serializable;

public class CustomTwoStepCredential implements Credential, Serializable {
    private static final long serialVersionUID = -1641484550307480114L;

    private String token;
    private String phone;

    public CustomTwoStepCredential() {
    }

    public CustomTwoStepCredential(String token) {
        this.token = token;
    }

    public String toString() {
        return (new ToStringBuilder(this, ToStringStyle.NO_FIELD_NAMES_STYLE)).append("token", this.token).toString();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CustomTwoStepCredential)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            CustomTwoStepCredential other = (CustomTwoStepCredential)obj;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(this.token, other.token);
            return builder.isEquals();
        }
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(97, 31);
        builder.append(this.token);
        return builder.toHashCode();
    }

    public String getId() {
        return this.token;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
