package org.example.dto;

public class SettingUpdateRequest {

    private Boolean isNotificationOn;

    private Boolean isDarkModeOn;

    private String selectedLanguage;

    // Getters and Setters
    public Boolean getIsNotificationOn() {
        return isNotificationOn;
    }

    public void setIsNotificationOn(Boolean isNotificationOn) {
        this.isNotificationOn = isNotificationOn;
    }

    public Boolean getIsDarkModeOn() {
        return isDarkModeOn;
    }

    public void setIsDarkModeOn(Boolean isDarkModeOn) {
        this.isDarkModeOn = isDarkModeOn;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }
}
