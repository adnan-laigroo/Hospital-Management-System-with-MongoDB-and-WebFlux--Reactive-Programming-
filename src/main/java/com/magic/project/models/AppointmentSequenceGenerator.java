package com.magic.project.models;
//package com.magic.demo.models;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.FindAndModifyOptions;
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AppointmentSequenceGenerator {
//	@Id
//	private String id;
//	private int sequence;
//
//	@Autowired
//	private MongoOperations mongoOperations;
//
//	public String getNextSequence(String sequenceName) {
//		Query query = new Query(Criteria.where("_id").is(sequenceName));
//		Update update = new Update().inc("sequence", 1);
//		FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);
//
//		AppointmentSequenceGenerator updatedSequence = mongoOperations.findAndModify(query, update, options,
//				AppointmentSequenceGenerator.class);
//		return "A" + String.format("%06d", updatedSequence.getSequence());
//	}
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public int getSequence() {
//		return sequence;
//	}
//
//	public void setSequence(int sequence) {
//		this.sequence = sequence;
//	}
//}
