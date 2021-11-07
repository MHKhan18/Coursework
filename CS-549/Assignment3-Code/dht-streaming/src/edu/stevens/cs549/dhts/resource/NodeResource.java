package edu.stevens.cs549.dhts.resource;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.sse.SseFeature;

import edu.stevens.cs549.dhts.activity.DHTBase.Invalid;

@Path("/dht")
public class NodeResource {

	/*
	 * Web service API.
	 * 
	 * TODO: Fill in the missing operations.
	 */

	Logger log = Logger.getLogger(NodeResource.class.getCanonicalName());

	@Context
	UriInfo uriInfo;

	@Context
	HttpHeaders headers;

	@GET
	@Path("info")
	@Produces("application/json")
	public Response getNodeInfo() {
		return new NodeService(headers, uriInfo).getNodeInfo();
	}

	@GET
	@Path("pred")
	@Produces("application/json")
	public Response getPred() {
		return new NodeService(headers, uriInfo).getPred();
	}

	@PUT
	@Path("notify")
	@Consumes("application/json")
	@Produces("application/json")
	/*
	 * Actually returns a TableRep
	 */
	public Response putNotify(TableRep predDb) {
		/*
		 * See the comment for WebClient::notify (the client side of this logic).
		 */
		return new NodeService(headers, uriInfo).notify(predDb);
	}

	@GET
	@Path("find")
	@Produces("application/json")
	public Response findSuccessor(@QueryParam("id") String index) {
		int id = Integer.parseInt(index);
		return new NodeService(headers, uriInfo).findSuccessor(id);
	}
	
	// ============================================================
	
	// GET /dht?key=KEY
	@GET
	@Produces("application/json")
	/*
	 * Retrieve the bindings for a (string) key at a node. [local search]
	 */
	public Response getBindings(@QueryParam("key") String key) throws Invalid {
		return new NodeService(headers , uriInfo).getBindings(key);
	}
	
	
	// PUT /dht?key=KEY&val=VAL
	/*
	 * Add a binding for a key at a node. [local]
	 */
	@PUT
	@Consumes("application/json")
	@Produces("application/json")
	public Response addBinding(@QueryParam("key") String key , @QueryParam("val") String val) throws Invalid{
		return new NodeService(headers , uriInfo).addBindings(key, val);
	}
	
	
	// DELETE /dht?key=KEY&val=VAL
	@DELETE
	@Consumes("application/json")
	@Produces("application/json")
	/*
	 * Delete the (first) binding for VAL under KEY [local]
	 */ 	
	public 	Response deleteBinding(@QueryParam("key") String key , @QueryParam("val") String val) throws Invalid {
		return new NodeService(headers , uriInfo).deleteBindings(key, val);
		
	}
	
	
	// GET /dht/succ
	@GET
	@Path("succ")
	@Produces("application/json")
	/*
	 * Get the successor for the node
	 */
	public Response getSucc() {
		return new NodeService(headers , uriInfo).getSucc();
	}
	
	// GET /dht/finger?id=ID
	@GET
	@Path("finger")
	@Produces("application/json")
	/*
	 * Get the closest preceding finger table entry for ID.
	 */
	public Response getFinger(@QueryParam("id") String index) {
		int id = Integer.parseInt(index);
		return new NodeService(headers , uriInfo).getFinger(id);
	}

	//===========================================================================
	//===========================================================================
	
	// TODO 
	
	// GET /dht/listen?id=ID&key=KEY
	
	/*
	 * Listen for notifications of new bindings.
	 */
	
	@GET
	@Path("listen")
	@Consumes("application/json")
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	public Response listenForBindings(@QueryParam("id") String nodeId , @QueryParam("key") String key) {
		int id = Integer.parseInt(nodeId);
		return new NodeService(headers , uriInfo).listenForBindings(id, key);
	}
	
	// TODO
	// DELETE /dht/listen?id=ID&key=KEY
	
	/*
	 * Stop event notifications for a node and key
	 */
	
	@DELETE
	@Path("listen")
	@Consumes("application/json")
	public Response listenOff(@QueryParam("id") String nodeId , @QueryParam("key") String key) {
		int id = Integer.parseInt(nodeId);
		return new NodeService(headers , uriInfo).listenOff(id , key);
	}


}
