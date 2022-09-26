package edu.stevens.cs548.clinic.json.bind;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import edu.stevens.cs548.clinic.service.dto.DrugTreatmentDto;
import edu.stevens.cs548.clinic.service.dto.PhysiotherapyDto;
import edu.stevens.cs548.clinic.service.dto.RadiologyDto;
import edu.stevens.cs548.clinic.service.dto.SurgeryDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto.TreatmentType;

/**
 * Class for deserializing a treatment.
 * 
 * The gson.fromJson operation requires a specification of the (concrete) type of the 
 * object being deserialized, which of course is not known until we are reading the input.
 * We parse the data into a JsonElement object, read the tag on that, then deserialize 
 * the object into a Java object of the concrete class.
 * 
 * We cannot register this as custom deserialization logic in Gson because that would lead
 * to a cycle where attempting to deserialize a treatment causes the custom deserializer to 
 * trigger deserialization on the same object.
 * 
 * @author dduggan
 *
 */
public class TreatmentDeserializer {
	
	private Gson gson;
			
	private TypeAdapter<JsonElement> jsonElementAdapter;
	
	public static TreatmentDeserializer getTreatmentDeserializer(Gson gson) {
		return new TreatmentDeserializer(gson);
	}
	
	private TreatmentDeserializer(Gson gson) {
		this.gson = gson;
		this.jsonElementAdapter = gson.getAdapter(JsonElement.class);
	}
	
	/*
	 * Deserialization has go to in two stages: parse the JSON data to an untyped
	 * JSON object, then examine the type tag to see what kind of object it is,
	 * and build the concreteinstance of TreatmentDto.
	 */
	public TreatmentDto deserialize(JsonReader rd) throws JsonParseException, IOException {
		return deserialize(parse(rd));
	}
	
	/*
	 * Parse the input stream into an untyped JSON object.
	 */
	private JsonElement parse(JsonReader rd) throws JsonParseException, IOException {
		JsonElement json = jsonElementAdapter.read(rd);
		return json;
	}
	
	private String jsonElementToString(JsonElement jsonElement) throws JsonParseException{
		if (!jsonElement.isJsonPrimitive()) {
			throw new JsonParseException("jsonElement  of treatment is not primitive: "+jsonElement);
		}
		if (!jsonElement.getAsJsonPrimitive().isString()) {
			throw new JsonParseException("jsonElement of treatment is not string: "+jsonElement);
		}
		return jsonElement.getAsJsonPrimitive().getAsString();
	}

	private Float jsonElementToFloat(JsonElement jsonElement) throws JsonParseException{
		if (!jsonElement.isJsonPrimitive()) {
			throw new JsonParseException("jsonElement of treatment is not primitive: "+jsonElement);
		}
		if (!jsonElement.getAsJsonPrimitive().isNumber()) {
			throw new JsonParseException("jsonElement of treatment is not number: "+jsonElement);
		}
		return jsonElement.getAsJsonPrimitive().getAsFloat();
	}

	private Integer jsonElementToInt(JsonElement jsonElement) throws JsonParseException{
		if (!jsonElement.isJsonPrimitive()) {
			throw new JsonParseException("jsonElement of treatment is not primitive: "+jsonElement);
		}
		if (!jsonElement.getAsJsonPrimitive().isNumber()) {
			throw new JsonParseException("jsonElement of treatment is not number: "+jsonElement);
		}
		return jsonElement.getAsJsonPrimitive().getAsInt();
	}
	
	private ArrayList<LocalDate> getTreatmentDatesFromJson(JsonElement jsonElement) throws JsonParseException{
		
		ArrayList<LocalDate> dates = new ArrayList<>();

		if (!jsonElement.isJsonArray()){
			throw new JsonParseException("jsonElement treatment-dates of treatment is not an array: " + jsonElement);
		}

		JsonArray treatmentDates = jsonElement.getAsJsonArray();

		for (JsonElement date : treatmentDates){
			dates.add(gson.fromJson(date, LocalDate.class));
		}

		return dates;
	}

	private ArrayList<TreatmentDto> getTreatmentDtosFromJson(JsonElement jsonElement) throws JsonParseException{
		
		ArrayList<TreatmentDto> treatments = new ArrayList<>();

		JsonArray treatmentDates = jsonElement.getAsJsonArray();

		for (JsonElement treatment: treatmentDates){
			treatments.add(deserialize(treatment));
		}

		return treatments;
	}
	/*
	 * Deserialize an untyped JSON object into a treatment DTO.
	 */
	private TreatmentDto deserialize(JsonElement json)
			throws JsonParseException {

		if (!json.isJsonObject()) {
			throw new JsonParseException("Non-object in token stream where treatment DTO expected: "+json);
		}
		
		if (!json.getAsJsonObject().has(TreatmentDto.TYPE_TAG)) {
			throw new JsonParseException("Missing type tag for treatment DTO: "+json);
		}
		
		JsonElement typeElem = json.getAsJsonObject().get(TreatmentDto.TYPE_TAG);
		if (!typeElem.isJsonPrimitive()) {
			throw new JsonParseException("Type tag for treatment is not primitive: "+typeElem);
		}
		if (!typeElem.getAsJsonPrimitive().isString()) {
			throw new JsonParseException("Type tag for treatment is not a string: "+typeElem);
		}
		if (!TreatmentType.isValid(typeElem.getAsJsonPrimitive().getAsString())) {
			throw new JsonParseException("Type tag for treatment is not a valid tag value: "+typeElem);
		}
		
		TreatmentType typeTag = TreatmentType.parse(typeElem.getAsJsonPrimitive().getAsString());
		
		// TODO use the typeTag to parse the specific treatment subtype
		JsonObject record = json.getAsJsonObject();

		switch(typeTag){
			case DRUGTREATMENT:
				DrugTreatmentDto drugTreatmentdto = new TreatmentDtoFactory().createDrugTreatmentDto();
				drugTreatmentdto.setId(UUID.fromString(jsonElementToString(record.get("id"))));
				drugTreatmentdto.setPatientId(UUID.fromString(jsonElementToString(record.get("patient-id"))));
				drugTreatmentdto.setProviderId(UUID.fromString(jsonElementToString(record.get("provider-id"))));
				drugTreatmentdto.setDiagnosis(jsonElementToString(record.get("diagnosis")));
				
				drugTreatmentdto.setDrug(jsonElementToString(record.get("drug")));
				drugTreatmentdto.setDosage(jsonElementToFloat(record.get("dosage")));
				drugTreatmentdto.setFrequency(jsonElementToInt(record.get("frequency")));
				drugTreatmentdto.setStartDate(gson.fromJson(record.get("start-date"), LocalDate.class));
				drugTreatmentdto.setEndDate(gson.fromJson(record.get("end-date"), LocalDate.class));
				return drugTreatmentdto;
			
			case PHYSIOTHERAPY:
				PhysiotherapyDto physiotherapyDto = new TreatmentDtoFactory().createPhysiotherapyDto();
				physiotherapyDto.setId(UUID.fromString(jsonElementToString(record.get("id"))));
				physiotherapyDto.setPatientId(UUID.fromString(jsonElementToString(record.get("patient-id"))));
				physiotherapyDto.setProviderId(UUID.fromString(jsonElementToString(record.get("provider-id"))));
				physiotherapyDto.setDiagnosis(jsonElementToString(record.get("diagnosis")));

				physiotherapyDto.setTreatmentDates(getTreatmentDatesFromJson(record.get("treatment-dates")));
				
				return physiotherapyDto;
			
			case RADIOLOGY:
				RadiologyDto radiologyDto = new TreatmentDtoFactory().createRadiologyDto();
				radiologyDto.setId(UUID.fromString(jsonElementToString(record.get("id"))));
				radiologyDto.setPatientId(UUID.fromString(jsonElementToString(record.get("patient-id"))));
				radiologyDto.setProviderId(UUID.fromString(jsonElementToString(record.get("provider-id"))));
				radiologyDto.setDiagnosis(jsonElementToString(record.get("diagnosis")));
				
				radiologyDto.setTreatmentDates(getTreatmentDatesFromJson(record.get("treatment-dates")));
				radiologyDto.setFollowupTreatments(getTreatmentDtosFromJson(record.get("followup-treatments")));
				return radiologyDto;
			
			case SURGERY:
				SurgeryDto surgeryDto = new TreatmentDtoFactory().createSurgeryDto();
				surgeryDto.setId(UUID.fromString(jsonElementToString(record.get("id"))));
				surgeryDto.setPatientId(UUID.fromString(jsonElementToString(record.get("patient-id"))));
				surgeryDto.setProviderId(UUID.fromString(jsonElementToString(record.get("provider-id"))));
				surgeryDto.setDiagnosis(jsonElementToString(record.get("diagnosis")));

				surgeryDto.setSurgeryDate(gson.fromJson(record.get("surgery-date"), LocalDate.class));
				surgeryDto.setDischargeinstructions(jsonElementToString(record.get("discharge-instructions")));
				surgeryDto.setFollowupTreatments(getTreatmentDtosFromJson(record.get("followup-treatments")));
				return surgeryDto;
			
			default:
				throw new IllegalStateException("Unknown type tag");
			
		}

		
	}

	
}
