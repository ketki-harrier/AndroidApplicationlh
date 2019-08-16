package com.lifecyclehealth.lifecyclehealth.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ColorCode {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("visualBrandingPreferences")
    private ColorDetails visualBrandingPreferences ;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ColorDetails getVisualBrandingPreferences() {
        return visualBrandingPreferences;
    }

    public void setVisualBrandingPreferences(ColorDetails visualBrandingPreferences) {
        this.visualBrandingPreferences = visualBrandingPreferences;
    }

    @Override
    public String toString() {
        return "ColorCode{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", visualBrandingPreferences=" + visualBrandingPreferences +
                '}';
    }

    public class ColorDetails {

        @SerializedName("Brand_Icon")
        private String BrandIcon;

        @SerializedName("Brand_Logo")
        private String BrandLogo;

        @SerializedName("Color_Preference")
        private String ColorPreference;

        @SerializedName("Comm_Closing_Name")
        private String CommClosingName;

        @SerializedName("Primary_Logo_File_Name")
        private String PrimaryLogoFileName;

        @SerializedName("Secondary_Logo_File_Name")
        private String SecondaryLogoFileName;

        public String getBrandIcon() {
            return BrandIcon;
        }

        public void setBrandIcon(String brandIcon) {
            BrandIcon = brandIcon;
        }

        public String getBrandLogo() {
            return BrandLogo;
        }

        public void setBrandLogo(String brandLogo) {
            BrandLogo = brandLogo;
        }

        public String getColorPreference() {
            return ColorPreference;
        }

        public void setColorPreference(String colorPreference) {
            ColorPreference = colorPreference;
        }

        public String getCommClosingName() {
            return CommClosingName;
        }

        public void setCommClosingName(String commClosingName) {
            CommClosingName = commClosingName;
        }

        public String getPrimaryLogoFileName() {
            return PrimaryLogoFileName;
        }

        public void setPrimaryLogoFileName(String primaryLogoFileName) {
            PrimaryLogoFileName = primaryLogoFileName;
        }

        public String getSecondaryLogoFileName() {
            return SecondaryLogoFileName;
        }

        public void setSecondaryLogoFileName(String secondaryLogoFileName) {
            SecondaryLogoFileName = secondaryLogoFileName;
        }

        @Override
        public String toString() {
            return "ColorDetails{" +
                    "BrandIcon='" + BrandIcon + '\'' +
                    ", BrandLogo='" + BrandLogo + '\'' +
                    ", ColorPreference='" + ColorPreference + '\'' +
                    ", CommClosingName='" + CommClosingName + '\'' +
                    ", PrimaryLogoFileName='" + PrimaryLogoFileName + '\'' +
                    ", SecondaryLogoFileName='" + SecondaryLogoFileName + '\'' +
                    '}';
        }
    }
}
