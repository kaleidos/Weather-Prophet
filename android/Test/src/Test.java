import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Test {
	private static final String URL_LOGIN="http://10.8.1.15:8080/weather/j_spring_security_check";
	
	public static String readRemoteJson(String urlString,
			Map<String, String> parameters) throws IOException {
		// Construct data
		String data = "";
		boolean first = true;
		for (Iterator<String> it = parameters.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			if (first) {
				first = false;
			} else {
				data += "&";
			}
			data += URLEncoder.encode(key, "UTF-8") + "="
					+ URLEncoder.encode(parameters.get(key), "UTF-8");
		}

		// Send data
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setDoInput(true);
	    con.setDoOutput(true);
	    //con.setRequestMethod("POST");
	    con.setUseCaches(false);
	    con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
	    con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	    
	    
	    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
		wr.write(data);
		wr.flush();

		// Get the response

		// Read all the text returned by the server
	    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    String str;
	    StringBuffer response=new StringBuffer();
	    while ((str = in.readLine()) != null) {
	        // str is one line of text; readLine() strips the newline character(s)
	    	response.append(str);
	    }
	    in.close();
	    return response.toString();

	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap<String, String> parameters=new HashMap<String, String>(); 
		
		parameters.put("j_username", "admin");
		parameters.put("j_password", "admin");
		
		try {
			System.out.println(Test.readRemoteJson(URL_LOGIN,parameters));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
