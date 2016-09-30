package com.unisender.responses;

public class Warning {
    private int index;
    private String email;
    private String warning;

    public Warning(int index, String email, String warning) {
        this.index = index;
        this.email = email;
        this.warning = warning;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    @Override
    public String toString() {
        return "Warning{" +
                "index=" + index +
                ", email='" + email + '\'' +
                ", warning='" + warning + '\'' +
                '}';
    }
}
