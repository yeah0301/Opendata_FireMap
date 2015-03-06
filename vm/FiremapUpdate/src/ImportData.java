import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;

import org.slf4j.*;

public class ImportData {

	SQLServer sql;

	public ImportData() {
		
	}

	

	/**
	 * Capture 重大不合格場所 and insert SQL Server
	 * 
	 * URL:http://trans.nfa.gov.tw/nfaweb/danger/%E5%8F%B0%E5%8C%97%E5%B8%82%E5%
	 * 9C%B0%E5%8D%80.htm
	 * 
	 */
	public void load_SeriousFailureLocation() {
		String URL = "http://trans.nfa.gov.tw/nfaweb/danger/%E5%8F%B0%E5%8C%97%E5%B8%82%E5%9C%B0%E5%8D%80.htm";
		Document doc = null;
		List<SeriousFailureLocation> list = new LinkedList<SeriousFailureLocation>();

		try {
			doc = Jsoup.connect(URL).timeout(1000).get();

			for (org.jsoup.nodes.Element element : doc.select("td.text")
					.select("tr")) {
				if (!element.hasAttr("background")) {

					String name = element.child(0).text();

					if (!name.equals("以下空白")) {

						String addr = element.child(1).text();
						String check = element.child(2).text();
						String dateString = element.child(3).text();
						LatLng latLng = queryAddressToLatlng(addr);

						int yy = Integer.parseInt((String) dateString
								.subSequence(0, 3));
						yy += 1911;

						new util();
						java.util.Date date = util.strToDatetime(yy + "-"
								+ dateString.substring(3, 5) + "-"
								+ dateString.substring(5, 7) + " 00:00:00");
						
						System.out.println(name+" "+addr+" "+check+" "+date.toString()+" "+latLng.getLat()
								+" "+latLng.getLng());

						list.add(new SeriousFailureLocation(name, addr, check,
								date, latLng.getLat(), latLng.getLng()));
					} else
						continue;
				} else
					continue;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sql = new SQLServer();
		sql.insert_SeriousFailureLocation(list);
		sql.close();

	}


	private LatLng queryAddressToLatlng(String address) {

		Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = null;
		GeocodeResponse geocoderResponse = null;
		LatLng latLng = new LatLng("0", "0");

		BigDecimal lat = null;
		BigDecimal lng = null;

		geocoderRequest = new GeocoderRequestBuilder().setAddress(address)
				.setLanguage("en").getGeocoderRequest();

		try {
			geocoderResponse = geocoder.geocode(geocoderRequest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (geocoderResponse.getStatus().value().equals("OK")) {
			for (GeocoderResult result : geocoderResponse.getResults()) {
				lat = result.getGeometry().getLocation().getLat();
				lng = result.getGeometry().getLocation().getLng();
				System.out.println(lat + "," + lng);
				return result.getGeometry().getLocation();
			}
		} else
			return latLng;

		return latLng;
	}

	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub

		ImportData importData = new ImportData();
		importData.load_SeriousFailureLocation();
		
	}

}
