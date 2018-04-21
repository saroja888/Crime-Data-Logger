package Crawler;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import Crawler.ContentExtraction;


public class WebCrawler {
	 static ContentExtraction ExtractionClass= new ContentExtraction();
public static void main(String args[]) throws IOException
{
	//Code for fetching present date and time 
			String year="";
			String month_num="";
			String date="";
			String month_string="";
			String date_sd="";
			DateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			DateFormat dateFormat2 = new SimpleDateFormat("MMMMM d, yyyy");
		    Calendar cal = Calendar.getInstance();
		    cal.add(Calendar.DATE, -1);
		    java.util.Date yesterday =  cal.getTime();
		   	String[] elements = dateFormat1.format(yesterday).split("/");
			if(elements.length >= 3)
			{
			 year = elements[0].toString();
			 month_num = elements[1].toString();
			String[] dateElements = elements[2].toString().split(" ");
			date = dateElements[0].toString();
			}
			String[] monthElements = dateFormat2.format(yesterday).toString().split(" ");
			month_string = monthElements[0].toString().trim();
			date_sd = monthElements[1].replaceAll(",","");
			// End of DateTime code
			
	Properties prop = new Properties();
	InputStream input = null;
	try {
		input = new FileInputStream("URLlinks.properties");
		prop.load(input);
		Enumeration<Object> enuKeys = prop.keys();
		while (enuKeys.hasMoreElements()) {
			String key = (String) enuKeys.nextElement();
			String value = prop.getProperty(key).trim();
			//Code for appending present date to URLS
			switch(key)
			{
			
			case "CentralMichiganUniversity" :
				value = CentralMichiganUniversity(value);
				break;
			case "UniversityofSouthernCalifornia" :
				value = value+year+"/"+month_num+"/"+month_num+date+year.substring(2, 4)+".pdf";
			    break;
			case "PrincetonUniversity" :
				value = value+year.substring(2, 4)+month_string.substring(0, 3)+date+"inc.pdf";
				break;
			case "UniversityofAlabama" :
				value =value+"cr"+month_num+year.substring(2, 4)+".pdf";
				break;

			case "TexasTechUniversity" :
				String valueToAppend = TexasTechUniversity(value);
				value = value+valueToAppend.replaceAll("index.php", "")+month_num+date+year.substring(2,4)+"_main.pdf";
				break;
			case "UniversityofCaliforniaSantaBarbara" :
				value =value+year+"-"+month_num+".pdf";
				break;	
			case "RensselaerPolytechnicInstitute" :
				value =value+month_string.substring(0, 3).toUpperCase()+"_"+year.substring(2, 4)+".pdf";
				break;	
			case "CaseWesternReserveUniversity" :
				value =value+month_string.toUpperCase()+"-"+year+".pdf";
				break;
			case "UniversityofCaliforniaatDavis" :
				value =value+month_num+date+year.substring(2,4)+"cl.pdf";
				break;
			case "UnivesityofGeorgia" :
				value =value+year+"/"+year+month_num+date+".pdf";
				break;
			case "ArizonaStateUnivesity" :
				value =value+month_string+year+".pdf";
				break;
			case "TexasAMUniversity" :
				value = value+month_num+"%20"+month_string+"%20"+year+"%20"+"Crime%20Log.pdf";
				break;
			case "UniversityofNotreDame" :
				value =value+month_num+date+year.substring(2,4)+".pdf";
				break;
			case "UniversityofCaliforniaatBerkeley" :
				value = value+month_num+date+year+".pdf";
				break;
			case "HarvardUniversity" :
				value =value+month_num+date+year.substring(2,4)+".pdf";
				break;
			case "FloridaIntlUniversity" :
				value = FloridaIntlUniversity(value);
				break;
			case "RutgersUniversityatCamden" :
				value = RutgersUniversity(value, "Camden");
				break;
			case "RutgersUniversityatNewark" :
				value = RutgersUniversity(value, "Newark");
				break;
			case "RutgersUniversityatNewBrunswick" :
				value = RutgersUniversity(value, "NewBrunswick");
				break;
			case "IndianaUniversityatBloomington" :
				value = IndianaUniversityatBloomington(value);
			
				break;
			case "UniversityatBuffalo" :
			  UniversityatBuffalo(value);
			  value ="";
				break;
			case "UniversityofCaliforniaatSanDiego" :
				value = UniversityofCaliforniaatSanDiego(value);
				break;
		
			case "JHU_EBaltimoreCampus" :
				 value = JohnsHopkinsUniversity(value,"EBaltimoreCampus");
				 break;
			
			case "JHU_PeabodyCampus" :
				 value = JohnsHopkinsUniversity(value,"PeabodyCampus");
				 break;
			case "UniversityofCaliforniaLA" :
				 UniversityofCaliforniaLA(value);
				  value = "";
				 break;
			case "DukeUniversity" :
				 value = DukeUniversity(value);
				 break;	 
			case "MichiganStateUniversity" :
				 value = MichiganStateUniversity(value);
				 break;
			case "UniversityofMissouri" :
				  UniversityofMissouri(value);
				  value = "";
				 break; 	
			case "UniversityofConnecticut" :
				 value = UniversityofConnecticut(value);
				  break;
			//case "GeorgetownUniversity" :
			//	 value = GeorgetownUniversity(value);
				//  break;
			case "VirginiaTech" :
				 value = VirginiaTech(value);
				 break;	
			case "UniversityofWashington" :
				 value = UniversityofWashington(value);
				 break;	
			case "UniversityofMinnesota" :
				 value = UniversityofMinnesota(value);
				 break;	
			case "MIT" :
				 value = MIT(value);
				 break;	 
			case "UniversityofSouthCarolina" :
				 value = UniversityofSouthCarolina(value);
				 break;
			case "PurdueUniversity" :
				 PurdueUniversity(value);
				 value = "";
				 break;
			case "PennStateCollege" :
				PennStateCollege(value);
				 value = "";
				 break;
			case "UniversityofArkansas" :
				UniversityofArkansas(value);
				 value = "";
				 break;	 
			case "NorthWesternUniversity" :
				NorthWesternUniversity(value);
				 value = "";
				 break;	
			case "UniversityofDelaware" :
				value = value+year+"/"+month_string.toLowerCase()+"/"+month_string.toLowerCase()+date+year.substring(2,4)+".html";
				UniversityofDelaware(value);
				value = "";
				 break;	
			case "UniversityofVirginia" :
				value = value+month_string.toLowerCase()+"-"+year;
				UniversityofVirginia(value);
				value = "";
				 break;	 
				 
			case "WashingtonUniversity" :
				WashingtonUniversity(value);
				 value = "";
				 break;	 
				 
			case "EmoryUnivesity" :
				EmoryUnivesity(value);
				 value = "";
				 break;	
			case "UniversityofCaliforniaRiverside" :
				UniversityofCaliforniaRiverside(value);
				 value = "";
				 break;	
			case "BostonUniversity" :
				BostonUniversity(value);
				 value = "";
				 break;	
			case "IllinoisInstituteofTechnology" :
				IllinoisInstituteofTechnology(value);
				 value = "";
				 break;	
			case "LehighUniversity" :
				LehighUniversity(value);
				 value = "";
				 break;	
			case "UniversityofNewMexico" :
				UniversityofNewMexico(value);
				 value = "";
				 break;	
				 
			case "UniversityofNebraska" :
				UniversityofNebraska(value);
				 value = "";
				 break;
			case "UniversityofMichigan" :
				value = value+month_num+"/"+date+"/"+year;
				UniversityofMichigan(value);
				 value = "";
				 break;  	 
			case "UniversityofIllinois_UC" :
				value = UniversityofIllinois_UC(value);
				 break; 
			case "YaleUniversity" :
				value = YaleUniversity(value);
				 break;
			case "OregonStateUniversity" :
				value = OregonStateUniversity(value);
				 break;	
			case "UniversityofTennessee" :
				value = UniversityofTennessee(value);
				 break;
			case "CarnegieMellonUniversity" :
				 CarnegieMellonUniversity(value);
				 value = "";
				 break;
			case "StateUniversityofNewYorkatBuffalo" :
				StateUniversityofNewYorkatBuffalo(value);
				 value = "";
				 break;
			case "TempleUnivesity" :
				TempleUnivesity(value);
				value="";
				 break;
			case "NewYorkUniversity" :
				NewYorkUniversity(value);
				value="";
				 break;
			case "CornellUniversity" :
				CornellUniversity(value);
				value="";
				 break;
			case "UniversityofChicago" :
				UniversityofChicago(value);
				value="";
				 break;
			case "UniversityofKentucky" :
				UniversityofKentucky(value);
				value="";
				 break;
			case "UniversityofFlorida" :
				UniversityofFlorida(value);
				value="";
				 break;
			case "UniversityofNorthCarolinaatChapelHill" :
				UniversityofNorthCarolinaatChapelHill(value);
				value="";
				 break;
			case "UniversityofArizona" :
				UniversityofArizona(value);
				value="";
				 break;
			case "UniversityofSouthFlorida" :
				UniversityofSouthFlorida(value);
				value="";
				 break;
			case "OhioStateUniversity" :
				OhioStateUniversity(value);
				value="";
				 break; 
			case "UniversityofMarylandCollegePark" :
				value = value+year+"&month="+month_num;
				UniversityofMarylandCollegePark(value);
				value="";
				 break;  
			case "UniversityofCincinnati" :
				UniversityofCincinnati(value);
				value="";
				 break;  
			case "AuburnUniversity" :
				AuburnUniversity(value);
				value="";
				 break; 
				 
			case "RiceUniversity" :
				RiceUniversity(value);
				value="";
				 break; 
				 
				 
			}
			//End of code for appending date
			DownloadPDF(key,value);
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
	catch (NullPointerException e) {
		e.printStackTrace();
	}
	
		
    }
public static String CentralMichiganUniversity(String BaseURL)
{
	String URL = BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element nodes=  document.select("a:contains(Da​ily Crime Log​)").first();
		 URL_return = "https://www.cmich.edu"+ nodes.attr("href");
		 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}

public static String FloridaIntlUniversity(String BaseURL)
{
	String URL = BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element sideDiv = document.select("div.su-spoiler-content").first();
		 Elements nodes=  sideDiv.select("a");
		 URL_return =  nodes.attr("href");
		 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static String RutgersUniversity(String BaseURL, String Campus)
{
	String URL = BaseURL;
	String URL_return="";
	Document document;
	try {
		document = Jsoup.connect(URL).get();
		Element tableContent = document.select("tbody:contains(Camden:)").first();
		Elements nodes= tableContent.select("a");
		String link_camden = "";
		String link_newark = "";
		String link_NewBrunswick = "";
		
		for(Element node : nodes)
		{
		String link =  node.attr("href");
	    if(link.contains("Camden"))
	    {
	     link_camden = link;	
	    }
	    if(link.contains("Newark"))
	    {
	     link_newark = link;	
	    }
	    if(!(link.contains("Camden") || link.contains("Newark")))
	    {
	     link_NewBrunswick = link;	
	    }
		}
			
		if(!link_camden.startsWith("http://rupd.rutgers.edu/"))
		{
			link_camden = "http://rupd.rutgers.edu/"+link_camden;
		}
		if(!link_newark.startsWith("http://rupd.rutgers.edu/"))
		{
			link_newark = "http://rupd.rutgers.edu/"+link_newark;
		}
		if(!link_NewBrunswick.startsWith("http://rupd.rutgers.edu/"))
		{
			link_NewBrunswick = "http://rupd.rutgers.edu/"+link_NewBrunswick;
		}
		if(Campus == "Camden")
		{
			URL_return = link_camden;
		}
		
		if(Campus == "NewBrunswick")
		{
			URL_return = link_NewBrunswick;
		}
		if(Campus == "Newark")
		{
			URL_return = link_newark;
		}
		
	} catch (IOException e) {
	
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	 return URL_return;
}
public static String IndianaUniversityatBloomington(String BaseURL)
{
	String URL = BaseURL;
	String URL_return="";
	Document document;
	try {
		document = Jsoup.connect(URL).get();
		Element ContentDiv = document.select("div.text").first();
		Element linkelement = ContentDiv.select("a").first();
		String linkContent = linkelement.attr("href");
		if(!linkContent.isEmpty())
		{
			URL_return ="https://iupd.indiana.edu/crimelogs/"+linkContent.replaceAll(" ", "%20");
		}
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static void UniversityatBuffalo(String BaseURL)
{

	String URL = BaseURL;
		
	Document document;
	try {
		document = Jsoup.connect(URL).get();
		Element divContent = document.select("div.table-style-hstripes").first();
		Element tableContent = divContent.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		for(Element row : rowsContent)
		{
		Elements cellContent =	row.select("td");
		Iterator<?> iterator =  cellContent.iterator();
		
		while(iterator.hasNext())
		{
			String IncidentNumber ="";
			String DateReported = "";
			String TimeReported = "";
			String IncidentType = "";
			String Location = "";
			
			Element IncidentNumber_ele = (Element) iterator.next();
		    IncidentNumber = IncidentNumber_ele.text();
			
			Element DateReported_ele = (Element) iterator.next();
		    DateReported = DateReported_ele.text();
			
			Element TimeReported_ele = (Element) iterator.next();
		    TimeReported = TimeReported_ele.text();
			
			Element IncidentType_ele = (Element) iterator.next();
		    IncidentType = IncidentType_ele.text();
			
			Element Location_ele = (Element) iterator.next();
		    Location = Location_ele.text();
			
			////System.out.println(IncidentNumber +"   "+DateReported+"   "+TimeReported+"   "+IncidentType+"   "+Location);
			  UniversityData recordData = new UniversityData();
	          recordData.IncidentNumber = IncidentNumber;
	          recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy") ;
	          recordData.OffenseDateTime=  dateToConvert( DateReported,"MM/dd/yyyy") ;
	          recordData.Location= Location;
	          recordData.IncidentType= IncidentType;
	          recordData.Disposition= "NOT AVAILABLE";
	          finalRecordsList.add(recordData);
		}
		}
		ExtractionClass.UniversityatBuffaloExtractionMethod(finalRecordsList);	
	} catch (IOException e) {
		
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static String UniversityofCaliforniaatSanDiego(String BaseURL)
{
    String URL = BaseURL;
	String URL_return ="http://www.police.ucsd.edu/docs/reports/CallsandArrests/CallsForService/";
	Document document;
	try {
		document = Jsoup.connect(URL).get();
		Element FirstOptionValue = document.select("option").get(1);
		URL_return =URL_return+FirstOptionValue.text().replaceAll(" ","%20");
         } catch (IOException e) {
			  	e.printStackTrace();
       	}catch (NullPointerException e) {
    		e.printStackTrace();
    	}
	
       	return URL_return;
}
public static String JohnsHopkinsUniversity(String BaseURL,String Campus)
{
	
	String URL_EBaltimoreCampus_indirect = "";
	String URL_PeabodyCampus_indirect = "";
	String URL_EBaltimoreCampus_direct ="";
	String URL_PeabodyCampus_direct ="";
	String URL_return="";
	
	if(Campus == "EBaltimoreCampus")
	{
		URL_EBaltimoreCampus_indirect = BaseURL;
	}
	if(Campus == "PeabodyCampus")
	{
		URL_PeabodyCampus_indirect = BaseURL;
	}
	
	Document document;
	try {
		if(Campus == "EBaltimoreCampus")
		{
		document = Jsoup.connect(URL_EBaltimoreCampus_indirect).get();
		Element tagContent = document.select("a:contains(View Crime Log)").first();
		URL_EBaltimoreCampus_direct = tagContent.attr("href");
		}
		if(Campus == "PeabodyCampus")
		{
		document = Jsoup.connect(URL_PeabodyCampus_indirect).get();
		Elements divContent = document.select("a:contains(Peabody Institute Crime Log)");
		URL_PeabodyCampus_direct = divContent.attr("href");
		}
	} catch (IOException e) {
		
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}

	if(Campus == "EBaltimoreCampus")
	{
		URL_return = URL_EBaltimoreCampus_direct;
	}
	
	if(Campus == "PeabodyCampus")
	{
		URL_return = URL_PeabodyCampus_direct;
	}

	return URL_return;
}
public static void UniversityofCaliforniaLA(String BaseURL)
{

	String URL = BaseURL;
	String DateReported="";
	String Location="";
	Document document;
	try {
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		document = Jsoup.connect(URL).get();
		Element divContent = document.select("div.field-item").first();
		Elements tagContent = divContent.select("p");
		for(Element record : tagContent )
		{
			String DateandLocation = record.text();
			String[] elements = DateandLocation.split(":");
			if(elements.length == 2)
			{
			 DateReported = elements[0].toString();
			 Location = elements[1].toString();
			}
			Random ran = new Random();
			int randomNum = ran.nextInt(1000000000) + 1;
			UniversityData recordData = new UniversityData();
	          recordData.IncidentNumber = "RN_"+randomNum;
	          recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yy") ;
	          recordData.OffenseDateTime=  dateToConvert( DateReported,"MM/dd/yy") ;
	          recordData.Location= Location;
	          recordData.IncidentType= "NOT AVAILABLE";
	          recordData.Disposition= "NOT AVAILABLE";
	          finalRecordsList.add(recordData);
		}
		ExtractionClass.UniversityofCaliforniaLAExtractionMethod(finalRecordsList);	
        } catch (IOException e) {
		
		e.printStackTrace();
	    }catch (NullPointerException e) {
			e.printStackTrace();
		}
	
}
public static String DukeUniversity(String BaseURL)
{
	String URL =BaseURL;
	String URL_return="http://police.duke.edu/news_stats/summaries/";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element sideDiv = document.select("p:contains(Current Week)").first();
		 Element nodes=  sideDiv.nextElementSibling();
		 Element node_link = nodes.select("a").first();
		 URL_return =  URL_return+node_link.attr("href");
		
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;

}
public static String MichiganStateUniversity(String BaseURL)
{
	String URL =BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element DivContent = document.select("a:contains(Download Clery Crime and Fire Log)").first();
		 URL_return = DivContent.attr("href");
		 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static void UniversityofMissouri(String BaseURL)
{
	String URL = BaseURL;
	
	try {
		Document document = Jsoup.connect(URL).get();
		 Element tableHTML = document.select("table").first();
		 Elements rowsHTML = tableHTML.select("tr");
		 Iterator<?> iterator = rowsHTML.iterator();
		 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 while(iterator.hasNext())
		 {
			 Element firstRow = (Element) iterator.next();
			 Element secondRow = (Element) iterator.next();
			 
			 Elements firstRowElements = firstRow.select("td");
			 Elements secondRowElements = secondRow.select("td");
			 
			 Iterator<?> iterator1 = firstRowElements.iterator();
			 Iterator<?> iterator2 = secondRowElements.iterator();
			
			 String DateTimeOccurred = "";
			 String CaseNumber = "";
			 String DateTimeReported = "";
			 String Location = "";
			 String IncidentType = "";
			 String Disposition = "";
			 
			 while(iterator1.hasNext())
			 {
				 Element cellData1 = (Element) iterator1.next();
				 Element cellData2 = (Element) iterator1.next();
				 Element cellData3 = (Element) iterator1.next();
				 Element cellData4 = (Element) iterator1.next();
				 Element cellData5 = (Element) iterator1.next();
				 Element cellData6 = (Element) iterator1.next();
				 
				 CaseNumber = cellData1.text();
				 DateTimeReported = cellData2.text();
				 Location = cellData3.text();
				 IncidentType = cellData4.text();
				 Disposition = cellData6.text();
				
			 }
			 
			 while(iterator2.hasNext())
			 {
				 Element cellData_secondrow = (Element) iterator2.next();
				 String[] DateTimeOccurredElements = cellData_secondrow.text().split("-");
				 if(DateTimeOccurredElements.length >= 1)
				 {
					  DateTimeOccurred = DateTimeOccurredElements[0].toString();
				 }
			}
			 UniversityData recordData = new UniversityData();
	          recordData.IncidentNumber = CaseNumber;
	          recordData.ReportedDateTime=  dateToConvert( DateTimeReported,"MM/dd/yyyy hh:mm:ss a") ;
	          recordData.OffenseDateTime=  dateToConvert( DateTimeOccurred,"MM/dd/yyyy hh:mm:ss a") ;
	          recordData.Location= Location;
	          recordData.IncidentType= IncidentType;
	          recordData.Disposition= Disposition;
	          finalRecordsList.add(recordData);
		 }
		 ExtractionClass.UniversityofMissouriExtractionMethod(finalRecordsList);	
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static String UniversityofConnecticut(String BaseURL)
{

	String URL =BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element sideDiv = document.select("p:contains(Click the link(s) below for an electronic copy of the daily crime log.)").first();
		 Element nodes=  sideDiv.nextElementSibling();
		 Element node_link = nodes.select("a").first();
		 URL_return =  URL_return+node_link.attr("href");
		
		 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static String GeorgetownUniversity(String BaseURL)
{
	String URL = BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element tagContent = document.select("a:contains(Current Crime Log)").first();
		 URL_return =  tagContent.attr("href");
		
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
return 	URL_return;
}
public static String VirginiaTech(String BaseURL)
{
	String URL =BaseURL;
	String URL_return="http://police.vt.edu";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element tagContent = document.select("a:contains(In progress)").first();
		 URL_return = URL_return+tagContent.attr("href");
		
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
return URL_return;	
}
public static String UniversityofWashington(String BaseURL)
{
	String URL = BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element tagContent = document.select("a:contains(Download)").first();
		 URL_return = tagContent.attr("href");
		
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static String UniversityofMinnesota(String BaseURL)
{
	String URL =BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element tagContent = document.select("a:contains(Daily Crime Log)").first();
		 URL_return = tagContent.attr("href");
		
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static String MIT(String BaseURL)
{
	String URL = "https://police.mit.edu/police-logs";
	String URL_return="https://police.mit.edu";
	try {
		Document document = Jsoup.connect(URL).get();
		 Elements tagContent = document.select("p:contains(The MIT Police online crime log is published in PDF)");
		 Element linkContent = tagContent.select("a").last();
		 URL_return = URL_return+ linkContent.attr("href"); 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	
	return URL_return;
}
public static String TexasTechUniversity(String BaseURL)
{

	String URL = BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		 Element node=  document.select("a:contains(Lubbock Campus)").first();
		 URL_return =  node.attr("href");
		 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static String UniversityofSouthCarolina(String BaseURL)
{
	String URL = BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		 Elements nodes=  document.select("a:contains(View Daily Log)");
		 URL_return =  nodes.attr("href");
		 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static void PurdueUniversity(String BaseURL)
{
	String URL =BaseURL;
    String URL_return="";
try {
	Document document = Jsoup.connect(URL).get();
	 Elements tagContent = document.select("div#content");
	 Elements linkContent = tagContent.select("p");
	 ArrayList<String> modifiedList = new ArrayList<String>();
	 for(Element RowContent : linkContent)
	 {
		 String RowData = RowContent.html().replace("<br />", "\n");
		 RowData = RowData.replace("&nbsp;","");
		if(!(RowData.trim().isEmpty() || RowData.split("\n").length <=1))
		{
			modifiedList.add(RowData);
		}
	 }
	 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	 for(String text:modifiedList)
	 {
		 String CaseNum="";
		 String DateReported = "";
		 String Disposition ="";
		 String Location="";
		 String IncidentType = "";	
		
		 if(text.contains("NO REPORT"))
		 {
			 Disposition = "NO REPORT";
		 }
		 if(text.contains("UNABLE TO LOCATE"))
		 {
			 Disposition = "UNABLE TO LOCATE";
		 }
		 if(text.contains("PENDING"))
		 {
			 Disposition = "PENDING";
		 }
		 if(text.contains("CASE CLOSED"))
		 {
			 Disposition = "CASE CLOSED";
		 }
		 if(text.contains("NOT FILED BY PROSECUTOR"))
		 {
			 Disposition = "NOT FILED BY PROSECUTOR";
		 }
		 if(text.contains("CITED FOR POSSESSION"))
		 {
			 Disposition = "CITED FOR POSSESSION";
		 }
		 if(text.contains("NO FURTHER INFORMATION"))
		 {
			 Disposition = "NO FURTHER INFORMATION";
		 }
		 if(text.contains("WARNING ISSUED"))
		 {
			 Disposition = "WARNING ISSUED";
		 }
		 
		 if(text.contains("EXCHANGED INFORMATION"))
		 {
			 Disposition = "EXCHANGED INFORMATION";
		 }
		 if(text.contains("ARRESTED"))
		 {
			 Disposition = "ARRESTED";
		 }
		 if(text.contains("WARNING ISSUED"))
		 {
			 Disposition = "WARNING ISSUED";
		 }
		 if(text.contains("UNFOUNDED"))
		 {
			 Disposition = "UNFOUNDED";
		 }
		 
		 if(text.contains("OWNER LOCATED"))
		 {
			 Disposition = "OWNER LOCATED";
		 }
		 
		 if(text.contains("PROSECUTOR REVIEW"))
		 {
			 Disposition = "PROSECUTOR REVIEW";
		 }
		 String[] lines= text.split("\n");
		 if(lines.length >=1)
		 {
			 CaseNum = lines[0].toString();
		 }
		
		 if(lines.length >=2)
		 {
			 String lastline = "";
			 if(lines[1].contains("REPORTED"))
			 {
				String DateReportedElement = lines[1].toString().replace("REPORTED", "").trim();
				if(DateReportedElement.matches("[0-9,-:. ]+"))
				{
					DateReported = DateReportedElement;
				}
				if(lines.length >=3)
				 {
				lastline = lines[2].toString();
				 }
			 }
			 else
			 {
				 lastline = lines[1].toString();
			 }
			 if(lastline.toUpperCase().contains("AT"))
			 {
			 String[] items=lastline.toUpperCase().split("AT");
			 if(items.length >=2)
			 {
				 IncidentType = items[0].toString();
				 Location = items[1].toString();
			 }
			 }
		 }
		 UniversityData recordData = new UniversityData();
         recordData.IncidentNumber = CaseNum;
         recordData.ReportedDateTime= dateToConvert( DateReported,"MM-dd-yy, H:mm") ;
         recordData.OffenseDateTime=  dateToConvert( DateReported,"MM-dd-yy, H:mm") ;
         recordData.Location= Location;
         recordData.IncidentType= IncidentType;
         recordData.Disposition= Disposition;
         finalRecordsList.add(recordData);
		 
	 }
	 ExtractionClass.PurdueUniversityExtractionMethod(finalRecordsList);
} catch (IOException e) {
	e.printStackTrace();
}catch (NullPointerException e) {
	e.printStackTrace();
}
}
public static void PennStateCollege(String BaseURL)
{
	String URL = BaseURL;
	try {
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
        for(int i=0;i<=10;i++)
        {
		Document document = Jsoup.connect(URL+i).get();
		 Elements mainDiv = document.select("div.view-content");
		 Elements tableDiv = mainDiv.select("div.views-row");
		 ArrayList<String> modifiedList = new ArrayList<String>();
		for(Element rowDiv : tableDiv)
		{   
			Elements cellDiv = rowDiv.select("div.views-field");
			for(Element eachCellContent : cellDiv)
			{
				if(eachCellContent.text().contains("Incident #:") || eachCellContent.text().contains("Occurred:") 
				|| eachCellContent.text().contains("Reported:") || eachCellContent.text().contains("Nature of the Incident:") || 
				eachCellContent.text().contains("Location:") || eachCellContent.text().contains("Disposition:") )
				{
					if(!eachCellContent.text().contains("Charges:"))
					{
					modifiedList.add(eachCellContent.text());
					}
				}
			}
			
		}
	
		Iterator<String> iterator = modifiedList.iterator();
		
		
		 while(iterator.hasNext())
         {
			    String ReportNum="";
	        	String DateReported="";
	        	String Location="";
	        	String DateOccurred = "";
	        	String IncidentType = "";
	        	String Disposition = "";
	        	
			    String ReportNumElement= iterator.next().toString();
	        	String DateOccurredElement= iterator.next().toString();
	        	String DateReportedElement= iterator.next().toString();
	        	String IncidentTypeElement = iterator.next().toString();
	        	String LocationElement = iterator.next().toString();
	        	String DispositionElement = iterator.next().toString();
		
			 if(ReportNumElement.contains("Incident #:"))
			 {
				 ReportNum = ReportNumElement.replace("Incident #:","").trim();
			 }
			 if(DateOccurredElement.contains("Occurred:"))
			 {
				 DateOccurred = DateOccurredElement.replace("Occurred:","").trim();
			 }
			 if(DateReportedElement.contains("Reported:"))
			 {
				 DateReported = DateReportedElement.replace("Reported:","").trim();
			 }
			 if(IncidentTypeElement.contains("Nature of the Incident:"))
			 {
				 IncidentType = IncidentTypeElement.replace("Nature of the Incident:","").trim();
			 }
			 if(LocationElement.contains("Location:"))
			 {
				 Location = LocationElement.replace("Location:","").trim();
			 }
			 if(DispositionElement.contains("Disposition:"))
			 {
				 Disposition = DispositionElement.replace("Disposition:","").trim();
			 }
			 
			 if(DateReported.contains("to"))
			 {
			 	String[] values = DateReported.split("to");
			 	DateReported = values[0].toString();
			 }
			 if(DateOccurred.contains("to"))
			 {
			 	String[] values = DateOccurred.split("to");
			 	DateOccurred = values[0].toString();
			 }
			 UniversityData recordData = new UniversityData();
	          recordData.IncidentNumber = ReportNum;
	          recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy h:mm a") ;
	          recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy h:mm a") ;
	          recordData.Location= Location;
	          recordData.IncidentType= IncidentType;
	          recordData.Disposition= Disposition;
	          finalRecordsList.add(recordData);
			  
         }
        }
        ExtractionClass.PennStateCollegeExtractionMethod(finalRecordsList);
        } catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void UniversityofArkansas(String BaseURL)
{
	String URL = BaseURL;
	try {
		 Document document = Jsoup.connect(URL).get();
		 Element mainDiv = document.select("*:contains(Daily Crime Log)").first();
		 Elements nodes=  mainDiv.select("p:contains(Case)");
		 ArrayList<String> modifiedList = new ArrayList<String>();
		 
		 for(Element node :nodes)
		 {
		 String text = node.html().replace("<br />", "\n");
		 modifiedList.add(text);
		 }
		 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
			
		 Iterator<String> iterator = modifiedList.iterator();
		 while(iterator.hasNext())
		 {
			    String ReportNum="";
	        	String DateReported="";
	        	String Location="";
	        	String IncidentType = "";
	        	String Disposition = "";
			 
			 String record = iterator.next().toString();
			 String[] lines = record.split("\n");
			 for(int i=0;i<= lines.length-1;i++)
			 {
				 if(lines[i].toString().contains("<strong>"))
				 {
					String DateReportedElement = lines[i].toString().replace("<strong>","");
					DateReported = DateReportedElement.replace("</strong>", "");
					 break;
				 }
			 }
			 if(lines.length>=2)
			 {
				 if(lines[1].toString().contains("Case"))
				 {
					 String[] elements = lines[1].toString().split(" ");
					 ReportNum = elements[1].toString().replace(",","").trim();
					 
					 for(int k=2;k<= elements.length -1;k++)
					 {
						 if(elements[k].toString().contains(";") || elements[k].toString().contains(","))
						 {
						 IncidentType = IncidentType+elements[k].toString().substring(0, elements[k].toString().length()-2);
						 break;
						 }
						 else
						 {
							 IncidentType = IncidentType+elements[k].toString();
						 }
					 }
					
				 }
		 }
			 if(lines[lines.length - 1].contains("Case Status:"))
			 {
				 Disposition = lines[lines.length - 1].replace("Case Status:","").trim();
			 }
			 if(DateReported.contains("at"))
				{
				 DateReported = DateReported.replace("at","");
				}
			 
			 UniversityData recordData = new UniversityData();
	         recordData.IncidentNumber = ReportNum;
	         recordData.ReportedDateTime=  dateToConvert( DateReported,"E, MMMMM dd, yyyy  H:mm") ;
	         recordData.OffenseDateTime=  dateToConvert( DateReported,"E, MMMMM dd, yyyy  H:mm") ;
	         recordData.Location= Location;
	         recordData.IncidentType= IncidentType;
	         recordData.Disposition= Disposition;
	         finalRecordsList.add(recordData);
			 			 
		 }
		 ExtractionClass.UniversityofArkansasExtractionMethod(finalRecordsList);
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void NorthWesternUniversity(String BaseURL)
{

	String URL=BaseURL;
	try {
		Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		 Elements rowsContent = tableContent.select("tr");
		  ArrayList<String> modifiedList = new ArrayList<String>();
		  ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
			
		for(Element rowContent : rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			if(cellsContent.text().contains("Case Number") || cellsContent.text().contains("Date & Time:") || cellsContent.text().contains("Date & Time:") || 
			   cellsContent.text().contains("Location:") || cellsContent.text().contains("Incident Type:") || cellsContent.text().contains("Disposition:") )
			{
				modifiedList.add(cellsContent.text());
			}
		}
		 
		 Iterator<String> iterator = modifiedList.iterator();
		 ArrayList<String> recordsList = new ArrayList<String>();
		 StringBuilder builder=new StringBuilder();
		 while(iterator.hasNext())
		 {
			 String record = iterator.next().toString();
			 if(record.contains("Case Number"))
			 {
				 recordsList.add(builder.toString());
				 builder=new StringBuilder();
				 builder.append(record+"\n");
			 }
			 else
			 {
				 builder.append(record+"\n"); 
			 }
		 }
		 recordsList.remove(0);
		 Iterator<String> iterator2 = recordsList.iterator();
		 while(iterator2.hasNext())
		 {
			    String ReportNum="";
	        	String DateReported="";
	        	String Location="";
	        	String DateOccurred = "";
	        	String IncidentType = "";
	        	String Disposition = "";
			 String recordDataElements = iterator2.next().toString();
			 String[] lines = recordDataElements.split("\n");
			 
				 if(lines[0].contains("Case Number"))
				 {
					 ReportNum = lines[0].replace("Case Number","").trim();
				 }
				 if(lines[1].contains("Date & Time:"))
				 {
					 DateReported = lines[1].replace("Date & Time:","").trim().replace("Reported","").trim();
				 }
				 if(lines[2].contains("Date & Time:"))
				 {
					 DateOccurred = lines[2].replace("Date & Time:","").trim().replace("Occurred","").trim();
				 }
				 if(lines[3].contains("Location:"))
				 {
					 Location = lines[3].replace("Location:","").trim();
				 }
				 if(lines[4].contains("Incident Type:"))
				 {
					 IncidentType = lines[4].replace("Incident Type:","").trim();
				 }
				 if(lines[5].contains("Disposition:"))
				 {
					 Disposition = lines[5].replace("Disposition:","").trim();
				 }
				 UniversityData recordData = new UniversityData();
				 String dr = DateReported.replaceAll("at","").replaceAll(" ","");
				 String oc = DateOccurred.replaceAll("at","").replaceAll(" ","");
				 
		         recordData.IncidentNumber = ReportNum;
		         recordData.ReportedDateTime= dateToConvert( dr,"MMMMMdd,yyyyh:mm:ssa") ; 
		         recordData.OffenseDateTime=  dateToConvert( oc,"MMMMMdd,yyyyh:mm:ssa") ;
		         recordData.Location= Location;
		         recordData.IncidentType= IncidentType;
		         recordData.Disposition= Disposition;
		         finalRecordsList.add(recordData);
		 }
		 ExtractionClass.NorthWesternUniversityExtractionMethod(finalRecordsList);
		 
	} catch (IOException e) {
		e.printStackTrace();
	}
	catch (NullPointerException e) {
		e.printStackTrace();
		
	}
}
public static void UniversityofDelaware(String BaseURL)
{
	String URL = BaseURL;
	try {
		Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		 Elements rowsContent = tableContent.select("tr"); 
		 
		 ArrayList<String> modifiedList = new ArrayList<String>();
		  ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 for(Element rowContent : rowsContent)
		 {
			 Elements cellContent = rowContent.select("td");
			 StringBuilder builder = new StringBuilder();
			 for(Element cellData: cellContent)
			 {
				 builder.append(cellData.text()+";");
		     }
			 modifiedList.add(builder.toString().substring(0,builder.toString().length()-1 ));
		 }
		 modifiedList.remove(0);
		 Iterator<String> iterator = modifiedList.iterator();
		 while(iterator.hasNext())
		 {
			    String ReportNum="";
	        	String DateReported="";
	        	String Location="";
	        	String DateOccurred = "";
	        	String IncidentType = "";
	        	String Disposition = "";
	        	
			 String record = iterator.next().toString();
			 String[] dataElements = record.split(";");
			 if(dataElements.length >=6)
			 {
			 ReportNum = dataElements[0].toString();
			 DateOccurred = dataElements[2].toString();
			 IncidentType = dataElements[3].toString();
			 Location =  dataElements[4].toString();
			 Disposition = dataElements[5].toString();
			 }
			 DateReported = DateOccurred;
			 
			 
			 UniversityData recordData = new UniversityData();
	         recordData.IncidentNumber = ReportNum;
	         recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yy Hmm") ;
	         recordData.OffenseDateTime=  dateToConvert( DateReported,"MM/dd/yy Hmm") ;
	         recordData.Location= Location;
	         recordData.IncidentType= IncidentType;
	         recordData.Disposition= Disposition;
	         finalRecordsList.add(recordData);
		 }
		 ExtractionClass.UniversityofDelawareExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void UniversityofVirginia(String BaseURL)
{
	String URL =BaseURL;
	try {
		Document document = Jsoup.connect(URL).get();
		 Elements mainDiv = document.select("div.field-item");
		 Elements childDivs = mainDiv.select("div");
		 StringBuilder fullText = new StringBuilder();
		 ArrayList<String> modifiedList = new ArrayList<String>();
		 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 for(Element eachDiv:childDivs)
		 {
			if(eachDiv.text().length() == 1)
			{
				modifiedList.add(fullText.toString());
				fullText = new StringBuilder();
			}else
			{
				fullText.append(eachDiv.text()+";;;");
				
			}
		 }
		 
		 Iterator<String> iterator = modifiedList.iterator();
		 while(iterator.hasNext())
		 {
			    String ReportNum="";
	        	String DateReported="";
	        	String Location="";
	        	String DateOccurred = "";
	        	String IncidentType = "";
	        	String Disposition = "";
	        	
			String record = iterator.next().toString();
			String[] lines = record.split(";;;");
			
			if(lines.length == 4)
			{
				Location = lines[0].toString();
				IncidentType = lines[1].toString();
						
					
			   String[] caseNumElements = 	lines[0].split(" ");
		    	if(caseNumElements[caseNumElements.length - 1].matches("[0-9/-]+"))
		        	{
				ReportNum =  caseNumElements[caseNumElements.length - 1];
				Location = Location.replace(ReportNum, "");
				
		        	}
				if(lines[2].contains("RPT:") )
				{
					String dateElements = lines[2].toString().replace("RPT:", "");
					String[] items = dateElements.split("OCC:");
					if(items.length == 2)
					{
						DateReported = items[0].toString();
						DateOccurred = items[1].toString();
					}
				}
				if(lines[3].contains("CASE STATUS:") )
				{
					Disposition = lines[3].replace("CASE STATUS:","");
				}
				
				 UniversityData recordData = new UniversityData();
		         recordData.IncidentNumber = ReportNum;
		         recordData.ReportedDateTime=  dateToConvert( DateReported,"HHmm MM-dd-yy") ;
		         if(DateOccurred.contains("to"))
		     	{
		     	String[] values = DateOccurred.split("to");
		     	DateOccurred = values[0].toString().trim();
		     	}
		         recordData.OffenseDateTime=  dateToConvert( DateOccurred,"HHmm MM-dd-yy") ;
		         recordData.Location= Location;
		         recordData.IncidentType= IncidentType;
		         recordData.Disposition= Disposition;
		         finalRecordsList.add(recordData);
			}
			
		 }
		 ExtractionClass.UniversityofVirginiaExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void WashingtonUniversity(String BaseURL)
{
	String URL = BaseURL;
	try {
		Document document = Jsoup.connect(URL).get();
		 Elements DivContents = document.select("div.alert");
		 StringBuilder recordText = new StringBuilder();
		 ArrayList<String> modifiedList = new ArrayList<String>();
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 for(Element eachDiv : DivContents)
		 {
			 recordText = new StringBuilder();
			 recordText.append(eachDiv.select("li.date").text()+"\n");
			 recordText.append(eachDiv.select("li.time").text()+"\n");
			 recordText.append(eachDiv.select("li.type").text()+"\n");
			 recordText.append(eachDiv.select("li.loc").text()+"\n");
			Element detailsDiv = eachDiv.select("div.details").first();
			recordText.append(detailsDiv.text().replace("Report Number:", "").replace("Occurred:", "\n").replace("Synopsis:", "\n").replace("Disposition:", "\n"));
			modifiedList.add(recordText.toString());
		 }
		 Iterator<String> iterator = modifiedList.iterator();
		 while(iterator.hasNext())
		 {
			    String ReportNum="";
	        	String DateReported="";
	        	String Location="";
	        	String DateOccurred = "";
	        	String IncidentType = "";
	        	String Disposition = "";
			 String record = iterator.next().toString();
			 String[] lines = record.split("\n");
			 
			 if(lines.length >= 8)
			 {
				 DateReported = lines[0].toString()+lines[1].toString();
				 IncidentType = lines[2].toString();
				 Location = lines[3].toString();
				 ReportNum = lines[4].toString();
				 DateOccurred = lines[5].toString();
				 Disposition = lines[7].toString();
		String[] dateValues = 		 DateOccurred.split(" ");
		if(dateValues.length >= 1)
		{
			DateOccurred = dateValues[0];
			DateReported = dateValues[0];
			
		}
			 UniversityData recordData = new UniversityData();
	          recordData.IncidentNumber = ReportNum;
	          recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/d/yyyy") ;
	          
	          recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/d/yyyy") ;
	          recordData.Location= Location;
	          recordData.IncidentType= IncidentType;
	          recordData.Disposition= Disposition;
	          finalRecordsList.add(recordData);
			 }
		 }
		 ExtractionClass.WashingtonUniversityExtractionMethod(finalRecordsList);
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void EmoryUnivesity(String BaseURL)
{
String URL =BaseURL;
	
	try {
		Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table:contains(Emory Police Department Daily Crime Log)").first();
		 Elements rowNodes=  tableContent.select("tr");
		 StringBuilder builder = new StringBuilder();
		 ArrayList<String> modifiedList = new ArrayList<String>();

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 for(Element rowContent : rowNodes)
		 {
			 Elements cellContent = rowContent.select("td");
			 builder = new StringBuilder();
			 for(Element cellData : cellContent)
			 {
				 builder.append(cellData.text()+"\n");
			 }
			 modifiedList.add(builder.toString());
		 }
		 
		 Iterator<String> iterator = modifiedList.iterator();
		 while(iterator.hasNext())
		 {
			    String ReportNum="";
	        	String DateReported="";
	        	String Location="";
	        	String DateOccurred = "";
	        	String IncidentType = "";
	        	String Disposition = "";
	        	
			 String record = iterator.next().toString();
			 String[] lines = record.split("\n");
			
			 if(lines.length == 7)
			 {
				 ReportNum = lines[0].toString();
				 DateReported = lines[1].toString();
				 IncidentType = lines[2].toString();
				 DateOccurred = lines[3].toString();
				 Location = lines[5].toString();
				 Disposition = lines[6].toString();
				 
				 UniversityData recordData = new UniversityData();
		          recordData.IncidentNumber = ReportNum;
		          recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yy") ;
		          recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yy H:mm") ;
		          recordData.Location= Location;
		          recordData.IncidentType= IncidentType;
		          recordData.Disposition= Disposition;
		          finalRecordsList.add(recordData);
			 }
		 }
		 
		 ExtractionClass.EmoryUnivesityExtractionMethod(finalRecordsList);
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void UniversityofCaliforniaRiverside(String BaseURL)
	{
		String URL = BaseURL;
		try {
			Document document = Jsoup.connect(URL).get();
			 Elements linkTags1 = document.select("a:contains(UCPD Crime Alert:)");
			 Elements linkTags2 = document.select("a:contains(Timely Warning:)");
			 StringBuilder recordsText = new StringBuilder();
			 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
			 for(Element tag : linkTags1)
			 {
				 recordsText.append(tag.text().replace("UCPD Crime Alert:", "").trim()+"\n");
			 }
			 for(Element tag : linkTags2)
			 {
				 recordsText.append(tag.text().replace("Timely Warning:", "").trim()+"\n");
			 }
			 
			 String[] lines = recordsText.toString().split("\n");
			 for(int i=0;i<= lines.length-1;i++)
			 {
				// String ReportNum="";
		        	String DateReported="";
		        	String Location="";
		        	String DateOccurred = "";
		        	String IncidentType = "";
		        	String Disposition = "";
				 String record = lines[i].toString();
				 if(record.contains("-"))
				 {
				 String[] elements = record.split("-");
				 IncidentType = elements[0].toString();
				 DateOccurred = elements[1].toString();
				 DateReported = DateOccurred;
				 
				 Random ran = new Random();
				int randomNum = ran.nextInt(1000000000) + 1;
				UniversityData recordData = new UniversityData();
			      recordData.IncidentNumber = "RN_"+randomNum;
		          recordData.ReportedDateTime=  dateToConvert( DateReported.trim().toUpperCase(),"MMM dd, yyyy") ;
		          recordData.OffenseDateTime=  dateToConvert( DateOccurred.trim().toUpperCase(),"MMM dd, yyyy") ;
		          recordData.Location= Location;
		          recordData.IncidentType= IncidentType;
		          recordData.Disposition= Disposition;
		          finalRecordsList.add(recordData);
				 }
				 
			 }
			 ExtractionClass.UniversityofCaliforniaRiversideExtractionMethod(finalRecordsList);	 
		} catch (IOException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
public static void BostonUniversity(String BaseURL)
{
	String URL = BaseURL;
	try {
		Document document = Jsoup.connect(URL).get();
		 Elements sideDiv = document.select("p");
		 Elements childDivs = sideDiv.select("b");
		 ArrayList<String> modifiedList = new ArrayList<String>();
		 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 for(Element childDivContent : childDivs)
		 {
			String text = childDivContent.html().replace("<br />","\n");
			modifiedList.add(text);
		 }
		 Iterator<String> iterator = modifiedList.iterator();
		 while(iterator.hasNext())
		 {
			    String ReportNum="";
	        	String DateReported="";
	        	String Location="";
	        	@SuppressWarnings("unused")
				String DateOccurred = "";
	        	String IncidentType = "";
	        	String Disposition = "";
			 String record = iterator.next().toString();
			 String[] lines = record.split("\n");
			 if(lines.length == 3)
			 {
				 String[] elements = lines[0].toString().replace(",","").split(" ");
				 for(String element : elements)
				 {
					 if(element.matches("[0-9]+") && element.length() >= 7)
					 {
						 ReportNum = element;
						 break;
					 }
				 }
				 
				 for(String element : elements)
				 {
					 if(element.matches("[0-9/]+") && element.contains("/"))
					 {
						 DateReported = element;
						 break;
					 }
				 }
				 
				 IncidentType = lines[1].toString();
				 Location = lines[2].toString();
				 
				 if(record.contains("Open"))
				 {
					 Disposition = "Open";
				 }
				 if(record.contains("Arrest"))
				 {
					 Disposition = "Arrest";
				 }
				 if(record.contains("Judicial Referral"))
				 {
					 Disposition = "Judicial Referral";
				 }
				 if(record.contains("Closed"))
				 {
					 Disposition = "Closed";
				 }
				 
				 UniversityData recordData = new UniversityData();
			      recordData.IncidentNumber = ReportNum;
		          recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy") ;
		          recordData.OffenseDateTime=  dateToConvert( DateReported,"MM/dd/yyyy") ;
		          recordData.Location= Location;
		          recordData.IncidentType= IncidentType;
		          recordData.Disposition= Disposition;
		          finalRecordsList.add(recordData);
				 
			 }
		 }
		 ExtractionClass.BostonUniversityExtractionMethod(finalRecordsList);	
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	
}
public static void IllinoisInstituteofTechnology(String BaseURL)
{
	String URL =BaseURL;
	try {
		Document document = Jsoup.connect(URL).get();
		 Elements mainDivs = document.select("span:contains(Incident Type:)");
		 Elements secondaryDivs = document.select("span:contains(ILLINOIS INSTITUTE OF TECHNOLOGY :)");
		 Elements nextDiv = document.select("span:contains(Disposition:");

		 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 
		 ArrayList<String> list1 = new ArrayList<String>();
		 ArrayList<String> list2 = new ArrayList<String>();
		 ArrayList<String> list3 = new ArrayList<String>();
       for(Element element : mainDivs)
       {
    	   list1.add(element.text());
       }
       for(Element element : secondaryDivs)
       {
    	   list2.add(element.text());
       }
       for(Element element : nextDiv)
       {
    	   list3.add(element.text());
       }
		 ArrayList<String> modifiedList = new ArrayList<String>();
			for(int i=0;i<=list1.toArray().length-1;i++)
			{
				String text = list1.get(i).toString()+"\n"+list2.get(i).toString()+"\n"+list3.get(i).toString();
				modifiedList.add(text);
			}
		
		 Iterator<String> iterator = modifiedList.iterator();
		 while(iterator.hasNext())
		 {
			   // String ReportNum="";
	        	//String DateReported="";
	        	String Location="";
	        	String DateOccurred = "";
	        	String IncidentType = "";
	        	String Disposition = "";
			 String record = iterator.next().toString();
			 String[] lines = record.split("\n");
			 if(lines[0].contains("Incident Type:"))
			 {
				 IncidentType = lines[0].toString().replace("Incident Type:", "").trim();
			 }
			 if(lines[1].contains("ILLINOIS INSTITUTE OF TECHNOLOGY :"))
			 {
				String locationElements = lines[1].toString().replace("ILLINOIS INSTITUTE OF TECHNOLOGY :", "").trim();
				String[] items = locationElements.split(":");
				Location = items[items.length-1].toString();
				String[] dateElements = Location.split(" ");
				int indexNum=0;
				for(int j=0;j<=dateElements.length-1;j++)
				{
					if(dateElements[j].contains("/") && dateElements[j].matches("[0-9/]+") )
					{
						indexNum =j;
						break;
					}
				}
				
				for(int k=indexNum;k<=dateElements.length-1;k++)
				{
					DateOccurred =DateOccurred+ dateElements[k].toString();
				}
				Location = "";
			 }
			 
			 if(lines[2].contains("Disposition:"))
			 {
				 Disposition = lines[2].toString().replace("Disposition:", "");
			 }
			 
			 Random ran = new Random();
				int randomNum = ran.nextInt(1000000000) + 1;
			UniversityData recordData = new UniversityData();
		     recordData.IncidentNumber = "RN_"+randomNum;
	          recordData.ReportedDateTime= null;
	          recordData.OffenseDateTime= null;
	          recordData.Location= Location;
	          recordData.IncidentType= IncidentType;
	          recordData.Disposition= Disposition;
	          finalRecordsList.add(recordData);
		 }
		 ExtractionClass.IllinoisInstituteofTechnologyExtractionMethod(finalRecordsList);
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void LehighUniversity(String BaseURL)
{
	String URL = BaseURL;
	try {
		Document document = Jsoup.connect(URL).get();
		 Elements divsContent = document.select("div.views-row");
		 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 for(Element divContent : divsContent)
		 {
			 
			 Elements contentFields = divContent.select("div.views-field");
			 Element DateReportedElements =  contentFields.select("div:contains(Reported on:)").first();
			 Element DateOccurredElements = contentFields.select("div:contains(Incident Date/Time:)").first();
			 Element DispositionElements = contentFields.select("div:contains(Disposition:)").first();
			 Element IncidentTypeElements = contentFields.select("div:contains(Incident Type:)").first();
			 Element LocationElements = contentFields.select("div:contains(Incident Location:)").first();
			 Element ReportNumberElements = contentFields.select("div:contains(Report Number:)").first();
			 
			 String DateReportedText =  DateReportedElements.text();
			 String DateOccurredText = DateOccurredElements.text();
			 String DispositionText = DispositionElements.text();
			 String IncidentTypeText = IncidentTypeElements.text();
			 String LocationText = LocationElements.text();
			 String ReportNumberText = ReportNumberElements.text();
			 
			 ArrayList<String> list1 = new ArrayList<String>();
			 ArrayList<String> list2 = new ArrayList<String>();
			 ArrayList<String> list3 = new ArrayList<String>();
			 ArrayList<String> list4 = new ArrayList<String>();
			 ArrayList<String> list5 = new ArrayList<String>();
			 ArrayList<String> list6 = new ArrayList<String>();
			 
			 String[] lines1 = DateReportedText.split("\n");
			 for(int i=0;i<= lines1.length - 1;i++)
			 {
				 list1.add(lines1[i].toString());
			 }
			 
			 String[] lines2 = DateOccurredText.split("\n");
			 for(int i=0;i<= lines2.length - 1;i++)
			 {
				 list2.add(lines2[i].toString());
			 }
			 
			 String[] lines3 = DispositionText.split("\n");
			 for(int i=0;i<= lines3.length - 1;i++)
			 {
				 list3.add(lines3[i].toString());
			 }
			 
			 String[] lines4 = IncidentTypeText.split("\n");
			 for(int i=0;i<= lines4.length - 1;i++)
			 {
				 list4.add(lines4[i].toString());
			 }
			 
			 String[] lines5 = LocationText.split("\n");
			 for(int i=0;i<= lines5.length - 1;i++)
			 {
				 list5.add(lines5[i].toString());
			 }
			 String[] lines6 =  ReportNumberText.split("\n");
			 for(int i=0;i<= lines6.length - 1;i++)
			 {
				 list6.add(lines6[i].toString());
			 }
			 Boolean validRecords = false;
			 int listLength = list1.toArray().length;
	if((list2.toArray().length == listLength) && (list3.toArray().length == listLength) && (list4.toArray().length == listLength) 
			&& (list5.toArray().length == listLength) && (list6.toArray().length == listLength))
	{
		validRecords = true;
	}
	
	if(validRecords)
	{
		for(int i=0;i<= listLength-1;i++)
		{
			String ReportNum="";
        	String DateReported="";
        	String Location="";
        	String DateOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	
			  DateReported =list1.get(i).toString().replace("Reported on:", "").trim();
			  DateOccurred =list2.get(i).toString().replace("Incident Date/Time:", "").trim();
			  Disposition =list3.get(i).toString().replace("Disposition:", "").trim();
		      IncidentType =list4.get(i).toString().replace("Incident Type:", "").trim();
			  Location =list5.get(i).toString().replace("Incident Location:", "").trim();
			  ReportNum = list6.get(i).toString().replace("Report Number:", "").trim();
			  
				
				UniversityData recordData = new UniversityData();
			     recordData.IncidentNumber = ReportNum;
		          recordData.ReportedDateTime=  dateToConvert( DateReported,"E, dd MMMMM yyyy - h:mma") ;
		          recordData.OffenseDateTime=  dateToConvert( DateOccurred,"E, dd MMMMM yyyy - h:mma") ;
		          recordData.Location= Location;
		          recordData.IncidentType= IncidentType;
		          recordData.Disposition= Disposition;
		          finalRecordsList.add(recordData);
			 
		}
	}
	}
		 ExtractionClass.LehighUniversityExtractionMethod(finalRecordsList); 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	

}
public static void UniversityofNewMexico(String BaseURL)
{
String URL = BaseURL;
	
	try {
		Document document = Jsoup.connect(URL).get();
		Element linkHTML = document.select("a:contains(Daily Crime Log Report").first();
		String urlLink = linkHTML.attr("href");
		Document document1 = Jsoup.connect(urlLink).get();
		Elements divElements = document1.select("div.IncidentEntry");
		 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		for(Element divContent : divElements)
		{
			Elements TitleElement = divContent.select("span.IncidentMapListingTitle");
			Elements LocationElement = divContent.select("span#IncidentMapLocation");
			Elements DRElement = divContent.select("span#NSDateSubmitted");
			Elements DOElement = divContent.select("span#NSIncidentDateLabel");
			Elements ReportNumElement = divContent.select("span#NSAdditionalListingField");
			
			String TitleText = TitleElement.text();
			String LocationText = LocationElement.text();
			String DRText = DRElement.text();
			String DOText = DOElement.text();
			String ReportNumText = ReportNumElement.text();
			if(LocationText.isEmpty())
			{
				Elements AltLocationElement = divContent.select("span#IncidentMapListingAddress");
				LocationText = AltLocationElement.text();
			}
			
			if(LocationText.contains("Location:"))
			{
				LocationText = LocationText.replace("Location:","").trim();
			}
			
			if(DRText.contains("Date Entered:"))
			{
				DRText = DRText.replace("Date Entered:","").trim();
			}
			
			if(DOText.contains("Incident Date:"))
			{
				DOText = DOText.replace("Incident Date:","").trim();
			}
			if(!ReportNumText.isEmpty())
			{
			UniversityData recordData = new UniversityData();
		      recordData.IncidentNumber = ReportNumText;
	          recordData.ReportedDateTime=  dateToConvert( DRText,"MM/dd/yyyy h:mm:ss a") ;
	          recordData.OffenseDateTime=  dateToConvert( DOText,"MM/dd/yyyy HHmm") ;
	          recordData.Location= LocationText;
	          recordData.IncidentType= TitleText;
	          recordData.Disposition= "NOT AVAILABLE";
	          finalRecordsList.add(recordData);
			}
		}
		ExtractionClass.UniversityofNewMexicoExtractionMethod(finalRecordsList);
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void UniversityofNebraska(String BaseURL)
{
	String URL =BaseURL;
	try {
		Document document = Jsoup.connect(URL).get();
		 Element mainDiv = document.select("div#ctl00_ContentPlaceHolder1_SummarySection_HTML").first();
		 Elements eachRecordDiv = mainDiv.select("li");
		 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 for(Element eachDiv : eachRecordDiv)
		 {
			  String ReportNum="";
	          String DateReported="";
	          String Location="";
	          String DateOccurred = "";
	          String IncidentType = "";
	          String Disposition = "";
		 Element firstElement = eachDiv.select("span:contains(Incident Code)").first();
		 Element element1= firstElement.nextElementSibling();
		 Element secondElement = eachDiv.select("span:contains(Reported)").first();
		 Element element2= secondElement.nextElementSibling();
		 Element thirdElement = eachDiv.select("span:contains(Case Status:)").first();
		 Element element3= thirdElement.nextElementSibling();
		 Element fourthElement = eachDiv.select("span:contains(Occurred)").first();
		 Element element4= fourthElement.nextElementSibling();
	     Element fifthElement = eachDiv.select("span:contains(Location:)").first();
	     Element element5= fifthElement.nextElementSibling();
	     Element sixthElement = eachDiv.select("a").first();
	    ReportNum=sixthElement.text();
     	DateReported=element2.text();
     	Location=element5.text();
     	DateOccurred = element4.text();
     	IncidentType = element1.text();
     	Disposition = element3.text();
     	if(DateOccurred.contains(":"))
     	{
     		int beginIndex = DateOccurred.indexOf(":");
     		int endIndex = DateOccurred.length()-1;
     		DateOccurred = DateOccurred.substring(beginIndex, endIndex);
     	}
     	
     	if(DateReported.contains("and"))
    	{
    	String[] values = DateReported.split("and");
    	DateReported = values[0].toString().replaceAll(":","").trim();
    	}
     	
     	if(DateOccurred.contains("and"))
    	{
    	String[] values = DateOccurred.split("and");
    	DateOccurred = values[0].toString().replaceAll(":","").trim();
    	}
     	
     	UniversityData recordData = new UniversityData();
	     recordData.IncidentNumber = ReportNum;
        recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyyHHmm") ;
        recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyyHHmm") ;
        recordData.Location= Location;
        recordData.IncidentType= IncidentType;
        recordData.Disposition= Disposition;
        finalRecordsList.add(recordData);
     	
     	
		 }
		 ExtractionClass.UniversityofNebraskaExtractionMethod(finalRecordsList);		 
		 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void UniversityofMichigan(String BaseURL)
{
    String url = BaseURL;

	try {
		     InputStream input = new URL(url).openStream();
		    
		     BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
		     String text = reader.readLine();
		     
		     if(text != null)
		     {
		     String[] lines = text.split("}");
		     

			 ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		     
		     for(String line: lines)
		     {
		    	 if(line.contains("\"id\""))
		    	 {
		    	 int startIndex = line.indexOf("\"id\"");
		    	 line = line.substring(startIndex);
		    	 String[] elements = line.split(",");
		    	 String ReportNum="";
		         String DateReported="";
		         String Location="";
		         String DateOccurred = "";
		         String IncidentType = "";
		         String Disposition = "";
		    	 for(String element : elements)
		    	 {
		    		
		    		 if(element.contains("\"id\""))
		    		 {
		    			 int beginIndex = element.indexOf(":")+1;
		    			 int endIndex = element.lastIndexOf("\"");
		    			 ReportNum = element.substring(beginIndex,endIndex).replaceAll("\"", "");
		    		 }
		    		 
		    		 if(element.contains("\"date\""))
		    		 {
		    			 int beginIndex = element.indexOf(":")+1;
		    			 int endIndex = element.lastIndexOf("\"");
		    			 DateOccurred = element.substring(beginIndex,endIndex).replaceAll("\"", "");
		    		 }
		    		 if(element.contains("\"description\""))
		    		 {
		    			 int beginIndex = element.indexOf(":")+1;
		    			 int endIndex = element.lastIndexOf("\"");
		    			 IncidentType = element.substring(beginIndex,endIndex).replaceAll("\"", "");
		    		 }
		    		 if(element.contains("\"location\""))
		    		 {
		    			 int beginIndex = element.indexOf(":")+1;
		    			 int endIndex = element.lastIndexOf("\"");
		    			 Location = element.substring(beginIndex,endIndex).replaceAll("\"", "");
		    		 }
		    		 if(Location.isEmpty() && element.contains("\"address\"") )
		    		 {
		    			 int beginIndex = element.indexOf(":")+1;
		    			 int endIndex = element.lastIndexOf("\"");
		    			 Location = element.substring(beginIndex,endIndex).replaceAll("\"", "");
		    		 }
		    		 if(element.contains("\"disposition\""))
		    		 {
		    			 int beginIndex = element.indexOf(":")+1;
		    			 int endIndex = element.lastIndexOf("\"");
		    			 Disposition = element.substring(beginIndex,endIndex).replaceAll("\"", "");
		    			 if(Disposition.contains("<br>"))
		    			 {
		    				 Disposition = Disposition.replace("<br>","");
		    			 }
		    		 }    		 
		    	 }
		    	 int index = BaseURL.indexOf("=")+1;
		    	 DateReported = BaseURL.substring(index);
		    	 UniversityData recordData = new UniversityData();
			     recordData.IncidentNumber = ReportNum;
		         recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy") ;
		         recordData.OffenseDateTime=  dateToConvert( DateOccurred.replaceAll(" ", ""),"yyyy-MM-ddHH:mm:ss.S") ;
		         recordData.Location= Location;
		         recordData.IncidentType= IncidentType;
		         recordData.Disposition= Disposition;
		        finalRecordsList.add(recordData);
		    	 
		    	 }
		     }
		     ExtractionClass.UniversityofMichiganExtractionMethod(finalRecordsList);	
		     }
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static String UniversityofIllinois_UC(String BaseURL)
{
	String url = BaseURL;
	String return_URL="";
	try {
	     InputStream input = new URL(url).openStream();
	     BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
	     String text = reader.readLine();
	    
	     while(text != null)
	     {
	    	if(text.contains("Clery Report, last 60 days")) 
	    	{
	    	
	    	String[] elements =  text.split("<a");
	    	for(String element : elements)
	    	{
	    		if(element.contains("href"))
	    		{
	    			int startIndex = element.indexOf("https://illinois.edu/blog/files");
	    			int endIndex = element.indexOf(".pdf")+4;
	    			element = element.substring(startIndex, endIndex);
	    			return_URL = element;
	    			break;
	    		}
	    	}
	    	}
	    	
	    	 text = reader.readLine();
	    	 
	     }
	    
	}
	catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return return_URL;
}
public static String YaleUniversity(String BaseURL)
{
	String URL = BaseURL;
	String return_URL="";
	try {
		Document document = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
		Element divContent = document.select("div.item-list").first();
		Element urlElement = divContent.select("a").first();
		String firstUrl = "https://your.yale.edu/"+urlElement.attr("href");
		
		Document document2 = Jsoup.connect(firstUrl).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
		Element divContent2 = document2.select("div.item-list").first();
		Element urlElement2 = divContent2.select("a").first();
		String secondUrl = "https://your.yale.edu/"+urlElement2.attr("href");
		 
		Document document3 = Jsoup.connect(secondUrl).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
		Element divContent3 = document3.select("span.file").first();
		Element urlElement3 = divContent3.select("a").first();
		String  thirdUrl = urlElement3.attr("href");
		return_URL = thirdUrl;
		
		
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return return_URL;
	
}
public static String OregonStateUniversity(String BaseURL)
{
	String URL = BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).get();
		Element tableContent = document.select("table").first();
		Element linkContent = tableContent.select("a:contains(Clery Crime Log").first();
		URL_return =  linkContent.attr("href").replaceAll(" ","%20");
		
		 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static String UniversityofTennessee(String BaseURL)
{
	String URL = BaseURL;
	String URL_return="";
	try {
		Document document = Jsoup.connect(URL).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
		Element node = document.select("a:contains(60 Day Crime Log)").first();
		 URL_return =  node.attr("href");
		 
	} catch (IOException e) {
		e.printStackTrace();
		
	}
	catch (NullPointerException e) {
		e.printStackTrace();
	}
	return URL_return;
}
public static void CarnegieMellonUniversity(String BaseURL)
{
	String URL = BaseURL;
	
	try {
		Document document = Jsoup.connect(URL).get();
		Elements divContent = document.select("div.red");
		Elements nodes = divContent.select("p");
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		for(Element node : nodes)
		{
	    	String DateReported="";
	    	String Location="NOT AVAILABLE";
	    	String DateOccurred = "";
	    	String IncidentType = "";
	    	String Disposition = "NOT AVAILABLE";
		 String[] elements = node.text().split("-");
		 if(elements.length == 2)
		 {
			 DateReported = elements[0].toString().trim();
			 IncidentType = elements[1].toString().trim();
			 DateOccurred = DateReported;
		 }
		 
		 Random ran = new Random();
		int randomNum = ran.nextInt(1000000000) + 1;
		UniversityData recordData = new UniversityData();
	    recordData.IncidentNumber = "RN_"+randomNum;
       recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy") ;
       recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy") ;
       recordData.Location= Location;
       recordData.IncidentType= IncidentType;
       recordData.Disposition= Disposition;
       finalRecordsList.add(recordData);
		}
		ExtractionClass.CarnegieMellonUniversityExtractionMethod(finalRecordsList);		 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void StateUniversityofNewYorkatBuffalo(String BaseURL)
{
String URL = BaseURL;
	
try {
	Document document = Jsoup.connect(URL).get();
	 Element dateReportedElement = document.select("p:contains(Report Date:)").first();
	 Element tableContent = document.select("table").first();
	 Elements rowsContent = tableContent.select("tr");

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	for(Element rowContent : rowsContent)
	 {
		String ReportNum="";
	    	//String DateReported="";
	    	//String TimeReported="";
	    	String Location="";
	    	String DateOccurred = "";
	    	String TimeOccurred = "";
	    	String IncidentType = "";
	    	//String Disposition = "";
	Elements cellContent = rowContent.select("td");
	StringBuilder builder = new StringBuilder();
	for(Element eachCellContent : cellContent )
	{
		builder.append(eachCellContent.text()+",,,");
		
	}
	
	String[] elements = builder.toString().split(",,,");
	if(elements.length == 5)
	{
	ReportNum = elements[0].toString();
    DateOccurred = elements[1].toString();
    TimeOccurred = elements[2].toString();
    IncidentType = elements[3].toString();
    Location = elements[4].toString();	    	 
  UniversityData recordData = new UniversityData();
String dr = DateOccurred;
		String oc = DateOccurred+" - "+TimeOccurred;
	 recordData.IncidentNumber = ReportNum;
	 recordData.ReportedDateTime=  dateToConvert( dr,"MM/dd/yyyy") ;
	 recordData.OffenseDateTime=  dateToConvert( oc,"MM/dd/yyyy - H:mm:ss") ;
	 recordData.Location= Location;
	 recordData.IncidentType= IncidentType;
	 recordData.Disposition= "NOT AVAILABLE";
	 finalRecordsList.add(recordData);
	}	 
	 }
		 ExtractionClass.StateUniversityofNewYorkatBuffaloExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void TempleUnivesity(String BaseURL)
{
	String URL = BaseURL;
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			if(elements.length == 7)
			{
			
			String IncidentType = elements[0].toString();
			String IncidentNum = elements[1].toString();
			String DateReported = elements[2].toString();
			String DateOccurred = elements[3].toString();
			String Location = elements[4].toString();
			String Disposition = elements[6].toString();
			if(DateReported.contains("to"))
			{
				String[] values = DateReported.split("to");
				DateReported = values[0];
			}
			if(DateOccurred.contains("to"))
			{
				String[] values = DateOccurred.split("to");
				DateOccurred = values[0];
			}
			UniversityData recordData = new UniversityData();
			 recordData.IncidentNumber = IncidentNum;
			 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yy h:mm a") ;
			 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yy h:mm a") ;
			 recordData.Location= Location;
			 recordData.IncidentType= IncidentType;
			 recordData.Disposition= Disposition;
			 finalRecordsList.add(recordData);
			}
		}
		ExtractionClass.TempleUnivesityExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	
}
public static void NewYorkUniversity(String BaseURL)
{
	String URL = BaseURL;
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			if(elements.length == 7)
			{
			
				String IncidentNum = elements[0].toString();
				String DateReported = elements[1].toString();
				String DateOccurred = elements[2].toString();
				String IncidentType = elements[3].toString();
				String Location = elements[4].toString();
				String Summary = elements[5].toString();
				String Disposition = elements[6].toString();
			
			UniversityData recordData = new UniversityData();
			
			String dr = DateReported;
			String oc = DateOccurred;
			
			if(dr.contains("-"))
			{
				String[] dateValues = dr.split("-");
				String dateValue = dateValues[0];
				dr = dateValue;
			   
			}
			if(dr.contains("p.m."))
			{
				dr = dr.replaceAll("p.m.","pm");
			}
			if(dr.contains("a.m."))
			{
				dr = dr.replaceAll("a.m.","am");
			}
			
			if(oc.contains("-"))
			{
				String[] dateValues = oc.split("-");
				String dateValue = dateValues[0];
				oc = dateValue;
			   
			}
			if(oc.contains("p.m."))
			{
				oc = oc.replaceAll("p.m.","pm");
			}
			if(oc.contains("a.m."))
			{
				oc = oc.replaceAll("a.m.","am");
			}
			 recordData.IncidentNumber = IncidentNum;
			 recordData.ReportedDateTime=  dateToConvert( dr,"dd/MM/yyyy h:mm a") ;
			 recordData.OffenseDateTime=  dateToConvert( oc,"dd/MM/yyyy h:mm a") ;
			 recordData.Location= Location;
			 recordData.IncidentType= IncidentType;
			 recordData.Disposition= Disposition;
			 finalRecordsList.add(recordData);
			}
		}
		ExtractionClass.NewYorkUniversityExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	
}


public static void CornellUniversity(String BaseURL)
{
	String URL = BaseURL;
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			if(elements.length == 9)
			{
			
				String IncidentType = elements[0].toString();
				String IncidentNum = elements[1].toString();
				String DateReported = elements[2].toString();
				String DateOccurred = elements[3].toString();
				String Location = elements[4].toString();
				String LocationType = elements[5].toString();
				String CrimeType = elements[6].toString();
				String Summary = elements[7].toString();
				String Disposition = elements[8].toString();
			
			UniversityData recordData = new UniversityData();
			 recordData.IncidentNumber = IncidentNum;
			 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/d/yyyy H:mm") ;
			 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/d/yyyy H:mm") ;
			 recordData.Location= Location;
			 recordData.IncidentType= IncidentType;
			 recordData.Disposition= Disposition;
			 finalRecordsList.add(recordData);
			}
		}
		ExtractionClass.CornellUniversityExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	
}
public static void UniversityofChicago(String BaseURL)
{
	String URL = BaseURL;
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 Document document = Jsoup.connect(URL).get();
		 Elements linksContent = document.select("a");
		 String URL_main = "";
		 for(Element linkContent : linksContent)
		 {
			 String link = linkContent.attr("href");
			 if(link.contains("https://incidentreports.uchicago.edu/incidentReportArchive.php"))
			 {
				 URL_main = link;
				 break;
			 }
			 }
		 Document document1 = Jsoup.connect(URL_main).get();
		 Element tableContent = document1.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			if(elements.length == 7)
			{
			
				String IncidentType = elements[0].toString();
				String Location = elements[1].toString();
				String DateReported = elements[2].toString();
				String DateOccurred = elements[3].toString();
				String Comments = elements[4].toString();
				String Disposition = elements[5].toString();
				String IncidentNum = elements[6].toString();
			
			UniversityData recordData = new UniversityData();
			 recordData.IncidentNumber = IncidentNum;
			 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yy h:mm a") ;
			 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yy h:mm a") ;
			 recordData.Location= Location;
			 recordData.IncidentType= IncidentType;
			 recordData.Disposition= Disposition;
			 finalRecordsList.add(recordData);
			}
		}
		ExtractionClass.UniversityofChicagoExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void UniversityofKentucky(String BaseURL)
{
	String URL = BaseURL;
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		Document document = Jsoup.connect(URL).get();
		
			 Element tableContent = document.select("table").first();
			 
			Elements rowsContent = tableContent.select("tr");
			for(Element rowContent: rowsContent)
			{
				Elements cellsContent = rowContent.select("td");
				StringBuilder builder = new StringBuilder();
				for(Element cellContent :cellsContent )
				{
					builder.append(cellContent.text()+";;;");
				}
				String[] elements = builder.toString().split(";;;");
				
				if(elements.length == 8)
				{
					String IncidentType = elements[0].toString();
					String IncidentNum = elements[1].toString();
					String DateOccurred = elements[2].toString();
					String DateReported = elements[3].toString();
					String Location = elements[4].toString();
					String Disposition = elements[7].toString();
			
			UniversityData recordData = new UniversityData();
			 recordData.IncidentNumber = IncidentNum;
			 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy") ;
			 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy - h:mma") ;
			 recordData.Location= Location;
			 recordData.IncidentType= IncidentType;
			 recordData.Disposition= Disposition;
			 finalRecordsList.add(recordData);
			}
		}
		ExtractionClass.UniversityofKentuckyExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void UniversityofFlorida(String BaseURL)
{
String URL = BaseURL;
	
	try {
		 Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();

			ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			
			if(elements.length == 9)
			{
				String IncidentNum = elements[1].toString();
				String DateReported = elements[2].toString()+"  "+elements[3].toString();
				String DateOccurred = elements[4].toString();
				String IncidentType = elements[5].toString();
				String Location = elements[7].toString();
				String Disposition = elements[8].toString();
				
				
				UniversityData recordData = new UniversityData();
				 recordData.IncidentNumber = IncidentNum;
				 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy H:mm") ;
				 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy");
				 recordData.Location= Location;
				 recordData.IncidentType= IncidentType;
				 recordData.Disposition= Disposition;
				 finalRecordsList.add(recordData);
				
			}
		}
		ExtractionClass.UniversityofFloridaExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void UniversityofNorthCarolinaatChapelHill(String BaseURL)
{
	String URL = BaseURL;
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table[border=1]").first();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			
			if(elements.length == 7)
			{
				String IncidentNum = elements[0].toString();
				String DateReported = elements[1].toString();
				String Location = elements[2].toString();
				String DateOccurred = elements[3].toString();
				String IncidentType = elements[4].toString();
				String Disposition = elements[6].toString();
				switch(Disposition)
				{
				
				case "1" :
					Disposition ="UNFOUNDED";
					break;
				case "2" :
					Disposition ="CLEARED BY ARREST";
					break;
				case "3" :
					Disposition ="CLEARED BY ARREST BY ANOTHER AGENCY";
					break;
				case "4" :
					Disposition ="DEATH OF OFFENDER";
					break;
				case "5" :
					Disposition ="VICTIM REFUSED TO COOPERATE";
					break;
				case "6" :
					Disposition ="PROSECUTION DECLINED";
					break;
				case "7" :
					Disposition ="EXTRADITION DECLINED";
					break;
				case "8" :
					Disposition ="JUVENILE NO CUSTODY";
					break;
				case "9" :
					Disposition ="MISSING PERSON RETURNED";
					break;
				}
				
				UniversityData recordData = new UniversityData();
				 recordData.IncidentNumber = IncidentNum;
				 recordData.ReportedDateTime=  dateToConvert( DateReported,"MMMMM dd, yyyy HHmm") ;
				 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MMMMM dd, yyyy HHmm") ;
				 recordData.Location= Location;
				 recordData.IncidentType= IncidentType;
				 recordData.Disposition= Disposition;
				 finalRecordsList.add(recordData);
			}
		}
		ExtractionClass.UniversityofNorthCarolinaatChapelHillExtractionMethod(finalRecordsList);	 
		} catch (IOException e) {
			e.printStackTrace();
		}catch (NullPointerException e) {
			e.printStackTrace();
		}
}
public static void UniversityofArizona(String BaseURL)
{
String URL = BaseURL;
	
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			if(elements.length == 6)
			{
				String IncidentNum = elements[0].toString();
				String DateReported = elements[1].toString();
				String DateOccurred = elements[2].toString();
				String IncidentType = elements[3].toString();
				String Location = elements[5].toString();
				String Disposition = "";
				
				UniversityData recordData = new UniversityData();
				 recordData.IncidentNumber = IncidentNum;
				 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy - H:mm") ;
				 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy - H:mm") ;
				 recordData.Location= Location;
				 recordData.IncidentType= IncidentType;
				 recordData.Disposition= Disposition;
				 finalRecordsList.add(recordData);
		
			}
		}
	 ExtractionClass.UniversityofArizonaExtractionMethod(finalRecordsList);	 
} catch (IOException e) {
	e.printStackTrace();
}catch (NullPointerException e) {
	e.printStackTrace();
}	
}
public static void UniversityofSouthFlorida(String BaseURL)
{
	String URL = BaseURL;
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			
			if(elements.length == 7)
			{
				String IncidentNum = elements[0].toString();
				String DateOccurred = elements[1].toString();
				String DateReported = elements[2].toString();
				String IncidentType = elements[3].toString();
				String Location = elements[4].toString();
				String Disposition = elements[6].toString();
				
				UniversityData recordData = new UniversityData();
				 recordData.IncidentNumber = IncidentNum;
				 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/d/yyyy h:mm:ss a");
				 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/d/yyyy h:mm:ss a");
				 recordData.Location= Location;
				 recordData.IncidentType= IncidentType;
				 recordData.Disposition= Disposition;
				 finalRecordsList.add(recordData);
			}
		}
		 ExtractionClass.UniversityofSouthFloridaExtractionMethod(finalRecordsList);	 
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	} 
}
public static void OhioStateUniversity(String BaseURL)
{
String URL = BaseURL;
	
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			
			if(elements.length == 7)
			{
				String IncidentNum = elements[0].toString();
				String DateOccurred = elements[1].toString();
				String DateReported = elements[2].toString();
				String IncidentType = elements[3].toString();
				String Location = elements[4].toString();
				String Disposition = elements[6].toString();
				
				UniversityData recordData = new UniversityData();
				 recordData.IncidentNumber = IncidentNum;
				 recordData.ReportedDateTime=  dateToConvert( DateReported,"dd-MM-yyyy H:mm") ;
				 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"dd-MM-yyyy H:mm") ;
				 recordData.Location= Location;
				 recordData.IncidentType= IncidentType;
				 recordData.Disposition= Disposition;
				 finalRecordsList.add(recordData);
			}
		}
		ExtractionClass.OhioStateUniversityExtractionMethod(finalRecordsList);	
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void UniversityofMarylandCollegePark(String BaseURL)
{
	String URL = BaseURL;
	try {

		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		Iterator iterator = rowsContent.iterator();
		Element heading = (Element) iterator.next();
		while(iterator.hasNext())
		{
			Element rowContent1 = (Element) iterator.next();
			Elements cellsContent = rowContent1.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			
			if(iterator.hasNext())
			{
			Element rowContent2 = (Element) iterator.next();
			
			Elements cellsContent2 = rowContent2.select("td");
			for(Element cellContent :cellsContent2 )
			{
				builder.append(cellContent.text()+";;;");
			}
			}
			
           String[] elements = builder.toString().split(";;;");
			
			if(elements.length == 6)
			{
				String IncidentNum = elements[0].toString();
				String DateOccurred = elements[1].toString();
				String DateReported = elements[2].toString();
				String IncidentType = elements[3].toString();
				String Disposition = elements[4].toString();
				String Location = elements[5].toString();
				
				UniversityData recordData = new UniversityData();
				 recordData.IncidentNumber = IncidentNum;
				 recordData.ReportedDateTime= dateToConvert( DateReported,"MM/dd/yy HH:mm");
				 recordData.OffenseDateTime=  dateToConvert(DateOccurred,"MM/dd/yy HH:mm");
				 recordData.Location= Location;
				 recordData.IncidentType= IncidentType;
				 recordData.Disposition= Disposition;
				 finalRecordsList.add(recordData);
				
			}
		}
		ExtractionClass.UniversityofMarylandCollegeParkExtractionMethod(finalRecordsList);	
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	
}
public static void UniversityofCincinnati(String BaseURL)
{
	String URL = BaseURL;
	try {
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").first();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			if(elements.length == 7)
			{
				String IncidentNum = elements[2].toString();
				String DateReported = elements[1].toString();
				String DateOccurred = elements[1].toString();
				String IncidentType = elements[4].toString();
				String Location = elements[5].toString();
				String Disposition = elements[6].toString()+" Arrest";
				

				UniversityData recordData = new UniversityData();
				 recordData.IncidentNumber = IncidentNum;
				 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy h:mm:ss a") ;
				 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy h:mm:ss a") ;
				 recordData.Location= Location;
				 recordData.IncidentType= IncidentType;
				 recordData.Disposition= Disposition;
				 finalRecordsList.add(recordData);
			}
		}
		ExtractionClass.UniversityofCincinnatiExtractionMethod(finalRecordsList);
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}
public static void AuburnUniversity(String BaseURL)
{
	String URL = BaseURL;
	try {
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 Document document = Jsoup.connect(URL).get();
		 Elements tablesContent = document.select("table");
		 Element tableContent = tablesContent.get(2);
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			if(elements.length >= 12)
			{
				String IncidentNum = elements[0].toString();
				String DateReported = elements[2].toString();
				String DateOccurred = elements[3].toString();
				String IncidentType = elements[7].toString();
				String Location = elements[10].toString();
				String Disposition = elements[11].toString();
				

				UniversityData recordData = new UniversityData();
				 recordData.IncidentNumber = IncidentNum;
				 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy") ;
				 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy") ;
				 recordData.Location= Location;
				 recordData.IncidentType= IncidentType;
				 recordData.Disposition= Disposition;
				 finalRecordsList.add(recordData);
				
			}
		}
		ExtractionClass.AuburnUniversityExtractionMethod(finalRecordsList);	
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	
}
public static void RiceUniversity(String BaseURL)
{
	String URL = BaseURL;
	try {
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		 Document document = Jsoup.connect(URL).get();
		 Element tableContent = document.select("table").last();
		Elements rowsContent = tableContent.select("tr");
		for(Element rowContent: rowsContent)
		{
			Elements cellsContent = rowContent.select("td");
			StringBuilder builder = new StringBuilder();
			for(Element cellContent :cellsContent )
			{
				builder.append(cellContent.text()+";;;");
			}
			String[] elements = builder.toString().split(";;;");
			if(elements.length == 8)
			{
				String IncidentNum = elements[1].toString();
				String DateReported = elements[3].toString();
				String DateOccurred = elements[4].toString();
				String IncidentType = elements[0].toString();
				String Location = elements[2].toString();
				String Disposition = elements[6].toString();
				
				String[] drElements = DateReported.split(" ");
				String[] ocElements = DateOccurred.split(" ");
				if(drElements.length >=1)
				{
					DateReported = drElements[0];
				}
				if(ocElements.length >=1)
				{
					DateOccurred = ocElements[0];
				}
					
				UniversityData recordData = new UniversityData();
				 recordData.IncidentNumber = IncidentNum;
				 recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy") ;
				
				 recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy") ;
				 recordData.Location= Location;
				 recordData.IncidentType= IncidentType;
				 recordData.Disposition= Disposition;
				 finalRecordsList.add(recordData);
			}
		}

		ExtractionClass.RiceUniversityExtractionMethod(finalRecordsList);
	} catch (IOException e) {
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
}

private static void DownloadPDF(String key,String value) {
	String UniversityName = key;
	String URL_university = value; 
	System.out.println(value);
	try {
		if(!URL_university.isEmpty())
	   {
		 URL url = new URL(URL_university);
		
		  HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		  httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		  httpConn.connect();
	    int responseCode = httpConn.getResponseCode();
	  
	   if (responseCode == HttpURLConnection.HTTP_OK) {
	       InputStream inputStream = httpConn.getInputStream();
	    	File dir = new File("D:\\CrimeReportPDFS");
	    	dir.mkdirs();
	    	 String saveFilePath = "D:\\CrimeReportPDFS\\"+UniversityName+"CrimeReport.pdf";
	    	 FileOutputStream outputStream = new FileOutputStream(saveFilePath,false);
	    
	           int BUFFER_SIZE = 4096;
	           int bytesRead = -1;
	           byte[] buffer = new byte[BUFFER_SIZE];
	           while ((bytesRead = inputStream.read(buffer)) != -1) {
	               outputStream.write(buffer, 0, bytesRead);
	           }
	           outputStream.close();
	           inputStream.close();
	         
	           Method method =	ContentExtraction.class.getDeclaredMethod(UniversityName+"ExtractionMethod",null);
	           method.invoke(ExtractionClass,null);
	           
	    
	           }
	} 
	}catch (IOException e) {
		e.printStackTrace();
	} catch (NoSuchMethodException e) {
		
		e.printStackTrace();
	} catch (SecurityException e) {
		
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		
		e.printStackTrace();
	} catch (IllegalArgumentException e) {
		
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		
		e.printStackTrace();
	}catch (NullPointerException e) {
		e.printStackTrace();
	}
	
}

public static Date dateToConvert(String date,String inputPattern) {
	Date dateReturn = null;
    try {
      

        DateFormat sdfSource = new SimpleDateFormat(inputPattern);
        Date d = sdfSource.parse(date);
       
        SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       String strDate = sdfDestination.format(d);
       // System.out.println(strDate);
         dateReturn =  sdfDestination.parse(strDate);
      //  System.out.println(dateReturn);

    } catch (ParseException pe) {
      //  System.out.println("Parse Exception : " + pe);
        
    }
    return dateReturn;
    }  

}

