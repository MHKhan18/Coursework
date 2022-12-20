package edu.stevens.cs548.owl;

import java.io.OutputStream;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.mgt.Explain;

import openllet.jena.PelletReasonerFactory;

public class Inferencer {
	
	protected boolean debug = false;
	
	protected String namespace;

	protected Model schema;

//	protected Model data;

	protected OntModel knowledgeBase;
	
	public interface Console {
		public void msgln(String s);
		public OutputStream getOutputStream();
	}
	
	public void toggleDebug() {
		debug = !debug;
	}

	public void namespace(String nsname) {
		namespace = nsname;
	}
	
	public void schema(Console console, String filename) {
		if (namespace == null) {
			console.msgln("Please define the namespace prefix first.");
			return;
		}
		String schemaFile = String.format("file:%s", filename);
		
	    // Create ontology model with reasoner support
		schema = RDFDataMgr.loadModel(schemaFile);
		OntModelSpec ontModelSpec = PelletReasonerFactory.THE_SPEC;
	    knowledgeBase = ModelFactory.createOntologyModel(ontModelSpec, schema);
	}

//	public void base(Console console, String filename) {
//		if (schema == null) {
//			console.msgln("Please load schema before loading knowledge base");
//			return;
//		}
//
//		String dataFile = String.format("file:%s", filename);
//		data = RDFDataMgr.loadModel(dataFile);
//
//		// Reasoner reasoner = ReasonerRegistry.getOWLReasoner().bindSchema(schema);
//		
//		OntModelSpec ontModelSpec = PelletReasonerFactory.THE_SPEC;
////		Reasoner reasoner = ontModelSpec.getReasoner().bindSchema(schema);
////		ontModelSpec.setReasoner(reasoner);
//	    knowledgeBase = ModelFactory.createOntologyModel(ontModelSpec, data);
//	}
	
	protected Resource getResource(String resname) {
		String name = String.format("%s%s", namespace, resname);
		return knowledgeBase.getResource(name);
	}
	
	protected OntClass getOntClass(String resname) {
		String name = String.format("%s%s", namespace, resname);
		return knowledgeBase.getOntClass(name);
	}
	
	protected Property getProperty(String resname) {
		String name = String.format("%s%s", namespace, resname);
		return knowledgeBase.getProperty(name);
	}
	
//	protected void props(Console console, Resource subject, Property property, Resource object) {
//		for (StmtIterator i = knowledgeBase.listStatements(subject, property, object); i.hasNext();) {
//			Statement stmt = i.nextStatement();
//			console.msgln(" - " + PrintUtil.print(stmt));
//		}		
//	}
//
//	public void props(Console console, String resName) {
//		Resource subject = this.getResource(resName);
//		props(console, subject, null, null);
//
//	}

//	public void prop(Console console, String subjName, String propName, String objName) {
//		Resource subject = this.getResource(subjName);
//		Property property = this.getProperty(propName);
//		Resource object = this.getResource(objName);
//		props(console, subject, property, object);
//	}
	
//	public boolean isa(String resName, String className) {
//		Resource resource = this.getResource(resName);
//		Resource clazz = this.getOntClass(className);
//		return knowledgeBase.contains(resource, RDF.type, clazz);
//	}
	
//	public void elems(Console console, String clzName) {
//		
//		OntClass clazz = getOntClass(clzName);
//		for (final ExtendedIterator<? extends OntResource> insts = clazz.listInstances(); insts.hasNext(); ) {
//			OntResource inst = insts.next();
//			console.msgln(" - " + inst.getURI());
//		}
//	}
	
	private String resourceName(String lname) {
		return String.format("<%s%s>",  namespace, lname);
	}
	
	public void allTriples(Console console) {
		String queryString = " SELECT ?subject ?prop ?object WHERE { ?subject ?prop ?object } ";
		Query query = QueryFactory.create(queryString);
		try (QueryExecution qexec = QueryExecutionFactory.create(query, knowledgeBase)) {
			if (debug) {
			    qexec.getContext().set(ARQ.symLogExec, Explain.InfoLevel.ALL) ;
			}
		    ResultSet results = qexec.execSelect() ;
		    ResultSetFormatter.out(console.getOutputStream(), results);
		}
	}
	
	public void props(Console console, String resName) {
		String resource = resourceName(resName);
		String queryString = String.format(" SELECT ?prop ?object WHERE { %s ?prop ?object } ", resource);
		Query query = QueryFactory.create(queryString);
		try (QueryExecution qexec = QueryExecutionFactory.create(query, knowledgeBase)) {
			if (debug) {
			    qexec.getContext().set(ARQ.symLogExec, Explain.InfoLevel.ALL) ;
			}
		    ResultSet results = qexec.execSelect() ;
		    ResultSetFormatter.out(console.getOutputStream(), results);
		}
	}
	
	public boolean isa(String resName, String className) {
		String resource = resourceName(resName);
		String clazz = resourceName(className);
		String queryString = String.format(" ASK { %s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> %s } ", resource, clazz);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, knowledgeBase);
		if (debug) {
		    qexec.getContext().set(ARQ.symLogExec, Explain.InfoLevel.ALL) ;
		}
		boolean result = qexec.execAsk();
		qexec.close();
		return result;
	}

	public void elems(Console console, String className) {
		String clazz = resourceName(className);
		String queryString = String.format(" SELECT ?subject WHERE { ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> %s } ", clazz);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, knowledgeBase);
		if (debug) {
		    qexec.getContext().set(ARQ.symLogExec, Explain.InfoLevel.ALL) ;
		}
	    ResultSet results = qexec.execSelect() ;
	    ResultSetFormatter.out(console.getOutputStream(), results);
	}
	
}
