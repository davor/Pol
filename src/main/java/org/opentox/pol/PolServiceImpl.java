package org.opentox.pol;

import java.io.InputStream;
import java.util.Hashtable;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.opentox.pol.mysql.DbException;
import org.opentox.pol.xml.PolicyCreator;
import org.opentox.pol.xml.PolicyExtinguisher;
import org.opentox.pol.xml.PolicyReader;


/**
 * A Singleton class that uses an in-memory map to keep 
 * the pol objects. This way, we maintain the state of the 
 * resources in memory. 
 * 
 */
@Path("/opensso-pol")
public class PolServiceImpl implements PolService {

	
	final static String mime_text = "text/plain";
	
	private static PolServiceImpl instance = null;

	private PolServiceImpl() {
	}

	public synchronized static PolServiceImpl getInstance() {
		if(instance == null) {
			instance = new PolServiceImpl();
		}
		return instance;
	}
	public void log(String msg) {
		System.out.print(msg);
	}
	/**
	 * Create a Pol resource to respond to an HTTP request
	 * 
	 *     POST /opensso-pol
	 *         
	 *  @param uriInfo
	 *  @param Pol
	 *  @param subjectid
	 *  @return Response
	 */
	public Response createPol(@HeaderParam("subjectid") String subjectId, @Context UriInfo uriInfo, InputStream is) throws WebApplicationException {
		
		try {
			PolicyCreator helper = new PolicyCreator();
			return Response.ok(helper.createPolicy(subjectId, uriInfo, is)).type("text/xml").build(); 
		} catch (DbException x) {
			log(x.getMessage());
			throw new WebApplicationException(Response.status(500).entity(x.getMessage()).type(mime_text).build());
		} catch (RestException x) {
			log(x.getMessage());
			throw new WebApplicationException(Response.status(x.getHttpcode()).entity(x.getMessage()).type(mime_text).build());
		}			
	}	

	public Response checkPols(@HeaderParam("subjectid") String subjectId, @QueryParam("uris") String queryURIs) throws WebApplicationException {
	 	try {    
	 		Hashtable uris = new Hashtable();
		    uris.put("andi", new String("true"));
		    uris.put("david", new String("true"));
		    uris.put("micha", new String("false"));
		    uris.put("christoph", new String("true"));
		    
		    String[] query_uris_arr = queryURIs.split(",");
		    String query_result = "";
		    
		    for (String uri : query_uris_arr) {
		    	query_result = query_result + uris.get(uri) + ",";
		    }
		    int length = query_result.length();
		    query_result = query_result.substring(0, (length - 1));
		    
		   		    
		    return Response.ok(subjectId + "\n" + queryURIs + "\n" + query_result + "\n").type("text/html").build(); 
	} catch (Exception x) {
		throw new WebApplicationException(Response.status(500).entity(x.getMessage()).type(mime_text).build());
	}
					
	}
	

	/**
	 *  Return a Pol resource in response to an HTTP request
	 *  
	 *     GET /opensso-pol/{id}
	 *  
	 *  @param id
	 *  @return Response
	 */
	public Response getPolID(@HeaderParam("subjectid") String subjectId, @HeaderParam("id") String id, @HeaderParam("uri") String uri, @HeaderParam("polnames") String polnames)
	throws WebApplicationException {
		try {
			PolicyReader helper = new PolicyReader();
			return Response.ok(helper.getPolID(subjectId, id, uri, polnames)).type("text/xml").build(); 
		} catch (DbException x) {
			throw new WebApplicationException(Response.status(500).entity(x.getMessage()).type(mime_text).build());
		} catch (RestException x) {
			throw new WebApplicationException(Response.status(x.getHttpcode()).entity(x.getMessage()).type(mime_text).build());
		} catch (Exception x) {
			throw new WebApplicationException(Response.status(500).entity(x.getMessage()).type(mime_text).build());
		}	
	}
	
	/**
	 *  Hello "name"
	 *  
	 *     GET /opensso-pol/{id}
	 *  
	 *  @param id
	 *  @return Response
	 */
//	public Response getURIs(@HeaderParam("subjectid") String subjectId, @HeaderParam("uris") String headerURIs, @QueryParam("uris") String queryURIs)
	public Response getURIs(@QueryParam("uris") String queryURIs)
	throws WebApplicationException {
		try {
	    Hashtable uris = new Hashtable();
	    uris.put("andi", new String("true"));
	    uris.put("david", new String("true"));
	    uris.put("micha", new String("false"));
	    uris.put("christoph", new String("true"));
	    
	    String[] query_uris_arr = queryURIs.split(",");
	    String query_result = "";
	    
	    for (String uri : query_uris_arr) {
	    	query_result = query_result + uris.get(uri) + ",";
	    }
	    int length = query_result.length();
	    query_result = query_result.substring(0, (length - 1));
	    
//	    String[] uris_arr = headerURIs.split(",");
//	    String header_result = "";
//	    
//	    for (String uri : uris_arr) {
//	    	header_result = header_result + uris.get(uri) + ",";
//	    }
//	    length = header_result.length();
//	    header_result = header_result.substring(0, (length - 1));
	    
//	    return Response.ok(subjectId + "\n" + queryURIs + "\n" + query_result + "\n" +headerURIs + "\n" + header_result + "\n").type("text/html").build(); 
	    return Response.ok(queryURIs + "\n" + query_result + "\n").type("text/html").build(); 
		
		} catch (Exception x) {
			throw new WebApplicationException(Response.status(500).entity(x.getMessage()).type(mime_text).build());
		}
		
	}
		
	/**
	 *  Delete a Pol resource in response to an HTTP request
	 *  
	 *     DELETE /opensso-pol/{id}
	 *    
	 *  @param id 
	 */
	public Response deletePol(@HeaderParam("subjectid") String subjectId, @HeaderParam("id") String id) 
	throws WebApplicationException {
		try {
			PolicyExtinguisher helper = new PolicyExtinguisher();
			return Response.ok(helper.deletePol(subjectId, id)).type("text/xml").build(); 
		} catch (DbException x) {
			throw new WebApplicationException(Response.status(500).entity(x.getMessage()).type(mime_text).build());
		} catch (RestException x) {
			throw new WebApplicationException(Response.status(x.getHttpcode()).entity(x.getMessage()).type(mime_text).build());
		}			
	}


	//todo replace with regexp
	protected final boolean ValidateIPAddress(String ipAddress) {
		String[] parts = ipAddress.split( "\\." );
		if ( parts.length != 3 ) return false;
		for ( String s : parts ) {
			try { 
				int i = Integer.parseInt( s );
				if ((i < 0) || (i>255)) return false;
			}
			catch (NumberFormatException e) {
				return false;
			}
		}
		return true;
	}

}




