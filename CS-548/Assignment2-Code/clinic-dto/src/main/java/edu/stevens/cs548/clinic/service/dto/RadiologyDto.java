package edu.stevens.cs548.clinic.service.dto;

import java.time.LocalDate;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class RadiologyDto extends TreatmentDto{
    

    @SerializedName("treatment-dates")
    private ArrayList<LocalDate> treatmentDates;
    
    @SerializedName("followup-treatments")
    private ArrayList<TreatmentDto> followupTreatments;

    
    public RadiologyDto(){
        super(TreatmentType.RADIOLOGY);
    }

    public ArrayList<LocalDate> getTreatmentDates(){
        return this.treatmentDates;
    }

    public void setTreatmentDates(ArrayList<LocalDate> treatmentDates){
        this.treatmentDates = treatmentDates;
    }

    public ArrayList<TreatmentDto> getFollowupTreatments(){
        return this.followupTreatments;
    }

    public void setFollowupTreatments(ArrayList<TreatmentDto> followupTreatments){
        this.followupTreatments = followupTreatments;
    }
}
