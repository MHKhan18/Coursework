package edu.stevens.cs548.clinic.service.dto;

import java.time.LocalDate;
import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class PhysiotherapyDto extends TreatmentDto{

    @SerializedName("treatment-dates")
    private ArrayList<LocalDate> treatmentDates;
    
    public PhysiotherapyDto(){
        super(TreatmentType.PHYSIOTHERAPY);
    }

    public ArrayList<LocalDate> getTreatmentDates(){
        return this.treatmentDates;
    }

    public void setTreatmentDates(ArrayList<LocalDate> treatmentDates){
        this.treatmentDates = treatmentDates;
    }
}
