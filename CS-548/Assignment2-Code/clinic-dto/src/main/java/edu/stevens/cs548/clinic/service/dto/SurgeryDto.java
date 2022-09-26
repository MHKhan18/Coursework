package edu.stevens.cs548.clinic.service.dto;

import java.time.LocalDate;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class SurgeryDto extends TreatmentDto{
    
    @SerializedName("surgery-date")
    private LocalDate surgeryDate;

    @SerializedName("discharge-instructions")
    private String dischargeInstructions;

    @SerializedName("followup-treatments")
    private ArrayList<TreatmentDto> followupTreatments;

    public SurgeryDto(){
        super(TreatmentType.SURGERY);
    }

    public LocalDate getSurgeryDate(){
        return surgeryDate;
    }

    public void setSurgeryDate(LocalDate date){
        this.surgeryDate = date;
    }

    public String getDischargeInstructions(){
        return dischargeInstructions;
    }

    public void setDischargeinstructions(String instructions){
        this.dischargeInstructions = instructions;
    }

    public ArrayList<TreatmentDto> getFollowupTreatments(){
        return followupTreatments;
    }

    public void setFollowupTreatments(ArrayList<TreatmentDto> treatments){
        this.followupTreatments = treatments;
    }


}
