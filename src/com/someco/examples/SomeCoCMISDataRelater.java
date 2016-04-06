package com.someco.examples;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Relationship;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.OperationContextImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.enums.IncludeRelationships;

/**
 * This class takes two object IDs and associates them with an sc:relatedDocuments association.
 * This is a port of the SomeCoDataRelater class which uses the CMIS API. This example
 * shows how to relate two instances of custom content types via CMIS including how to dump the
 * relationships for an object.
 * 
 * @author jpotts
 */
public class SomeCoCMISDataRelater extends CMISExampleBase {

	private static final String USAGE = "java SomeCoCMISDataRelater <username> <password> <root folder> <doc|whitepaper> <content name> <source objectId> <target objectId>";
	private static String[] tab = {"admin","admin","SomeCo","whitepaper","test","2e8ff887-fd4e-4478-97aa-bbd120538fd7;1.0","60c008ba-d52f-4356-8764-0023ec49bfbe;1.0"}; 
	
    private String sourceObjectId;
    private String targetObjectId;

	public static void main(String[] args) throws Exception {
    	if (tab.length < 5) doUsage(SomeCoCMISDataRelater.USAGE);
    	SomeCoCMISDataRelater sccdr = new SomeCoCMISDataRelater();
    	sccdr.setUser(tab[0]);
    	sccdr.setPassword(tab[1]);
    	sccdr.setFolderName(tab[2]);
    	sccdr.setContentType(tab[3]);
    	sccdr.setContentName(tab[4]);
    	if (args.length == 7) {
    		sccdr.setSourceObjectId(tab[5]);
    		sccdr.setTargetObjectId(tab[6]);
    	}
    	sccdr.relate();
    }
    
    public void relate() throws Exception {
    	Session session = getSession();
    	String sourceObjectId = null;
    	String targetObjectId = null;
    	
    	if (getSourceObjectId() != null && getTargetObjectId() != null) {
    		sourceObjectId = getSourceObjectId();
    		targetObjectId = getTargetObjectId();
    	} else {
    		Document sourceDoc = createTestDoc(getContentName(), getContentType());
    		sourceObjectId = sourceDoc.getId();
    		
    		Document targetDoc = createTestDoc(getContentName(), getContentType());
    		targetObjectId = targetDoc.getId();    		
    	}

		// Create a Map of objects association type, source ID, and target ID
		Map <String, String> properties = new HashMap<String, String>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, "R:sc:relatedDocuments");
    	properties.put(PropertyIds.SOURCE_ID, sourceObjectId);
    	properties.put(PropertyIds.TARGET_ID, targetObjectId);
    	try {
    		session.createRelationship(properties);
    	} catch (Exception e) {
    		System.out.println("Oops, something unexpected happened. Maybe the rel already exists?");
    	}
    	
    	dumpAssociations(sourceObjectId);
    }

    public void dumpAssociations(String sourceObjectId) {
    	Session session = getSession();

    	// Dump the object's associations
        OperationContext oc = new OperationContextImpl();
        oc.setIncludeRelationships(IncludeRelationships.SOURCE);
    	Document sourceDoc = (Document) session.getObject(session.createObjectId(sourceObjectId), oc);
    	List<Relationship> relList = sourceDoc.getRelationships();
        System.out.println("Associations of objectId: " + sourceObjectId);
    	for (Relationship rel : relList) {
    		System.out.println("    " + rel.getTarget().getId());
    	}
    }

    public String getSourceObjectId() {
		return sourceObjectId;
	}

	public void setSourceObjectId(String sourceObjectId) {
		this.sourceObjectId = sourceObjectId;
	}

    public String getTargetObjectId() {
		return targetObjectId;
	}

	public void setTargetObjectId(String targetObjectId) {
		this.targetObjectId = targetObjectId;
	}

}
