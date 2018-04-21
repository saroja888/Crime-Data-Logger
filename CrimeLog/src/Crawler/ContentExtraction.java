package Crawler;

import java.io.File;
import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.text.PDFTextStripper;

public class ContentExtraction {
	
	DBConnection DBConn = new DBConnection();
	public void DrexelUniversityExtractionMethod()
	{
		String UniversityName = "DrexelUniversity";
		File input = new File("D:\\CrimeReportPDFS\\DrexelUniversityCrimeReport.pdf"); 
		PDDocument pd;
		try{
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        
	      	String pageText = reader.getText(pd);
	        	        
	        String[] lines = pageText.split("\n");
	        ArrayList<String> modifiedList = new ArrayList<String>();
	        for(String line : lines)
	        {
	        	  if((line.contains("Date Reported:")|| line.contains("Location :") || 
	        		  line.contains("Date and Time Occurred From - Occurred To:") ||
	        		  line.contains("Incident(s):") || line.contains("Disposition:")))
	        	     {
	        	    	  modifiedList.add(line);
	        	    	 
	        	      }
	        }
	        
	     Iterator<String> iterator = modifiedList.iterator();
	     ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	    
	     while(iterator.hasNext())
	     {
	    	String ReportNum="";
	    	String DateReported="";
	    	String TimeReported="";
	    	String Location="";
	    	String DateOccurred = "";
	    	String TimeOccurred = "";
	    	String IncidentType = "";
	    	String Disposition = "";
	    	
	   	    String ReportNumelement = iterator.next().toString();
	  
	      if(ReportNumelement.contains("Date Reported:")&& ReportNumelement.contains("Report #:") )
		   {
	        String[] words= ReportNumelement.split("Report #:");
			String DateandTime= words[0].toString().replace("Date Reported:", "").trim();
		    ReportNum = words[1].toString().trim();
		    String[] DateElement = DateandTime.split("-");
		   if(DateElement.length == 2)
		   {
		   DateReported = DateElement[0].toString();
		   String[] timelements = DateElement[1].toString().split("at");
		   TimeReported = timelements[timelements.length - 1].toString();
		   }
		  
		   }
	      
	     String locationelement = iterator.next().toString();
	     if(locationelement.contains("Location :"))
	      {
	    	 String[] words =  locationelement.split("Location :");
	    	 Location = words[1].toString().trim();
	      }
	     
	      String TimeRangelement = iterator.next().toString();
	      if(TimeRangelement.contains("Date and Time Occurred From - Occurred To:"))
	      {
	    	 String[] words =  TimeRangelement.split("Date and Time Occurred From - Occurred To:");
	     	 String TimeRange = words[1].toString().trim();
	     	   String[] DateOccurredElement = TimeRange.split("-");
			   if(DateOccurredElement.length >= 2)
			   {
				   DateOccurred = DateOccurredElement[0].toString();
			       String[] timelements = DateOccurredElement[1].toString().split("at");
			       TimeOccurred = timelements[1].toString();
			   }
	     	
	      }
	      String Incidentelement = iterator.next().toString();
	      if(Incidentelement.contains("Incident(s):"))
	      {
	    	   IncidentType =  Incidentelement.substring(12).trim();
	      }
	      
	      String Dispositionelement = iterator.next().toString();
	      if(Dispositionelement.contains("Disposition:"))
	      {
	    	 String[] words =  Dispositionelement.split("Disposition:");
	      	 Disposition = words[1].toString().trim();
	      }
	      
	     String dr = DateReported.trim()+"-"+TimeReported.trim();
	     String oc = DateOccurred.trim()+"-"+TimeOccurred.trim();
	     
	      UniversityData recordData = new UniversityData();
	      recordData.IncidentNumber = ReportNum;
	      recordData.ReportedDateTime=   dateToConvert( dr, "MM/dd/yy-HH:mm") ;
	      recordData.OffenseDateTime=   dateToConvert(oc , "MM/dd/yy-HH:mm") ;
	    		  
	      recordData.Location= Location;
	      recordData.IncidentType= IncidentType;
	      recordData.Disposition= Disposition;
	      finalRecordsList.add(recordData);
	     
	     } 
	     pd.close();
	     DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
		}catch (IOException e) {
           e.printStackTrace();
		}
	}
    public void CentralMichiganUniversityExtractionMethod(){
    	String UniversityName = "CentralMichiganUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\CentralMichiganUniversityCrimeReport.pdf"); 
    	PDDocument pd;
    	ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
            String pageText = reader.getText(pd);
            
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	 if(!( line.contains("Rpt Thru") || line.contains("CENTRAL MICHIGAN UNIVERSITY") || line.contains("DAILY CRIME LOG") ))
               	     {
            	    	  modifiedList.add(line);
            	    
               	     }
            	      
            }
            
         Iterator<String> iterator = modifiedList.iterator();
  
         while(iterator.hasNext())
         {
        
           String record =iterator.next().toString();
           
           StringTokenizer st = new StringTokenizer(record,"\t");  
           if (st.countTokens() == 8) {  
               String DateOccurred = st.nextToken();
               String TimeOccurred = st.nextToken();
               String DateReported = st.nextToken();
               String Nature = st.nextToken();
               String ReportSource= st.nextToken();
               String ReportNumber = st.nextToken();
               String Location = st.nextToken();
               String Status = st.nextToken();
             // // System.out.println(DateOccurred+" "+TimeOccurred+" "+DateReported+" "+Nature+" "+ReportNumber+" "+ReportSource+" "+Location+" "+Status);

               UniversityData recordData = new UniversityData();
              	      recordData.IncidentNumber = ReportNumber;
              	      recordData.ReportedDateTime=  dateToConvert( DateReported, "MM/dd/yy") ;
              	      recordData.OffenseDateTime=  dateToConvert(DateOccurred+" - "+TimeOccurred, "MM/dd/yy - Hm") ;
              	      recordData.Location= Location;
              	      recordData.IncidentType= Nature;
              	      recordData.Disposition= Status;
              	      finalRecordsList.add(recordData);
              	     
           } 
          
          }
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {

    		e.printStackTrace();

    }
    }
    public void UniversityofTexasatAustinExtractionMethod()
    {
    	String UniversityName = "UniversityofTexasatAustin";
    	File input = new File("D:\\CrimeReportPDFS\\UniversityofTexasatAustinCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
            String pageText = reader.getText(pd);
            
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	 if(!(line.contains("Nature of Call")|| line.contains("Date and Time") || line.contains("Case #") || 
               		  line.contains("Disposition:") || line.contains("Disposition Date")|| line.contains("Daily Crime Log for")))
               	     {
            	    	  modifiedList.add(line);
            	    
               	     }
            	      
            }
            
         Iterator<String> iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>(); 
         while(iterator.hasNext())
         {
        
           String record =iterator.next().toString();
           
           StringTokenizer st = new StringTokenizer(record,"\t");  
           if (st.countTokens() == 7) {  
               st.nextToken();
               String NatureofCall = st.nextToken();
               String DateandTimeOccurred = st.nextToken();
               String CaseNum = st.nextToken();
               String Location = st.nextToken();
               String Disposition = st.nextToken();
               String DispositionDate = st.nextToken();
               String DateOccurred = "" ;
               String DateReported = ""  ;
               String TimeReported = "" ;
               
               StringTokenizer st2 = new StringTokenizer(DateandTimeOccurred," ");  
              if(st2.countTokens() == 5)
              {
               st2.nextToken(); // Ignoring string "Between"
               DateOccurred = st2.nextToken();
               DateReported = st2.nextToken();
               TimeReported = st2.nextToken()+""+st2.nextToken();
              }
         //    // System.out.println(NatureofCall+" "+DateOccurred+" "+DateReported+" "+TimeReported+" "+CaseNum+" "+Location+" "+Disposition);

              
             UniversityData recordData = new UniversityData();
            	      recordData.IncidentNumber = CaseNum;
            	      recordData.ReportedDateTime=  dateToConvert( DateReported, "MM/dd/yy") ;;
            	      recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yy");
            	      recordData.Location= Location;
            	      recordData.IncidentType= NatureofCall;
            	      recordData.Disposition= Disposition;
            	      finalRecordsList.add(recordData);
            	     
           } 
    	   }
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);   
    	}catch (IOException e) {

    		e.printStackTrace();

    }
    }
    public void VanderbiltUniversityExtractionMethod(){
    	String UniversityName = "VanderbiltUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\VanderbiltUniversityCrimeReport.pdf"); 
    	PDDocument pd;

    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
            ArrayList<String> modifiedList = new ArrayList<String>();
            ArrayList<String> RecordsList = new ArrayList<String>();
         for(int j=0;j<=pd.getNumberOfPages();j++)
         {
         reader.setStartPage(j);
         reader.setEndPage(j);
          String pageText = reader.getText(pd);
          String[] lines = pageText.split("\n");
           
            for(String line : lines)
            {
            	 if(!(line.contains("Incident #")|| line.contains("Victim/Target") || line.contains("Date/Time") ||  line.contains("Occurred On or Between") || line.contains("Arrest Type /") ||
               		  line.contains("Disposition:") || line.contains("Past 90 Days") ||  line.contains("VUPD - DAILY CRIME LOG") ))
               	     {
            	    	  modifiedList.add(line.trim());
            	    	
               	     }
            	      
            }
         }
         
         Iterator<String> iterator = modifiedList.iterator();

         while(iterator.hasNext())
         {
          StringBuilder builder = new StringBuilder();
          String line=iterator.next().toString();
          while(!line.startsWith("Arrestee:")){
        	  builder.append(line+";");
        	  if(iterator.hasNext())
        	  {
        	  line = iterator.next().toString();
              }
        	  }
          RecordsList.add(builder.toString());
          
           } 

   	  ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
   	 
   	  for(String record : RecordsList ) 
    	 {
    		 
    		 String[] record_lines = record.split(";");
    		 String firstline= record_lines[0].toString();
    		 String secondline= record_lines[1].toString();
    		 String lastline = "";
    		 
    		 for(int i=0;i<=record_lines.length-1;i++)
    		 {
    			 if(record_lines[i].toString().startsWith("Active") || record_lines[i].toString().startsWith("Closed") 
    					 || record_lines[i].toString().startsWith("Unfounded") || record_lines[i].toString().startsWith("Inactive"))
    			 {
    				 lastline = record_lines[i].toString();
    				
    				 break;
    			 }
    			
    		 }
    		
    		
    		 StringTokenizer st1= new StringTokenizer(firstline,"\t");
    		 StringTokenizer st2= new StringTokenizer(secondline,"\t");
    		 StringTokenizer st3= new StringTokenizer(lastline,"\t");
    		 String DateReported="";
    		 String DateOccurred = "";
    		 String TimeOccurred = "";
    		 String IncidentNumber ="";
    		 String IncidentType = "";
    		 String TimeReported = "";
    		 String Disposition = "";
    		 String Location ="";
    		 
    		if(st1.countTokens() >= 5)
    		{
    			
    			 DateReported=st1.nextToken().toString();
    			 DateOccurred = st1.nextToken().toString();
    			 TimeOccurred = st1.nextToken().toString();
    			 String nextElement = st1.nextToken().toString();
    		     Boolean CaseNumAvailable = false;
                 for(int i=0;i<= nextElement.length()-1;i++)
                 {
                 	if(nextElement.charAt(i) == '-' || Character.isDigit(nextElement.charAt(i)))
                 	{
                 		CaseNumAvailable = true;
                 	}
                 	else
                 	{
                 		CaseNumAvailable = false;
                 		break;
                 	}
                 }
                if(CaseNumAvailable)
                {
                	IncidentNumber = nextElement;
             	   IncidentType = st1.nextToken().toString();
             	  }
                else
                {
             	   IncidentType = nextElement;
             	}
    			
    		
    		 
    		if(st2.countTokens() >= 1)
    		{
    			TimeReported=st2.nextToken().toString();
    		}
    		if(st3.countTokens() >= 2)
    		{
    			Disposition= st3.nextToken().toString();
    			Location= st3.nextToken().toString();
    		}
    		else
    		{
    		
           	String[] elements=	lastline.split("  ");
         	if( elements.length == 2)
    	    {
         		Disposition= elements[0].toString();
    			Location= elements[1].toString();
    	
            }
        	if( elements.length == 1)
    	    {
         		Disposition= elements[0].toString();
            }
    		}
    	if(CaseNumAvailable)
    	{
    		String dr = DateReported+" - "+TimeReported;
    		String oc = DateOccurred+" - "+TimeOccurred;
       UniversityData recordData = new UniversityData();
        		      recordData.IncidentNumber = IncidentNumber;
        		      recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy - H:mm") ;
        		      recordData.OffenseDateTime=  dateToConvert( oc, "E, MMMMM dd, yyyy - H:mm") ;
        		      recordData.Location= Location;
        		      recordData.IncidentType= IncidentType;
        		      recordData.Disposition= Disposition;
        		    finalRecordsList.add(recordData);
        		   
    		}
    		}
    		
    	 }
   	 pd.close();
   	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);    
         
         }catch (IOException e) {

    	e.printStackTrace();
    	

    }
    }
    public void UniversityofPittsburghExtractionMethod(){
    	String UniversityName = "UniversityofPittsburgh";
		File input = new File("D:\\CrimeReportPDFS\\UniversityofPittsburghCrimeReport.pdf"); 
		PDDocument pd;
		try{
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	      
	        String pageText = reader.getText(pd);
	        String[] lines = pageText.split("\n");
	        ArrayList<String> modifiedList = new ArrayList<String>();
	        ArrayList<String> finalList = new ArrayList<String>();
	        for(String line : lines)
	        {
	        	 if(!(line.contains("University of Pittsburgh") || line.contains("POLICE DEPARTMENT") ||  
	        	line.contains("Media Log") || line.contains("Print Date and Time") ||  line.contains("Page No.")  ))
	           	{
	        	    	  modifiedList.add(line.trim());
	        	}
	        	      
	        }
	         String IncidentType ="";
	    	 String IncidentDate="";
	    	 String IncidentTime = "";
	    	 String ReportNumber="";
	    	 String location="";
	    	 String summary="";
	     Iterator iterator = modifiedList.iterator();

     ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	     
	     while(iterator.hasNext())
	     {
	    	
	    	 boolean isfirstline = false;
	    	 String line = iterator.next().toString();
	    	 StringTokenizer tokenizer = new StringTokenizer(line,"\t");
	    	 if(tokenizer.countTokens() == 3)
	    	 {
	    		 isfirstline = true; 
	    		 IncidentType = tokenizer.nextToken();
	    		 String DateElement = tokenizer.nextToken().toString();
	    		 String[] elements = DateElement.split("-");
	    		 if(elements.length >=2)
	    		 {
	    			 IncidentDate = elements[0].toString();
	    			 String timestring = elements[1].toString();
	    			String[] timeElements =  timestring.split("at");
	    			IncidentTime = timeElements[timeElements.length - 1].toString();
	    			 
	    		 }
	    		 ReportNumber = tokenizer.nextToken()+" "+iterator.next().toString();
	    		 
	    	 }
	    	 if(isfirstline)
	    	 {
	    		 String LocationLine = iterator.next().toString();
	    		 if(LocationLine.startsWith("Location :"))
	    		 {
	    			 location= LocationLine.trim().replaceFirst("Location :", "").trim();
	    			 
	    		 }
	    		 String SummaryLine = iterator.next().toString();
	    		 if(SummaryLine.startsWith("Summary :"))
	    		 {
	    			 summary= SummaryLine.trim().replaceFirst("Summary :", "").trim();
	    			 
	    		 }
	    		 
	    		 finalList.add(IncidentType+";"+IncidentDate+";"+IncidentTime+";"+ReportNumber+";"+location+";"+summary);
	    	 }
	    	 else
	    	 {
	    		 if (finalList != null && !finalList.isEmpty()) {
	    			String lastelement =  finalList.get(finalList.size()-1);
	    			finalList.set(finalList.size()-1, lastelement+line);
	    			}
	    	 }
	    	
	     }
	     
	     for(String item : finalList)
	 	{
	 		StringTokenizer tokenizer = new StringTokenizer(item,";");
	 		if(tokenizer.countTokens() == 6)
	 		{
	 		IncidentType = tokenizer.nextToken().toString();
	 		IncidentDate = tokenizer.nextToken().toString();
	 		IncidentTime = tokenizer.nextToken().toString();
	 		ReportNumber = tokenizer.nextToken().toString();
	 		location =  tokenizer.nextToken().toString();
	 		summary = tokenizer.nextToken().toString();
	 		String date = IncidentDate+" - "+IncidentTime;
	 		date = date.replaceAll(" ", "");
	 		 UniversityData recordData = new UniversityData();
	 			      recordData.IncidentNumber = ReportNumber;
	 			      recordData.ReportedDateTime= null;
	 			      recordData.OffenseDateTime=  dateToConvert( date, "MMMdd,yyyy-H:mm") ;;
	 			      recordData.Location= location;
	 			      recordData.IncidentType= IncidentType;
	 			      recordData.Disposition= "";
	 			      finalRecordsList.add(recordData);
	 			     
	 		}
	 		}    
	     pd.close();
	     DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	     }catch (IOException e) {

		e.printStackTrace();

	}
    }
    public void UniversityofIowaExtractionMethod(){
    	String UniversityName = "UniversityofIowa";
	 File input = new File("D:\\CrimeReportPDFS\\UniversityofIowaCrimeReport.pdf"); 
		PDDocument pd;
		try{
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        String pageText = reader.getText(pd);
	        String content="";
	       if(!( pageText.indexOf("Explanation of Case Dispositions") == -1))
	       {
	    	  content =   pageText.substring(0, pageText.indexOf("Explanation of Case Dispositions"));
	       }
	        
	        String[] lines = content.split("\n");
	        ArrayList<String> modifiedList = new ArrayList<String>();
	        
	        for(String line : lines)
	        {
	        	if(!(line.contains("THE UNIVERSITY OF IOWA POLICE DAILY CRIME LOG") || line.contains("Date Printed:") ||  
	        	line.contains("Case #") || line.contains("Report Printed on")  ))
	           	{
	        	    	  modifiedList.add(line.trim());
	        	}
	        	      
	        }
	        
	        Iterator iterator = modifiedList.iterator();

    ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	        while(iterator.hasNext())
	        {
	        	String IncidentNumber= "";// May be empty for some records in this pdf
	        	String DateOccurred = "";
	        	String TimeOccurred = "";
	        	String DateReported="";
	        	String Location = "";
	        	String IncidentType = "";
	        	String Disposition="";
	        	String TimeReported = "";
	        	String record = iterator.next().toString();
	        	String[] tokenizer= record.split("\t");
	        	int length = tokenizer.length;
	        	if(length >= 4)
	        	{
	        	if(tokenizer[0].toString().contains("/"))
	        	{
	        		DateOccurred = tokenizer[0].toString();
	        		TimeOccurred = tokenizer[1].toString();
	           	}
	        	else
	        	{
	        		IncidentNumber = tokenizer[0].toString();
	        		if(tokenizer[1].toString().contains("/"))
	        		{
	        			DateOccurred = tokenizer[1].toString();
	        			TimeOccurred = tokenizer[2].toString();
	        		}
	        	}
	        	String DateTimeReported = tokenizer[length-4].toString();
	        	String[] elements = DateTimeReported.split(" ");
	        		DateReported = elements[0].toString();
	        		TimeReported = DateTimeReported.replaceFirst(DateReported, "");
	        	
	        	Location =  tokenizer[length-3].toString();
	        	IncidentType =  tokenizer[length-2].toString();
	        	Disposition = tokenizer[length-1].toString();
	        	}
	        //  // System.out.println(TimeOccurred+"   "+IncidentNumber+"   "+DateReported+"     "+TimeReported+"   "+Location+"   "+Disposition+"   "+IncidentType+"    "+DateOccurred);
String dr = DateReported+" - "+TimeReported;
String oc = DateOccurred+" - "+TimeOccurred;
dr = dr.replaceAll(" ","");
oc = oc.replaceAll(" ", "");
	        	
	        	 UniversityData recordData = new UniversityData();
	        		      recordData.IncidentNumber = IncidentNumber;
	        		      recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yy-h:mma") ;
	        		      recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yy-h:mma") ;
	        		      recordData.Location= Location;
	        		      recordData.IncidentType= IncidentType;
	        		      recordData.Disposition= Disposition;
	        		      finalRecordsList.add(recordData);
	        		     
	        }
	        pd.close();
	        DBConn.InsertDatatoDB(UniversityName,finalRecordsList);	
	        }catch (IOException e) {
		e.printStackTrace();
              	}
 }
    public void AmericanUniversityExtractionMethod(){
    	String UniversityName = "AmericanUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\AmericanUniversityCrimeReport.pdf"); 
    	PDDocument pd;

        ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
    	try {
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
            
          for(int i=0;i<=pd.getNumberOfPages();i++)
          {
        	reader.setStartPage(i);
        	reader.setEndPage(i);
        	String pageText = reader.getText(pd);
        	
        	ArrayList<String> recordList= new ArrayList<String>();
        	String[] lines = pageText.split("\n");
        	
        	for(String line : lines)
        	{
        		if(!(line.contains("Report #") || line.split("\t").length < 7))
        		{
        			recordList.add(line);
           		}
        			
        	}
        	for(String record : recordList )
        	{
        		String ReportNum = "";
            	String IncidentType = "";
            	String Location = "";
            	String DateReported = "";
            	String DateOccurred = "";
            	String Disposition = "";
        		StringTokenizer tokens= new StringTokenizer(record,"\t");
        		if(tokens.countTokens() == 7)
        		{
        	    ReportNum = tokens.nextToken().toString();
                IncidentType = tokens.nextToken().toString();
            	Location = tokens.nextToken().toString();
            	DateReported = tokens.nextToken().toString();
            	DateOccurred = tokens.nextToken().toString();
            	tokens.nextToken().toString();
            	Disposition = tokens.nextToken().toString();

            	 UniversityData recordData = new UniversityData();
            		      recordData.IncidentNumber = ReportNum;
            		      recordData.ReportedDateTime=  dateToConvert( DateReported, "MM/d/yyyy H:mm") ;;
            		      recordData.OffenseDateTime=  dateToConvert( DateOccurred, "MM/d/yyyy H:mm") ;
            		      recordData.Location= Location;
            		      recordData.IncidentType= IncidentType;
            		      recordData.Disposition= Disposition;
            		      finalRecordsList.add(recordData);
            		            	}
        		//// System.out.println(ReportNum+"  "+IncidentType+"  "+Location+"  "+DateReported+"  "+
        			//	DateOccurred+"  "+Disposition);
        	}
        	
    }
          pd.close();
          DBConn.InsertDatatoDB(UniversityName,finalRecordsList);

    	} catch (InvalidPasswordException e) {
    				e.printStackTrace();
    	} catch (IOException e) {
    		
    		e.printStackTrace();
    	}
    }
    public void UniversityofCentralFloridaExtractionMethod(){
    	String UniversityName = "UniversityofCentralFlorida";
	 File input = new File("D:\\CrimeReportPDFS\\UniversityofCentralFloridaCrimeReport.pdf"); 
		PDDocument pd;

ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		
		try {
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        
	      for(int i=0;i<=pd.getNumberOfPages();i++)
	      {
	    	reader.setStartPage(i);
	    	reader.setEndPage(i);
	    	String pageText = reader.getText(pd);
	    	
	    	ArrayList<String> recordList= new ArrayList<String>();
	    	String[] lines = pageText.split("\n");
	    	
	    	for(String line : lines)
	    	{
	    		if(!(line.contains("UCF Police Department")|| line.contains("Daily Crime Log")|| line.contains("Page:")||
	    		 line.contains("Print Date:")|| line.contains("Filter Criteria: CleryCampus = 'MAIN CAMPUS'") || 
	    		 line.contains("Reported Date")|| line.contains("Case #") || line.split("\t").length < 7))
	    		{
	    			recordList.add(line);    		
	    		}
	    			
	    	}
	    	
	     
	    	for(String record : recordList )
	    	{
	    		String ReportNum = "";
		    	String IncidentType = "";
		    	String Location = "";
		    	String DateReported = "";
		    	String DateOccurred = "";
		    	String Disposition = "";
		    	String TimeReported = "";
		    	String TimeOccurred = "";
	    		StringTokenizer tokens= new StringTokenizer(record,"\t");
	    		if(tokens.countTokens() == 7)
	    		{
	    	    ReportNum = tokens.nextToken().toString();
	            IncidentType = tokens.nextToken().toString();
	            Disposition = tokens.nextToken().toString();
	        	String DateTimeReported = tokens.nextToken().toString();
	        	String DateTimeOccurred = tokens.nextToken().toString();
	        	String[] items = DateTimeReported.split(" ");
	        	if(items.length == 2)
	        	{
	        		DateReported = items[0].toString();
	        		TimeReported = items[1].toString();
	        	}
	        	String[] elements = DateTimeOccurred.split(" ");
	        	if(elements.length == 2)
	        	{
	        		DateOccurred = elements[0].toString();
	        		TimeOccurred = elements[1].toString();
	        	}
	        	tokens.nextToken().toString();
	        	Location = tokens.nextToken().toString();
	    	}
	    		//// System.out.println(ReportNum+"  "+IncidentType+"  "+Location+"  "+DateOccurred+"   "+TimeOccurred+"     "+DateReported+"   "+TimeReported+"  "+Disposition);
String dr = DateReported+" - "+TimeReported;
String oc = DateOccurred+" - "+TimeOccurred;
	    		 UniversityData recordData = new UniversityData();
	    			      recordData.IncidentNumber = ReportNum;
	    			      
	    			      recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yy - H:mm") ;
	    			      recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yy - H:mm") ;
	    			      recordData.Location= Location;
	    			      recordData.IncidentType= IncidentType;
	    			      recordData.Disposition= Disposition;
	    			      finalRecordsList.add(recordData);
	    			     

	    	}
	    	
	}
	      pd.close();
	      DBConn.InsertDatatoDB(UniversityName,finalRecordsList);

		} catch (InvalidPasswordException e) {
					e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
 }
    public void UniversityofWisconsinMadisonExtractionMethod(){
    	String UniversityName = "UniversityofWisconsinMadison";
	     File input = new File("D:\\CrimeReportPDFS\\UniversityofWisconsinMadisonCrimeReport.pdf"); 
		PDDocument pd;
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		   
		try {
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        
	      for(int i=0;i<=pd.getNumberOfPages();i++)
	      {
	    	reader.setStartPage(i);
	    	reader.setEndPage(i);
	    	String pageText = reader.getText(pd);
	    	
	    	ArrayList<String> recordList= new ArrayList<String>();
	    	String[] lines = pageText.split("\n");
	    	
	    	for(String line : lines)
	    	{
	    		if(!(line.contains("UNIVERSITY OF WISCONSIN - MADISON POLICE DEPARTMENT INCIDENT LOG")
	    				|| line.contains("Event #")|| line.contains("Page")||line.split("\t").length < 5))
	    		{
	    			recordList.add(line);
	    		}
	    			
	    	}
	    	  
	    	for(String record : recordList )
	    	{
	    		String ReportNum = "";
		    	String IncidentType = "";
		    	String Location = "";
		    	String DateOccurred = "";
		    	String Disposition = "";
		    	String TimeOccurred = ""; 
	    		String[] tokens= record.split("\t");
	    		if(tokens.length >=5 )
	    		{
	    	    ReportNum = tokens[0].toString();
	    	    String   DateTimeOccurred = tokens[1].toString();
	            String[] elements = DateTimeOccurred.split(" ");
	            if(elements.length >= 2)
	            {
	            DateOccurred = elements[0].toString();
	           	TimeOccurred = DateTimeOccurred.replaceFirst(DateOccurred, "");
	            }
	          
	    	    IncidentType = tokens[2].toString();
	    	    Disposition = tokens[tokens.length -1].toString();
	    	    Location = tokens[tokens.length -2].toString();
	        	
	    	}
	    	//// System.out.println(ReportNum+"  "+IncidentType+"  "+DateOccurred+"  "+Location+"  "+Disposition+"  "+TimeOccurred);
	    	String oc = DateOccurred+" - "+TimeOccurred;
	    	oc = oc.replaceAll(" ","");
	    		 UniversityData recordData = new UniversityData();
	    			      recordData.IncidentNumber = ReportNum;
	    			      recordData.ReportedDateTime= null;
	    			      recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yy-h:mma") ;
	    			      recordData.Location= Location;
	    			      recordData.IncidentType= IncidentType;
	    			      recordData.Disposition= Disposition;
	    			      finalRecordsList.add(recordData);
	    			     
	      }
	      }
	      pd.close();
	    	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	   
		} catch (InvalidPasswordException e) {
					e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
 }
    public void UniversityofUtahExtractionMethod(){
    	String UniversityName = "UniversityofUtah";
    	File input = new File("D:\\CrimeReportPDFS\\UniversityofUtahCrimeReport.pdf"); 
    	PDDocument pd;
    	
    	try {
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
         
        	String pageText = reader.getText(pd);
        	
        	ArrayList<String> recordList= new ArrayList<String>();
        	String[] lines = pageText.split("\n");
        	
        	for(String line : lines)
        	{
        		if(line.contains("Case Number:") || line.contains("Location:")|| line.contains("Offense:")
        		|| line.contains("Report Date:") || line.contains("Happened Between:")|| line.contains("Narrative:"))
        		{
        			recordList.add(line);
           		}
        		
        	}
        	Iterator iterator = recordList.iterator();
            ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
        	
        	while(iterator.hasNext())
        	{
        		String ReportNum = "";
            	String IncidentType = "";
            	String Location = "";
            	String DateReported ="";
            	String DateOccurred = "";
            	String Disposition = "";
            	String TimeOccurred ="";
            	String TimeReported = "";
        		String firstline = iterator.next().toString();
        		
        		if(firstline.contains("Case Number:"))
        		{
        			String modifiedline = firstline.replaceFirst("Case Number:","").trim();
        			String[] words = modifiedline.split("Nature:");
        			if(words.length == 2)
        			{
        				ReportNum = words[0].toString();
        				String DispositionText = words[1].toString();
        				String[] worditems = DispositionText.split("Disposition:");
        				if(worditems.length == 2)
        				{ IncidentType = worditems[0].toString();
        				 Disposition = worditems[1].toString();}
        			}
           		}
        		
        		String SecondLine = iterator.next().toString();
        		if(SecondLine.startsWith("Location:"))
        		{
        			Location = SecondLine.replaceFirst("Location:","").trim();
        		}
        		
        		String ThirdLine = iterator.next().toString();
        		if(ThirdLine.startsWith("Offense:"))
        		{
        			IncidentType = ThirdLine.replaceFirst("Offense:","").trim();
        		}
        		
        		String forthLine = iterator.next().toString();
        		if(forthLine.startsWith("Report Date:"))
        		{
        			String DateTimeReported  = forthLine.replaceFirst("Report Date:","").trim();
        			String[] elements = DateTimeReported.split(" ");
     	            if(elements.length >= 2)
     	            {
     	            DateReported = elements[0].toString();
     	           	TimeReported = DateTimeReported.replaceFirst(DateReported, "");
     	            }
        		}
        		
        		String fifthLine = iterator.next().toString();
        		if(fifthLine.startsWith("Happened Between:"))
        		{
        			String DateTimeOccurred = fifthLine.replaceFirst("Happened Between:","").trim();
        			String[] elements = DateTimeOccurred.split(" ");
        			 if(elements.length >= 1)
       	            DateOccurred = elements[0].toString();
     	            
     	            String[] timeElements = DateTimeOccurred.replaceFirst(DateOccurred,"").trim().split("&");
     	           if(timeElements.length >= 1)
          	            TimeOccurred = timeElements[0].toString();   
     	          }
        		
        		String sixthLine_summary = iterator.next().toString();
        	//// System.out.println(ReportNum+"   "+IncidentType+"   "+Disposition+"  "+DateReported+"  "+DateOccurred+"  "+TimeReported+"  "+TimeOccurred);
        	 UniversityData recordData = new UniversityData();
       	      recordData.IncidentNumber = ReportNum;
       	      recordData.ReportedDateTime=  dateToConvert( (DateReported+" - "+TimeReported).replaceAll(" ",""), "MM/dd/yyyy-h:mm:ssa") ;

       	      recordData.OffenseDateTime=  dateToConvert( (DateOccurred+" - "+TimeOccurred).replaceAll(" ",""), "MM/dd/yyyy-h:mm:ssa") ;

       	      recordData.Location= Location;
       	      recordData.IncidentType= IncidentType;
       	      recordData.Disposition= Disposition;
       	      finalRecordsList.add(recordData);
        	}
        	 pd.close();
        	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	} catch (InvalidPasswordException e) {
    				e.printStackTrace();
    	} catch (IOException e) {
    		
    		e.printStackTrace();
    	}
 }

    public  void UniversityofSouthernCaliforniaExtractionMethod(){
    	String UniversityName = "UniversityofSouthernCalifornia";
    	File input = new File("D:\\CrimeReportPDFS\\UniversityofSouthernCaliforniaCrimeReport.pdf"); 
    	PDDocument pd;
    	
    	try {
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
        
        	String pageText = reader.getText(pd);
        	
        	ArrayList<String> recordList= new ArrayList<String>();
        	String[] lines = pageText.split("\n");
        	
        	for(String line : lines)
        	{
        		if(line.contains("Reported:") || line.contains("Location:")|| line.contains("Report #:")
        		|| line.contains("Occurred:") || line.contains("Incident:") || line.contains("Summary:")  )
        		{
        			recordList.add(line);
        		
        			}
        		
        	}
        	Iterator iterator = recordList.iterator();

     ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
        	
        	while(iterator.hasNext())
        	{
        		String ReportNum = "";
            	String IncidentType = "";
            	String Location = "";
            	String DateReported ="";
            	String DateOccurred = "";
            	String Disposition = "";
            	String TimeOccurred ="";
            	String TimeReported = "";
        		String firstline = iterator.next().toString();
        		
        		if(firstline.contains("Reported:"))
        		{
        			String modifiedline = firstline.replaceFirst("Reported:","").trim();
        			String[] words = modifiedline.split("Location:");
        			
        			if(words.length == 2)
        			{
        				String ReportedDateTime = words[0].toString();
        				 String[] items = ReportedDateTime.split("-");
	        				if(items.length >= 2)
	        				{
	        				DateReported = items[0].toString();
	        				TimeReported = items[1].toString();
	        				}
        				String LocationText = words[1].toString();
        				String[] worditems = LocationText.split("Report #:");
        				if(worditems.length == 2)
        				{ Location = worditems[0].toString();
        				ReportNum = worditems[1].toString();}
        			}
        			
           		}
        		
        		String SecondLine = iterator.next().toString();
        		if(SecondLine.startsWith("Occurred:"))
        		{
        			String DateTimeOccurred = SecondLine.replaceFirst("Occurred:","").trim();
        			String[] elements = DateTimeOccurred.split("to");
        			if(elements.length >= 1)
        			{
        				String DateTime = elements[0].toString();
        				String[] items = DateTime.split("-");
        				if(items.length >= 2)
        				{
        				DateOccurred = items[0].toString();
        				TimeOccurred = items[1].toString();
        				}
        			}
        		}
        		if(SecondLine.contains("Disposition:"))
        		{
        			String[] elements = SecondLine.split("Disposition:");
        			Disposition = elements[elements.length-1];
        			
        		}
        		String ThirdLine = iterator.next().toString();
        		if(ThirdLine.startsWith("Incident:"))
        		{
        			String[] incidentTypeElements = ThirdLine.replaceFirst("Incident:","").trim().split("\t");
        			if(incidentTypeElements.length >=1 )
        			IncidentType = incidentTypeElements[0].toString();
        		}
        		
        		String forthLine = iterator.next().toString();
        		String summary ="";
        		if(forthLine.startsWith("Summary:"))
        		{
        			 summary = forthLine.replaceFirst("Summary:","").trim();
        		}
        		

        		 UniversityData recordData = new UniversityData();
        			
        			      recordData.IncidentNumber = ReportNum;
        			      String date = DateReported+" - "+TimeReported;
        			      recordData.ReportedDateTime=  dateToConvert( date.replaceAll(" ",""), "MM/dd/yy-hh:mma") ;
        			      date = DateOccurred+" - "+TimeOccurred;
        			      recordData.OffenseDateTime=  dateToConvert( date.replaceAll(" ",""), "MM/dd/yy-hh:mma") ;
        			      recordData.Location= Location;
        			      recordData.IncidentType= IncidentType;
        			      recordData.Disposition= Disposition;
        			      finalRecordsList.add(recordData);
        	}
        	 pd.close();
        	 
  	      DBConn.InsertDatatoDB(UniversityName,finalRecordsList);

    	} catch (IOException e) {
    		
    		e.printStackTrace();
    	}
 }
    public  void PrincetonUniversityExtractionMethod()
	 {
    	String UniversityName = "PrincetonUniversity";
	 	File input = new File("D:\\CrimeReportPDFS\\PrincetonUniversityCrimeReport.pdf"); 
	 	PDDocument pd;
	 	try{
	 		pd= PDDocument.load(input);
	 		PDFTextStripper reader = new PDFTextStripper();
	         reader.setSortByPosition(true);
	         reader.setWordSeparator("\t");
	         String pageText = reader.getText(pd);
	         String[] lines = pageText.split("\n");
	         ArrayList<String> modifiedList = new ArrayList<String>();
	         for(String line : lines)
	         {
	         	 if(!(line.contains("PRINCETON UNIVERSITY")|| line.contains("DEPARTMENT OF PUBLIC SAFETY") || line.contains("200 Elm Drive Princeton New Jersey 08544") || 
	            		  line.contains("CLERY CRIME AND FIRE LOG") || line.contains("CRIME LOG")|| line.contains("Classification")
	            		|| line.contains("FIRE LOG") || line.contains("Date/Time Reported")|| line.contains("Case Number")  ))
	            	     {
	         	    	  modifiedList.add(line);
	         	       	  }
	         	      
	         }
	         
	      Iterator iterator = modifiedList.iterator();

         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	       while(iterator.hasNext())
	        {
	    	   String IncidentType = "";
	    	   String ReportNum = "";
	    	   String DateReported = "";
	    	   String DateOccurred = "";
	    	   String Location = "";
	    	   String Disposition = "";
		          String record = iterator.next().toString();
		          StringTokenizer st = new StringTokenizer(record,"\t"); 
		
		  if(st.countTokens() >= 7)
		  {
			   IncidentType = st.nextToken().toString();
			   ReportNum = st.nextToken().toString();
			   DateReported = st.nextToken().toString();
			   DateOccurred = st.nextToken().toString();
			  st.nextToken();
			   Location = st.nextToken().toString();
			   Disposition = st.nextToken().toString();

			  UniversityData recordData = new UniversityData();
			 	      recordData.IncidentNumber = ReportNum;
			 	      recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yyyy H:mm") ;
			 	      recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy H:mm") ;
			 	      recordData.Location= Location;
			 	      recordData.IncidentType= IncidentType;
			 	      recordData.Disposition= Disposition;
			 	      finalRecordsList.add(recordData);
			  //// System.out.println(IncidentType+"  "+ReportNum+"  "+DateReported+"  "+TimeReported+"   "+DateOccurred+"  "+TimeOccurred+"  "+Disposition);
		  }
		  
	  }
	       pd.close();
      DBConn.InsertDatatoDB(UniversityName,finalRecordsList);    
	 	}catch (IOException e) {

	 	e.printStackTrace();

	 }
	 }
    public  void UniversityofAlabamaExtractionMethod()
    {
    	String UniversityName = "UniversityofAlabama";
   	 File input = new File("D:\\CrimeReportPDFS\\UniversityofAlabamaCrimeReport.pdf"); 
	 	PDDocument pd;
	 	try{
	 		pd= PDDocument.load(input);
	 		PDFTextStripper reader = new PDFTextStripper();
	         reader.setSortByPosition(true);
	         reader.setWordSeparator(" ");
	         String pageText = reader.getText(pd);
	         String[] lines = pageText.split("\n");
	         ArrayList<String> modifiedList = new ArrayList<String>();
	         for(String line : lines)
	         {
	         	 if(!(line.contains("Clery Crime and Fire Log")|| line.contains("Case Number") || line.contains("Summary") 
	         			 || line.contains("No Crimes or Fires reported this date.") || line.contains("Reported") || line.isEmpty()))
	             {
	         		  modifiedList.add(line);
	         		  
	          	  }
	         	
	         	      
	         }
	         
	      Iterator iterator = modifiedList.iterator();

   ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	  while(iterator.hasNext())
	  {
		  String record = iterator.next().toString();
		  StringTokenizer st = new StringTokenizer(record," "); 

		  if(st.countTokens() >= 6)
		  {
			 
			  String DateReported = st.nextToken().toString();
			  String ReportNum = st.nextToken().toString();
			if(!(DateReported.matches("[A-Za-z]+") || ReportNum.matches("[A-Za-z]+")) )
			{
			  UniversityData recordData = new UniversityData();
			 	      recordData.IncidentNumber = ReportNum;
			 	      recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/d/yyyy") ; ;
			 	      recordData.OffenseDateTime= dateToConvert( DateReported,"MM/d/yyyy") ; ;
			 	      recordData.Location= "";
			 	      recordData.IncidentType= "";
			 	      recordData.Disposition= "";
			 	      finalRecordsList.add(recordData);
			}
			  
		  }
		  
	  }
	  pd.close();
      DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	         
	 	}catch (IOException e) {

	 	e.printStackTrace();

	 }
    }
    public  void TexasTechUniversityExtractionMethod()
    {
    	String UniversityName = "TexasTechUniversity";
    	 File input = new File("D:\\CrimeReportPDFS\\TexasTechUniversityCrimeReport.pdf"); 
  	 	PDDocument pd;
  	 	try{
  	 		pd= PDDocument.load(input);
  	 		PDFTextStripper reader = new PDFTextStripper();
  	         reader.setSortByPosition(true);
  	        
  	         String pageText = reader.getText(pd);
  	         String[] lines = pageText.split("\n");
  	         ArrayList<String> modifiedList = new ArrayList<String>();
  	         for(String line : lines)
  	         {
  	         	 if(!(line.contains("TEXAS TECH POLICE DEPARTMENT")|| line.contains("SUMMARY OF DAILY REPORTS") || line.contains("Date") || line.contains("UNIT")
  	         			 || line.contains("SHIFT") || line.contains("SIGNED")))
  	             {
  	         		if(line.trim().length() > 0)
  	         		modifiedList.add(line.trim());
  	         		
  	           	  }
  	         	      
  	         }
  	       modifiedList.remove(0);
  	      Iterator iterator = modifiedList.iterator();

ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
  	 
  	while(iterator.hasNext())
	  {
  		  String ReportNum = "";
  		  String record = iterator.next().toString();
  		  StringTokenizer st = new StringTokenizer(record," "); 
  		  String TimeOccurred = "";
  		  String Location = "";
  		  String summary = "";
  		  String DateReported = "";
  		  String TimeReported = "";
  		
  		  if(st.countTokens() >= 2)
  		  {
  			   TimeOccurred = st.nextToken();
  			   Location = st.nextToken();
  			    while(st.hasMoreTokens())
  			    {
  			    	summary = summary+" "+st.nextToken();
  			    }
  		  }
  		  
  		  String line = iterator.next().toString();
  		  while(!line.startsWith("Entry written"))
  		  {
  			 if(line.contains("report#")|| line.contains("report #"))
  	  		  {
  			summary = summary+line;
  			line = iterator.next().toString();
  			break;
  	  		  }
  			 else
  			 {
  				line = iterator.next().toString();
  			 }	
  		  }
  		  
  		  if(line.startsWith("Entry written"))
  		  {
  			String[] elements =  line.split("on");
  			
  			if(elements.length == 2)
  			{
  			String	DateTimeReported = elements[1].toString();
  			String[] items = DateTimeReported.split("at");
  			if(items.length >= 2)
  				{
  				 DateReported = items[0].toString();
  				 TimeReported = items[1].toString();
  				}
  			}
  		  }
  		  
  		  if(summary.contains("report#")|| summary.contains("report #"))
  		  {
  		 ReportNum = summary.substring(summary.indexOf("report"),summary.length()-1).replace("#","").replace("report","").replace(".","").trim();
  		
  		  }
  		  
  		// // System.out.println(ReportNum+"  "+TimeOccurred+"  "+DateReported+"  "+TimeReported+"  "+Location+"  ");
  	boolean	ReportNumIsDigit = false;
  	if(ReportNum.isEmpty())
  		{
  		ReportNumIsDigit = false;
  		}
  	   String regex = "[0-9]+"; 
  	     if(ReportNum.matches(regex))
  	    {
  		    ReportNumIsDigit = true;
  	    }
  	
		  if(ReportNumIsDigit )
		  {
  		 UniversityData recordData = new UniversityData();
  			      recordData.IncidentNumber = ReportNum;
  			      recordData.ReportedDateTime= null;
  			      recordData.OffenseDateTime= null;
  			      recordData.Location= Location;
  			      recordData.IncidentType= "";
  			      recordData.Disposition= "";
  			      finalRecordsList.add(recordData);
		  }
  	  }
  	 pd.close();

     DBConn.InsertDatatoDB(UniversityName,finalRecordsList);  
  	 	}catch (IOException e) {

  	 	e.printStackTrace();

  	 }
    }
    public  void UniversityofCaliforniaSantaBarbaraExtractionMethod()
    {
    	String UniversityName = "UniversityofCaliforniaSantaBarbara";
    	File input = new File("D:\\CrimeReportPDFS\\UniversityofCaliforniaSantaBarbaraCrimeReport.pdf"); 
    	PDDocument pd;
    	
    	try {
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
        
        	String pageText = reader.getText(pd);
        	
        	ArrayList<String> recordList= new ArrayList<String>();
        	String[] lines = pageText.split("\n");
        	
        	for(String line : lines)
        	{
        		if(line.contains("date reported:") || line.contains("location :")|| line.contains("date and time occurred from - occurred to:")
        		|| line.contains("incident :") || line.contains("disposition:") || line.contains("report #:"))
        		{
        			recordList.add(line);
           		}
        		
        	}
        	Iterator iterator = recordList.iterator();

    ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	     
        	while(iterator.hasNext())
        	{
        		String ReportNum = "";
            	String IncidentType = "";
            	String Location = "";
            	String DateReported ="";
            	String DateOccurred = "";
            	String Disposition = "";
            	String TimeOccurred ="";
            	String TimeReported = "";
            	String IncidentNum = "";
        		String firstline = iterator.next().toString().trim();
        		
        		if(firstline.contains("date reported:"))
        		{
        			String modifiedline = firstline.replaceFirst("date reported:","").trim();
        			String[] words = modifiedline.split("location :");
        			
        			if(words.length == 2)
        			{
        				String ReportedDateTime = words[0].toString();
        				 String[] items = ReportedDateTime.split("-");
	        				if(items.length >= 2)
	        				{
	        				DateReported = items[0].toString();
	        				String[] timelements = items[1].toString().split("at");
	        				if(timelements.length > 1)
	        				TimeReported = timelements[1].toString();
	        				}
        				String LocationText = words[1].toString();
        				String[] worditems = LocationText.split("event #:");
        				if(worditems.length == 2)
        				{ 
        				Location = worditems[0].toString();
        				IncidentNum = worditems[1].toString();
        				}
        			}
        			
           		}
        		
        		String SecondLine = iterator.next().toString().trim();
        		if(SecondLine.startsWith("date and time occurred from - occurred to:"))
        		{
        			String DateTimeOccurred = SecondLine.replaceFirst("date and time occurred from - occurred to:","").trim();
        			String[] elements = DateTimeOccurred.split("-");
        			if(elements.length >= 4)
        			{
        				String DateTime = elements[0].toString();
        				String[] items = DateTime.split("-");
        				if(items.length >= 2)
        				{
        				DateOccurred = items[0].toString();
        				String[] timeItems = items[1].toString().split("at");
        				if(timeItems.length > 1)
        				TimeOccurred = timeItems[1].toString();
        				}
        			}
        		}
        		
        		String ThirdLine = iterator.next().toString().trim();
        		if(ThirdLine.startsWith("incident :"))
        		{
        			String[] elements = ThirdLine.replaceFirst("incident :","").trim().split("report #:");
        			if(elements.length >= 2)
        			{
        			IncidentType = elements[0].toString();
        			ReportNum = elements[1].toString();
        			}
             		}
        		
        		String forthLine = iterator.next().toString().trim();
        		
        		if(forthLine.startsWith("disposition:"))
        		{
        			 Disposition = forthLine.replaceFirst("disposition: :","").trim();
        		}
        		
        	//// System.out.println(DateReported+"   "+TimeReported+"   "+Location+"   "+IncidentNum+"   "+DateOccurred+"  "+TimeOccurred+"  "+IncidentType+"  "+ReportNum+"  "+Disposition);
        	
        		String dr =  DateReported;
        	    String oc = DateReported;
        	 UniversityData recordData = new UniversityData();
        		      recordData.IncidentNumber = IncidentNum;
        		      recordData.ReportedDateTime=	 dateToConvert( dr, "MM/dd/yy") ;
        		      recordData.OffenseDateTime= 	 dateToConvert( oc, "MM/dd/yy") ;
        		      recordData.Location= Location;
        		      recordData.IncidentType= IncidentType;
        		      recordData.Disposition= Disposition;
        		      finalRecordsList.add(recordData);
        		     
        	}
        	 pd.close();
  	      DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}  catch (IOException e) {
    		
    		e.printStackTrace();
    	}
    }
    public  void RensselaerPolytechnicInstituteExtractionMethod()
    {
    	String UniversityName = "RensselaerPolytechnicInstitute";

    	File input = new File("D:\\CrimeReportPDFS\\RensselaerPolytechnicInstituteCrimeReport.pdf"); 
    	PDDocument pd;
    	
    	try {
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
        
        	String pageText = reader.getText(pd);
        	
        	ArrayList<String> recordList= new ArrayList<String>();
        	String[] lines = pageText.split("\n");
        	
        	for(String line : lines)
        	{
        		if(line.contains("date reported:") || line.contains("location :")|| line.contains("date and time occurred from - occurred to:")
        		|| line.contains("incident :") || line.contains("disposition:") || line.contains("report #:"))
        		{
        			recordList.add(line);
           	}
        		
        	}
        	Iterator iterator = recordList.iterator();

ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
        	while(iterator.hasNext())
        	{
        		String ReportNum = "";
            	String IncidentType = "";
            	String Location = "";
            	String DateReported ="";
            	String DateOccurred = "";
            	String Disposition = "";
            	String TimeOccurred ="";
            	String TimeReported = "";
            	String IncidentNum = "";
        		String firstline = iterator.next().toString().trim();
        		
        		if(firstline.contains("date reported:"))
        		{
        			String modifiedline = firstline.replaceFirst("date reported:","").trim();
        			String[] words = modifiedline.split("location :");
        			
        			if(words.length == 2)
        			{
        				String ReportedDateTime = words[0].toString();
        				 String[] items = ReportedDateTime.split("-");
	        				if(items.length >= 2)
	        				{
	        				DateReported = items[0].toString();
	        				String[] timelements = items[1].toString().split("at");
	        				if(timelements.length > 1)
	        				TimeReported = timelements[1].toString();
	        				}
        				String LocationText = words[1].toString();
        				String[] worditems = LocationText.split("event #:");
        				if(worditems.length == 2)
        				{ 
        				Location = worditems[0].toString();
        				IncidentNum = worditems[1].toString();
        				}
        			}
        			
           		}
        		
        		String SecondLine = iterator.next().toString().trim();
        		if(SecondLine.startsWith("date and time occurred from - occurred to:"))
        		{
        			String DateTimeOccurred = SecondLine.replaceFirst("date and time occurred from - occurred to:","").trim();
        			String[] elements = DateTimeOccurred.split("-");
        			if(elements.length >= 2)
        			{
        				String DateTime = elements[0].toString();
        				String[] items = DateTime.split("-");
        				if(items.length >= 2)
        				{
        				DateOccurred = items[0].toString();
        				String[] timeItems = items[1].toString().split("at");
        				if(timeItems.length > 1)
        				TimeOccurred = timeItems[1].toString();
        				}
        			}
        		}
        		
        		String ThirdLine = iterator.next().toString().trim();
        		if(ThirdLine.startsWith("incident :"))
        		{
        			String[] elements = ThirdLine.replaceFirst("incident :","").trim().split("report #:");
        			if(elements.length >= 2)
        			{
        			IncidentType = elements[0].toString();
        			if(IncidentType.charAt(0)== '-')
        			{
        				IncidentType = IncidentType.replace('-',' ').trim();
        			}
        			ReportNum = elements[1].toString();
        			}
             		}
        		
        		String forthLine = iterator.next().toString().trim();
        		
        		if(forthLine.startsWith("disposition:"))
        		{
        			 Disposition = forthLine.replaceFirst("disposition: :","").trim();
        		}
        		
        		//// System.out.println(DateReported+"   "+TimeReported+"   "+Location+"   "+IncidentNum+"   "+DateOccurred+"  "+TimeOccurred+"  "+IncidentType+"  "+ReportNum+"  "+Disposition);	
String dr =  DateReported+" - "+TimeReported;
String oc = DateOccurred+" - "+TimeOccurred;
        		 UniversityData recordData = new UniversityData();
        			      recordData.IncidentNumber = IncidentNum;
        			      recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yy  -  H:mm") ;
        			      recordData.OffenseDateTime=   dateToConvert( oc, "MM/dd/yy  -  H:mm") ;
        			      recordData.Location= Location;
        			      recordData.IncidentType= IncidentType;
        			      recordData.Disposition= Disposition;
        			      finalRecordsList.add(recordData);

        	}
        	 pd.close();
  	      DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	} catch (InvalidPasswordException e) {
    				e.printStackTrace();
    	} catch (IOException e) {
    		
    		e.printStackTrace();
    	}
    }
         
    public void CaseWesternReserveUniversityExtractionMethod(){
    	String UniversityName = "CaseWesternReserveUniversity";
	     File input = new File("D:\\CrimeReportPDFS\\CaseWesternReserveUniversityCrimeReport.pdf"); 
		PDDocument pd;
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		   
		try {
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        
	        String pageText = reader.getText(pd);
	        String content="";
		       if(!( pageText.indexOf("Thefts are listed as misdemeanor or felony") == -1))
		       {
		    	  content =   pageText.substring(0, pageText.indexOf("Thefts are listed as misdemeanor or felony"));
		       }
		        
		   String[] lines = content.split("\n");
	    	ArrayList<String> recordList= new ArrayList<String>();
	   	
	    	for(String line : lines)
	    	{
	    		
	    		if(!(line.contains("DAILY CRIME LOG ") || line.contains("INCIDENT #")|| line.contains("Off Campus Crimes Involving CWRU Persons/Property/In CWRU PD patrol zone")
	    			||	Character.isAlphabetic(line.charAt(0))|| line.isEmpty() || !(line.contains("/"))))
	    		{
	    			recordList.add(line.trim());
	    			
	    		}
	    			
	    	}
	    	  
	    	for(String record : recordList )
	    	{
	    		String ReportNum = "";
		    	String IncidentType = "";
		    	String Location = "";
		    	String DateReported = "";
		    	String Disposition = "";
		    	String TimeReported = ""; 
	    		String[] tokens= record.split(" ");
	    		
	    	
	    		if(tokens.length >=6 )
	    		{
	    			boolean ReportNumAvailable= false;
	    			String IncidentNumber = tokens[0].toString();
	    			
	    			String regex = "[0-9]+"; 
	    			if(IncidentNumber.matches(regex))
	    			{
	    				ReportNumAvailable = true;
	    			}
	    			if(ReportNumAvailable)
	    			{
	    	          ReportNum = IncidentNumber;
	    			}
	    			
	    			if(tokens[1].toString().contains("/"))
	    			{
	    	          DateReported = tokens[1].toString();
	    			}
	    			
	    			if(tokens[2].toString().length() == 4 && tokens[2].toString().matches(regex))
	    			{
	    				TimeReported =  tokens[2].toString();
	    			}
	    			int IncidentTypeLastIndex = 3;
	    			for(int i= 3;i<=tokens.length-1;i++ )
		    	      {
	    				if(tokens[i].toString().contains("-"))
		    			{
	    					IncidentTypeLastIndex = i;
		    			}
		    	      }
	    			String IncidentTypeElement ="";
	    			for(int i= 3;i<=IncidentTypeLastIndex;i++ )
		    	      {
	    				IncidentTypeElement = IncidentType + tokens[i].toString();
		    	      }
	    			IncidentType = IncidentTypeElement.replace('-',' ');
	    			int lastIndex = tokens.length-1;
	    			String DispositionElement="";
	    	      for(int i= 4;i<=tokens.length-1;i++ )
	    	      {
	    	    	  if(tokens[i].toString().contains("Closed") || tokens[i].toString().contains("Open") )
	    	    	  {
	    	    		  lastIndex = i;
	    	    		  DispositionElement = tokens[i].toString();
	    	    	  }
	    	      }
	    	      if(DispositionElement.contains("Open"))
	    	      {
	    	    	  Disposition = "Open";
	    	      }
	    	      else
	    	      {
	    	    	  Disposition = "Closed";
	    	      }
	    	   
	    	   for(int i=5;i<= lastIndex-1;i++ )
	    	   {
	    		   Location = Location+tokens[i].toString();
	    	   }
	        	
	    	}
	    	//// System.out.println(ReportNum+"  "+IncidentType+"  "+DateOccurred+"  "+Location+"  "+Disposition+"  "+TimeOccurred);
	    	if(!ReportNum.isEmpty())
	    	{
	    		int year = Year.now().getValue();
	    		String oc =  DateReported+"/"+year+" - "+TimeReported;
	    		 UniversityData recordData = new UniversityData();
	    			      recordData.IncidentNumber = ReportNum;
	    			      
	    			      recordData.ReportedDateTime= null;
	    			      recordData.OffenseDateTime=  dateToConvert( oc,"MM/d/yyyy - Hmm") ;
	    			      recordData.Location= Location;
	    			      recordData.IncidentType= IncidentType;
	    			      recordData.Disposition= Disposition;
	    			      finalRecordsList.add(recordData);
	    	
	      }
	    	}
	    	 pd.close();
	    	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	   
		} catch (InvalidPasswordException e) {
					e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }
    public void UniversityofCaliforniaatDavisExtractionMethod(){
    	String UniversityName = "UniversityofCaliforniaatDavis";
	     File input = new File("D:\\CrimeReportPDFS\\UniversityofCaliforniaatDavisCrimeReport.pdf"); 
		PDDocument pd;
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		   
		try {
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        
	        String pageText = reader.getText(pd);
	        String[] lines = pageText.split("\n");
	    	ArrayList<String> recordList= new ArrayList<String>();
	    
	    	
	    	for(String line : lines)
	    	{
	    		if(!(line.contains("UC DAVIS") || line.contains("POLICE DEPARTMENT")|| line.contains("Dispo")
	    				|| line.split("\t").length < 6))
	    		{
	    			recordList.add(line);
	    		}
	    			
	    	}
	    	  
	    	for(String record : recordList )
	    	{
	    		String ReportNum = "";
		    	String IncidentType = "";
		    	String Location = "";
		    	String DateReported = "";
		    	String Disposition = "";
		    	String TimeReported = ""; 
	    		String[] tokens= record.split("\t");
	    		if(tokens.length >=6 )
	    		{
	    	    ReportNum = tokens[0].toString();
	    	    Location = tokens[1].toString();
	    	    DateReported =  tokens[2].toString();
	    	    TimeReported =   tokens[3].toString();
	    	    IncidentType = tokens[4].toString();
	    	    Disposition = tokens[5].toString();
	    	   
	        	
	    	}
	    	//// System.out.println(ReportNum+"  "+IncidentType+"  "+DateOccurred+"  "+Location+"  "+Disposition+"  "+TimeOccurred);
	    	String dr = DateReported+" - "+TimeReported;
	    	if(dr.contains("N/A"))
	    	{
	    		dr = dr.replace("N/A","0:0:0");
	    	}
	    	
	    		 UniversityData recordData = new UniversityData();
	    			      recordData.IncidentNumber = ReportNum;
	    			      recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy - H:mm:ss") ;
	    			      recordData.OffenseDateTime= null;
	    			      recordData.Location= Location;
	    			      recordData.IncidentType= IncidentType;
	    			      recordData.Disposition= Disposition;
	    			      finalRecordsList.add(recordData);
	    	
	      } pd.close();
	    	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	   
		} catch (InvalidPasswordException e) {
					e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }
    public void UnivesityofGeorgiaExtractionMethod(){
    	String UniversityName = "UnivesityofGeorgia";
	     File input = new File("D:\\CrimeReportPDFS\\UnivesityofGeorgiaCrimeReport.pdf"); 
		PDDocument pd;
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		   
		try {
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        
	        String pageText = reader.getText(pd);
	        int index=0;
	        String content =pageText;
	        if(pageText.contains("CASE #"))
	        {
	        	index = pageText.indexOf("CASE #");
	        	content = pageText.substring(index);
	        }
	        String[] lines = content.split("\n");
	    	ArrayList<String> recordList= new ArrayList<String>();
	    
	    	
	    	for(String line : lines)
	    	{
	    		
	    		String[] elements = line.split(" ");
	    		boolean caseNumAvailable=false;
	    		for(int i=0;i<= elements[0].toString().length()-1;i++)
	    		{
	    			if(Character.isDigit(elements[0].toString().charAt(i)) || elements[0].toString().charAt(i) == '-' )
	    			{
	    				caseNumAvailable= true;
	    			}
	    			else
	    			{
	    				caseNumAvailable=false;
	    				break;
	    			}
	    		}
	    		if(elements[0].toString().length()>= 7 && caseNumAvailable)
	    		{
	    			recordList.add(line.trim());
	    		}
	    			
	    	}
	    	  
	    	for(String record : recordList )
	    	{
	    		
	    		String ReportNum = "";
		    	String IncidentType = "";
		    	String Location = "";
		    	String DateReported = "";
		    	String Disposition = "";
		    	String TimeReported = ""; 
		    	String DateOccurred ="";
		    	String TimeOccurred = "";
	    		String[] tokens= record.split(" ");
	    		ReportNum = tokens[0].toString();
	    		int dateReportedIndex = 3;
	    		for(int i=1;i<=tokens.length-1;i++)
	    		{
	    			if(tokens[i].contains("/"))
	    			{
	    				dateReportedIndex=i;
	    				break;
	    			}
	    			else
	    			{
	    				IncidentType = IncidentType+tokens[i].toString();
	    			}
	    		}
	    		int dateReportedEndIndex = 6;
	    		for(int i=dateReportedIndex; i<=tokens.length-1;i++)
	    		{
	    			if(tokens[i].toString().matches("[0-9/]+") )
	    			{
	    				DateReported = tokens[i].toString();
	    				dateReportedEndIndex=i;
        				break;
	    			}
	    			
	    		}
	    		
	    		for(int i = dateReportedEndIndex+1;i<= tokens.length-1;i++)
	    		{
	    			if(tokens[i].toString().matches("[0-9/]+") )
	    			{
	    				DateOccurred = tokens[i].toString();
	      				break;
	    			}
	    		}
	    		
	    		Location = "-";
	    				//// System.out.println(ReportNum+"  "+IncidentType+"  "+DateOccurred+"  "+Location+"  "+Disposition+"  "+TimeOccurred);
	    	
	    		 UniversityData recordData = new UniversityData();
	    			      recordData.IncidentNumber = ReportNum;
	    			      recordData.ReportedDateTime= null;
	    			      recordData.OffenseDateTime= null;
	    			      recordData.Location= Location;
	    			      recordData.IncidentType= IncidentType;
	    			      recordData.Disposition= "";
	    			      finalRecordsList.add(recordData);
	    	
	      }
	    	 pd.close();
	    	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	   
		} catch (InvalidPasswordException e) {
					e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }
    public void ArizonaStateUnivesityExtractionMethod()
    {
    	String UniversityName = "ArizonaStateUnivesity";
	     File input = new File("D:\\CrimeReportPDFS\\ArizonaStateUnivesityCrimeReport.pdf"); 
		PDDocument pd;
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		   
		try {
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        
	        String pageText = reader.getText(pd);
	        String[] lines = pageText.split("\n");
	    	ArrayList<String> recordList= new ArrayList<String>();
	    
	    	
	    	for(String line : lines)
	    	{
	    		if(!(line.contains("Police Log") || line.contains("Arizona State University Police Department")||
	    				line.contains("Incident # ") || line.split("\t").length < 10))
	    		{
	    			recordList.add(line);
	    		}
	    			
	    	}
	    	  
	    	for(String record : recordList )
	    	{
	    		String ReportNum = "";
		    	String IncidentType = "";
		    	String Location = "";
		    	String DateReported = "";
		    	String Disposition = "";
		    	String TimeReported = ""; 
		    	String DateOccurred ="";
		    	String TimeOccurred = "";
	    		String[] tokens= record.split("\t");
	    		if(tokens.length ==10 )
	    		{
	    	    ReportNum = tokens[0].toString();
	    	    DateReported = tokens[1].toString();
	    	    TimeReported =  tokens[2].toString();
	    	    DateOccurred =   tokens[3].toString();
	    	    TimeOccurred = tokens[4].toString();
	    	    
	    	    IncidentType = tokens[7].toString();
	    	    Location = tokens[8].toString();
	    	    Disposition = tokens[9].toString();
	    	  
	        	
	    	}
	    	//// System.out.println(ReportNum+"  "+IncidentType+"  "+DateOccurred+"  "+Location+"  "+Disposition+"  "+TimeOccurred);
	    	
	    		 UniversityData recordData = new UniversityData();
	    			      recordData.IncidentNumber = ReportNum;
	    			      int year = Year.now().getValue();
	    			  	  String dr = DateReported+"/"+year+" - "+TimeReported;
	    			  	  String oc = DateOccurred+"/"+year+" - "+TimeOccurred;
	    			  	
	    			      recordData.ReportedDateTime=  dateToConvert( dr, "MM/d/yyyy - Hmm") ;
	    			      recordData.OffenseDateTime=  dateToConvert( oc, "MM/d/yyyy - Hmm") ;
	    			      recordData.Location= Location;
	    			      recordData.IncidentType= IncidentType;
	    			      recordData.Disposition= Disposition ;
	    			      finalRecordsList.add(recordData);
	    	
	      }
	    	 pd.close();
	    	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	   
		} catch (InvalidPasswordException e) {
					e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }
    public void TexasAMUniversityExtractionMethod()
    {

    	String UniversityName = "TexasAMUniversity";
	     File input = new File("D:\\CrimeReportPDFS\\TexasAMUniversityCrimeReport.pdf"); 
		PDDocument pd;
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		   
		try {
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        
	        String pageText = reader.getText(pd);
	        String[] lines = pageText.split("\n");
	    	ArrayList<String> recordList= new ArrayList<String>();
	    
	    	
	    	for(String line : lines)
	    	{
	    		if(!(line.contains("Crime Log") || line.contains("Case Number") ||  line.split("\t").length < 8))
	    		{
	    			recordList.add(line);
	    		}
	    			
	    	}
	    	  
	    	for(String record : recordList )
	    	{
	    		String ReportNum = "";
		    	String IncidentType = "";
		    	String Location = "";
		    	String DateReported = "";
		    	String Disposition = "";
		    	String TimeReported = ""; 
		    	String DateOccurred ="";
		    	String TimeOccurred = "";
	    		String[] tokens= record.split("\t");
	    		if(tokens.length == 8 )
	    		{
	    			IncidentType = tokens[0].toString();
	    			ReportNum = tokens[1].toString();
	    			DateReported =  tokens[2].toString();
	    			DateOccurred =   tokens[3].toString();
	    			TimeOccurred = tokens[4].toString();
	    	    	Location = tokens[5].toString();
	    	    
	    	      Disposition = tokens[7].toString();
	    	  
	        	
	    	}
	    	//// System.out.println(ReportNum+"  "+IncidentType+"  "+DateOccurred+"  "+Location+"  "+Disposition+"  "+TimeOccurred);
	    	String dr = DateReported;
	        String oc = DateOccurred;
	    		if(dr.contains("Hours"))
	    		{
	    			dr= dr.replace("Hours","").trim();
	    			
	    		}
	    		if(oc.contains("Hours"))
	    		{
	    			oc= oc.replace("Hours","").trim();
	    			
	    		}
	    		 UniversityData recordData = new UniversityData();
	    			      recordData.IncidentNumber = ReportNum;
	    			      recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy");
	    			      recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
	    			      recordData.Location= Location;
	    			      recordData.IncidentType= IncidentType;
	    			      recordData.Disposition= Disposition ;
	    			      finalRecordsList.add(recordData);
	    	
	      }
	    	 pd.close();
	    	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	   
		} catch (InvalidPasswordException e) {
					e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }
    public void UniversityofNotreDameExtractionMethod(){
    	String UniversityName = "UniversityofNotreDame";
	     File input = new File("D:\\CrimeReportPDFS\\UniversityofNotreDameCrimeReport.pdf"); 
		PDDocument pd;
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		   
		try {
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        
	        String pageText = reader.getText(pd);
	        String content = pageText;
	        if(pageText.contains("Case Number"))
	        {
	        int index = pageText.indexOf("Case Number");
	        content = pageText.substring(index);
	        }
	        
	        String[] lines = content.split("\n");
	    	ArrayList<String> recordList= new ArrayList<String>();
	        
	    	
	    	for(String line : lines)
	    	{
	    		if(!(line.split("\t").length < 6 || line.contains("Case Number")))
	    		{
	    			recordList.add(line);
	    				    			
	    		}
	    			
	    	}
	    	  Iterator iterator = recordList.iterator();
	    	while(iterator.hasNext() )
	    	{
	    		String record = iterator.next().toString();
	    		String ReportNum = "";
		    	String IncidentType = "";
		    	String Location = "";
		    	String DateReported = "";
		    	String Disposition = "";
		    	String TimeReported = ""; 
		    	String DateOccurred ="";
		    	String TimeOccurred = "";
	    		String[] tokens= record.split("\t");
	    		if(tokens.length >= 6 )
	    		{
	    	    ReportNum = tokens[0].toString();
	    	    DateReported = tokens[1].toString();
	    	    Location =  tokens[2].toString();
	    	   
	    	    IncidentType = tokens[tokens.length - 3].toString();
	    	    Disposition = tokens[tokens.length - 1].toString();
	    	  }
	    		
	    		
	    	//// System.out.println(ReportNum+"  "+IncidentType+"  "+DateOccurred+"  "+Location+"  "+Disposition+"  "+TimeOccurred);
	    	
	    		 UniversityData recordData = new UniversityData();
	    			      recordData.IncidentNumber = ReportNum;
	    			      recordData.ReportedDateTime=  dateToConvert( DateReported, "MM/dd/yyyy H:mm") ;
	    			      recordData.OffenseDateTime=  dateToConvert( DateReported, "MM/dd/yyyy H:mm") ;
	    			      recordData.Location= Location;
	    			      recordData.IncidentType= IncidentType;
	    			      recordData.Disposition= Disposition ;
	    			      finalRecordsList.add(recordData);
	    	
	      }
	    	 pd.close();
	    	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	   
		} catch (InvalidPasswordException e) {
					e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }
    public void UniversityofCaliforniaatBerkeleyExtractionMethod()
    {
    	String UniversityName = "UniversityofCaliforniaatBerkeley";
        File input = new File("D:\\CrimeReportPDFS\\UniversityofCaliforniaatBerkeleyCrimeReport.pdf"); 
    	PDDocument pd;
    	ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
    	   
    	try {
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
           reader.setSortByPosition(true);
           reader.setWordSeparator("\t");
           
           String pageText = reader.getText(pd);
           String[] lines = pageText.split("\n");
       	ArrayList<String> recordList= new ArrayList<String>();
       
       	
       	for(String line : lines)
       	{
       		if(!(line.contains("DAILY CRIME LOG") || line.contains("Case #")|| line.split("\t").length < 5))
       		{
       			recordList.add(line);
       			
       		}
       			
       	}
       	  Iterator iterator = recordList.iterator();
       	while(iterator.hasNext() )
       	{

    		    String ReportNum = "";
    	    	String IncidentType = "";
    	    	String Location = "";
    	    	String DateReported = "";
    	    	String Disposition = "";
    	    	String TimeReported = ""; 
    	    	String DateOccurred ="";
    	    	String TimeOccurred = "";
    	 
       		String record = iterator.next().toString();
            String[] dataElements = record.split("\t");
          
            if (dataElements.length >= 5) {  
                 DateOccurred = dataElements[0].toString();
                 TimeOccurred = dataElements[1].toString();
                 DateReported = DateOccurred;
                 TimeReported = TimeOccurred;
                
                String nextElement = dataElements[2].toString();
                Boolean CaseNumAvailable = false;
                for(int i=0;i<= nextElement.length()-1;i++)
                {
                	if(nextElement.charAt(i) == '-' || Character.isDigit(nextElement.charAt(i)))
                	{
                		CaseNumAvailable = true;
                	}
                	else
                	{
                		CaseNumAvailable = false;
                		break;
                	}
                }
               if(CaseNumAvailable)
               {
            	   ReportNum = nextElement;
            	   IncidentType = dataElements[3].toString();
            	  }
               else
               {
            	   IncidentType = nextElement;
            	}
               String DispositionElement = dataElements[dataElements.length - 1].toString();
               if(DispositionElement.contains("CLOSED"))
               {
            	   Disposition = "CLOSED";
               }
               if(DispositionElement.contains("CLOSED BY CITATION"))
               {
            	   Disposition = "CLOSED BY CITATION";
               }
               if(DispositionElement.contains("UNDER"))
               {
            	   Disposition = "UNDER INVESTIGATION";
               }
               if(DispositionElement.contains("FIELD"))
               {
            	   Disposition = "FIELD IDENTIFICATION CARD";
               }
               if(DispositionElement.contains("WARNING ISSUED"))
               {
            	   Disposition = "WARNING ISSUED";
               }
               if(DispositionElement.contains("OPEN"))
               {
            	   Disposition = "OPEN";
               }
                if(dataElements.length == 5)
         	   {
         		   Location = dataElements[4].toString();
         	   }
                else
                {
                	Location = dataElements[dataElements.length - 2].toString();
                }
            }
            
            String dr = DateReported+" - "+TimeReported;
            String oc = DateOccurred+" - "+TimeOccurred;
           
             UniversityData recordData = new UniversityData();
       			      recordData.IncidentNumber = ReportNum;
       			      recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yy - H:mm") ;
       			      recordData.OffenseDateTime=   dateToConvert( oc, "MM/dd/yy - H:mm") ;
       			      recordData.Location= Location;
       			      recordData.IncidentType= IncidentType;
       			      recordData.Disposition= Disposition ;
       			      finalRecordsList.add(recordData);
       	
         }
        pd.close();
       	 DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
       	
    	} catch (InvalidPasswordException e) {
    				e.printStackTrace();
    	} catch (IOException e) {
    		
    		e.printStackTrace();
    	}
    }
    public void UniversityofCaliforniaIrvineExtractionMethod()
    {
    	String UniversityName = "UniversityofCaliforniaIrvine";
        File input = new File("D:\\CrimeReportPDFS\\UniversityofCaliforniaIrvineCrimeReport.pdf"); 
    	PDDocument pd;
    	ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
    	   
    	try {
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
           reader.setSortByPosition(true);
           reader.setWordSeparator("\t");
           
           String pageText = reader.getText(pd);
           String[] lines = pageText.split("\n");
       	ArrayList<String> recordList= new ArrayList<String>();
       
       	
       	for(String line : lines)
       	{
       		if(!(line.contains("Daily Crime and Fire Log") || line.contains("Case #") || line.split("\t").length < 6))
       		{
       			recordList.add(line.trim());
       			
       		}
       			
       	}
       	
       	  Iterator iterator = recordList.iterator();
       	while(iterator.hasNext() )
       	{

    		    String ReportNum = "";
    	    	String IncidentType = "";
    	    	String Location = "";
    	    	String DateReported = "";
    	    	String Disposition = "";
    	    	String TimeReported = ""; 
    	    	String DateOccurred ="";
    	    	String TimeOccurred = "";
    	    
       		String record = iterator.next().toString();
            String[] dataElements = record.split("\t");
            
            if (dataElements.length >= 6) {  
            	IncidentType = dataElements[0].toString();
            	ReportNum = dataElements[1].toString();
                DateReported = dataElements[2].toString();
                DateOccurred =  dataElements[3].toString();
                Location = dataElements[4].toString();
                Disposition = dataElements[5].toString();
               String dr = DateReported;
               String oc = DateOccurred;
               
               if(dr.contains("-"))
           	{
           		String[] values  = dr.split("-");
           		dr = values[0].toString();
           	}
               if(oc.contains("-"))
           	{
           		String[] values  = oc.split("-");
           		oc = values[0].toString();
           	}
       		 UniversityData recordData = new UniversityData();
       			      recordData.IncidentNumber = ReportNum;
       			      recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy H:mm") ;;
       			      recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy H:mm") ;
       			      recordData.Location= Location;
       			      recordData.IncidentType= IncidentType;
       			      recordData.Disposition= Disposition ;
       			      finalRecordsList.add(recordData);
       	
         }}
        pd.close();
       	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
       	
    	} catch (InvalidPasswordException e) {
    				e.printStackTrace();
    	} catch (IOException e) {
    		
    		e.printStackTrace();
    	}
    }
  
    public void HarvardUniversityExtractionMethod()
    {
    	String UniversityName = "HarvardUniversity";
	     File input = new File("D:\\CrimeReportPDFS\\HarvardUniversityCrimeReport.pdf"); 
		PDDocument pd;
		ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
		   
		try {
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	        
	        String pageText = reader.getText(pd);
	        int index=0;
	        String content= pageText;
	        if(pageText.contains("Disposition"))
	        {
	        	index= pageText.indexOf("Disposition");
	        	content = pageText.substring(index);
	        }
	        String[] lines = content.split("\n");
	    	ArrayList<String> recordList= new ArrayList<String>();
	    
	    	
	    	for(String line : lines)
	    	{
	    		String[] elements = line.split("\t");
	    		boolean validRecord = false;
	    		for(int i=0;i <= elements[0].toString().length()-1;i++)
	    		{ 
	    			if(Character.isDigit(elements[0].toString().charAt(i)) || elements[0].toString().charAt(i) == '/' )
	    			{
	    				validRecord = true;
	    			}
	    			else
	    			{
	    				validRecord = false;
	    				break;
	    			}
	    		}
	    		if(validRecord)
	    		{
	    			recordList.add(line);
	    		}
	    			
	    	}
	    	  Iterator iterator = recordList.iterator();
	    	while(iterator.hasNext() )
	    	{
	    		String record = iterator.next().toString();
	    		String ReportNum = "";
		    	String IncidentType = "";
		    	String Location = "";
		    	String DateReported = "";
		    	String Disposition = "";
		    	String TimeReported = ""; 
		    	String DateOccurred ="";
		    	String TimeOccurred = "";
	    		String[] tokens= record.split("\t");
	    		DateReported = tokens[0].toString();
	    		IncidentType = tokens[1].toString().substring(0, tokens[1].length()-9).trim();
	    		DateOccurred = tokens[1].toString().substring(tokens[1].length()-9 , tokens[1].length()).trim();
	    		Location = tokens[2].toString();
	    		if(tokens.length >= 4)
	    		{
	    			Disposition = tokens[3].toString();
	    		}
	    		else
	    		{
	    			Disposition = "CLOSED";
	    		}
	    	//// System.out.println(ReportNum+"  "+IncidentType+"  "+DateOccurred+"  "+Location+"  "+Disposition+"  "+TimeOccurred);
	    	
	    		 UniversityData recordData = new UniversityData();
	    			      recordData.IncidentNumber = "";
	    			      recordData.ReportedDateTime=  dateToConvert( DateReported, "MM/dd/yy") ;
	    			      recordData.OffenseDateTime=  dateToConvert( DateOccurred, "MM/dd/yy") ;
	    			      recordData.Location= Location;
	    			      recordData.IncidentType= IncidentType;
	    			      recordData.Disposition= Disposition ;
	    			      finalRecordsList.add(recordData);
	    	
	      }
	    	 pd.close();
	    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
	   
		} catch (InvalidPasswordException e) {
					e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }

    public void FloridaIntlUniversityExtractionMethod()
    {
    	String UniversityName = "FloridaIntlUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\FloridaIntlUniversityCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            	        
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	  if(!(line.contains("TYPE OF CRIME")|| line.contains("DATE/TIME REPORTED") ))
            	     {
            	    	  modifiedList.add(line);
              	      }
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String[] recordElements = record.split("\t");
        	if(recordElements.length == 8)
        	{
        		IncidentType = recordElements[0].toString();
          		ReportNum = recordElements[1].toString();
        		DateReported =  recordElements[2].toString();
        		DateOccurred = 	 recordElements[3].toString();
        		Location =  recordElements[recordElements.length - 3].toString();
        		Disposition =  recordElements[recordElements.length - 2].toString();
        	}
        	//// System.out.println(ReportNum+"   "+IncidentType+"    "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
         
        	String dr = DateReported;
        	String oc = DateOccurred;
        	String[] values1 = dr.split(" ");
        	String[] values2 = oc.split(" ");
        	if(values1.length >= 1)
        	{
        		dr = values1[0].toString();
        		
        	}
        	if(values2.length >= 1)
        	{
        		oc = values2[0].toString();
        		
        	}
        	UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          if(dr.equals(""))
          {
        	  recordData.ReportedDateTime= null;
          }else
          recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
          if(oc.equals(""))
          {
        	  recordData.OffenseDateTime= null;
          }else
        	  recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
       //  // System.out.println(finalRecordsList.size());
        DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}

    }
    public void RutgersUniversityatCamdenExtractionMethod()
    {
    	String UniversityName = "RutgersUniversityatCamden";
    	File input = new File("D:\\CrimeReportPDFS\\RutgersUniversityatCamdenCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            	        
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	  if(!(line.contains("Daily Crime and Fire Safety Log")|| line.contains("Rutgers PD - Camden") 
            			  || line.contains("Incident") || line.contains("Occurrence Date") || line.contains("Offenses on this report are based on State Criminal Codes")
            			  || line.contains("Report Run On:") || line.contains("Page") || line.trim().isEmpty() ))
            	     {
            		  String[] elements = line.split("\t");
            		if(elements[0].toString().matches("[0-9-]+"))
            		{
            	    	  modifiedList.add(line );
            	    	  }
              	      }
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	
        	String[] recordElements = record.split("\t");
        	if(recordElements[0].toString().matches("[0-9-]+"))
        	{
        		ReportNum = recordElements[0].toString();
        	}
        	if(!recordElements[1].toString().matches("[0-9]+") && !recordElements[1].toString().contains("Hrs"))
        	{
        		IncidentType = recordElements[1].toString();
        	}
        	else
        	{
        		DateReported =  recordElements[1].toString();
        	}
        		
        	if(recordElements[2].toString().contains("/") && recordElements[2].toString().contains("Hrs"))
        	{
        		DateReported =  recordElements[2].toString();
        	}
        	if(recordElements.length >=4)
        	{
        	if(recordElements[3].toString().contains("/") && recordElements[3].toString().contains("Hrs"))
        	{
        		DateOccurred =  recordElements[3].toString();
        	}	
        	}
        	if(DateOccurred.isEmpty())
        	{
        		DateOccurred = DateReported;
        	}
        		if(recordElements.length > 4)
        		{
        			if(!recordElements[recordElements.length - 2].toString().contains("Hrs"))
        			{ Location =  recordElements[recordElements.length - 2].toString(); }
        		}
        		if(!recordElements[recordElements.length - 1].toString().contains("Hrs"))
        		{
        		Disposition =  recordElements[recordElements.length - 1].toString();
        		}
        	
        //	// System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
         String dr = DateReported;
         String oc = DateOccurred;
        		if(dr.contains("Hrs"))
        		{
        			dr = dr.replace("Hrs","");
        		}
        		if(oc.contains("Hrs"))
        		{
        			oc = oc.replace("Hrs","");
        		}
        UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy Hmm") ;
          recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy Hmm") ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
        DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void RutgersUniversityatNewarkExtractionMethod()
    {
    	String UniversityName = "RutgersUniversityatNewark";
    	File input = new File("D:\\CrimeReportPDFS\\RutgersUniversityatNewarkCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            	        
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	  if(!(line.contains("Daily Crime and Fire Safety Log")|| line.contains("Rutgers PD - Camden") 
            			  || line.contains("Incident") || line.contains("Occurrence Date") || line.contains("Offenses on this report are based on State Criminal Codes")
            			  || line.contains("Report Run On:") || line.contains("Page") || line.trim().isEmpty() ))
            	     {
            		  String[] elements = line.split("\t");
            		if(elements[0].toString().matches("[0-9-]+"))
            		{
            	    	  modifiedList.add(line );
            	    	  }
              	      }
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	
        	String[] recordElements = record.split("\t");
        	if(recordElements[0].toString().matches("[0-9-]+"))
        	{
        		ReportNum = recordElements[0].toString();
        	}
        	if(!recordElements[1].toString().matches("[0-9]+") && !recordElements[1].toString().contains("Hrs"))
        	{
        		IncidentType = recordElements[1].toString();
        	}
        	else
        	{
        		DateReported =  recordElements[1].toString();
        	}
        		
        	if(recordElements[2].toString().contains("/") && recordElements[2].toString().contains("Hrs"))
        	{
        		DateReported =  recordElements[2].toString();
        	}
        	if(recordElements.length >=4)
        	{
        	if(recordElements[3].toString().contains("/") && recordElements[3].toString().contains("Hrs"))
        	{
        		DateOccurred =  recordElements[3].toString();
        	}	
        	}
        	if(DateOccurred.isEmpty())
        	{
        		DateOccurred = DateReported;
        	}
         
        		if(recordElements.length > 4)
        		{
        			if(!recordElements[recordElements.length - 2].toString().contains("Hrs"))
        			{ Location =  recordElements[recordElements.length - 2].toString(); }
        		}
        		if(!recordElements[recordElements.length - 1].toString().contains("Hrs"))
        		{
        		Disposition =  recordElements[recordElements.length - 1].toString();
        		}
        //	// System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
        		 String dr = DateReported;
                 String oc = DateOccurred;
                		if(dr.contains("Hrs"))
                		{
                			dr = dr.replace("Hrs","");
                		}
                		if(oc.contains("Hrs"))
                		{
                			oc = oc.replace("Hrs","");
                		}
                UniversityData recordData = new UniversityData();
                  recordData.IncidentNumber = ReportNum;
                  recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy Hmm") ;
                  recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy Hmm") ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
        DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void RutgersUniversityatNewBrunswickExtractionMethod()
    {
    	String UniversityName = "RutgersUniversityatNewBrunswick";
    	File input = new File("D:\\CrimeReportPDFS\\RutgersUniversityatNewBrunswickCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            	        
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	  if(!(line.contains("Daily Crime and Fire Safety Log")|| line.contains("Rutgers PD - Camden") 
            			  || line.contains("Incident") || line.contains("Occurrence Date") || line.contains("Offenses on this report are based on State Criminal Codes")
            			  || line.contains("Report Run On:") || line.contains("Page") || line.trim().isEmpty() ))
            	     {
            		  String[] elements = line.split("\t");
            		if(elements[0].toString().matches("[0-9-]+"))
            		{
            	    	  modifiedList.add(line );
            	    	  }
              	      }
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	
        	String[] recordElements = record.split("\t");
        	if(recordElements[0].toString().matches("[0-9-]+"))
        	{
        		ReportNum = recordElements[0].toString();
        	}
        	if(!recordElements[1].toString().matches("[0-9]+") && !recordElements[1].toString().contains("Hrs"))
        	{
        		IncidentType = recordElements[1].toString();
        	}
        	else
        	{
        		DateReported =  recordElements[1].toString();
        	}
        		
        	if(recordElements[2].toString().contains("/") && recordElements[2].toString().contains("Hrs"))
        	{
        		DateReported =  recordElements[2].toString();
        	}
        	if(recordElements.length >=4)
        	{
        	if(recordElements[3].toString().contains("/") && recordElements[3].toString().contains("Hrs"))
        	{
        		DateOccurred =  recordElements[3].toString();
        	}	
        	}
        	if(DateOccurred.isEmpty())
        	{
        		DateOccurred = DateReported;
        	}
        		if(recordElements.length > 4)
        		{
        			if(!recordElements[recordElements.length - 2].toString().contains("Hrs"))
        			{ Location =  recordElements[recordElements.length - 2].toString(); }
        		}
        		if(!recordElements[recordElements.length - 1].toString().contains("Hrs"))
        		{
        		Disposition =  recordElements[recordElements.length - 1].toString();
        		}
        //	// System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
        		 String dr = DateReported;
                 String oc = DateOccurred;
                		if(dr.contains("Hrs"))
                		{
                			dr = dr.replace("Hrs","");
                		}
                		if(oc.contains("Hrs"))
                		{
                			oc = oc.replace("Hrs","");
                		}
                UniversityData recordData = new UniversityData();
                  recordData.IncidentNumber = ReportNum;
                  recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy Hmm") ;
                  recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy Hmm") ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
        DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void IndianaUniversityatBloomingtonExtractionMethod()
    {
    	String UniversityName = "IndianaUniversityatBloomington";
    	File input = new File("D:\\CrimeReportPDFS\\IndianaUniversityatBloomingtonCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            	        
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	  if(line.contains("date reported:")|| line.contains("general location:") || line.contains("date occurred from:")
            		|| line.contains("date occurred to:") || line.contains("incident/offenses:") || line.contains("disposition:")
            		|| line.contains("modified date:") )
            	     {
            	    	  modifiedList.add(line.trim() );
              	      }
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	
            if(record.startsWith("date reported:"))
            {
            	if(record.contains("report #:"))
            	{
            	String[] DateReportedElements = record.split("report #:");
            	DateReported = DateReportedElements[0].toString().replace("date reported:","");
            	ReportNum = DateReportedElements[1].toString();
            	}
            	
            	if(record.contains("cad event #:"))
            	{
            	String[] DateReportedElements = record.split("cad event #:");
            	DateReported = DateReportedElements[0].toString().replace("date reported:","");
            	ReportNum = DateReportedElements[1].toString();
            	}
            }
            if(iterator.hasNext())
            {
            	String record2 = iterator.next().toString();
            if(record2.startsWith("general location:"))
            {
            	Location = record2.replace("general location:","");
            }
            }
            
            if(iterator.hasNext())
            {
            	String record3 = iterator.next().toString();
            if(record3.startsWith("date occurred from:"))
            {
            	DateOccurred = record3.replace("date occurred from:","");
            }
            }
            
            if(iterator.hasNext())
            {
            	String record4 = iterator.next().toString();
            if(record4.startsWith("date occurred to:"))
            {
            	String DateOccurredTo = record4.replace("date occurred to:","");
            }
            }
            
            if(iterator.hasNext())
            {
            	String record5 = iterator.next().toString();
            if(record5.startsWith("incident/offenses:"))
            {
            	IncidentType = record5.replace("incident/offenses:","");
            }
            }
            if(iterator.hasNext())
            {
            	String record6 = iterator.next().toString();
            if(record6.startsWith("disposition:"))
            {
            	Disposition = record6.replace("disposition:","");
            }
            }
            
            if(iterator.hasNext())
            {
            	String record7 = iterator.next().toString();
            if(record7.startsWith("modified date:"))
            {
               String	ModifiedDate = record7.replace("modified date:","");
            }
            }
     //// System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
        	
            if(DateReported.contains("at"))
        	{
            	DateReported = DateReported.replaceAll("at","");
        		
        	}
            
            if(DateOccurred.contains("at"))
        	{
            	DateOccurred = DateOccurred.replaceAll("at","");
        		
        	}
        	
            UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          
          recordData.ReportedDateTime=  dateToConvert( DateReported, "MM/dd/yy - EEE H:mm") ;;
          recordData.OffenseDateTime=  dateToConvert( DateOccurred, "MM/dd/yy - EEE H:mm") ;;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void UniversityatBuffaloExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityatBuffalo";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    
    public void UniversityofCaliforniaatSanDiegoExtractionMethod()
    {

    	String UniversityName = "UniversityofCaliforniaatSanDiego";
    	File input = new File("D:\\CrimeReportPDFS\\UniversityofCaliforniaatSanDiegoCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            	        
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
          
            for(int i=0;i<=lines.length-1;i++)
            {
            	
            	if(lines[i].toString().contains("Date Reported"))
            	{
               		modifiedList.add("IncidentType: "+lines[i-2].toString());
            		modifiedList.add("Location: "+lines[i-1].toString());
            		modifiedList.add(lines[i].toString());
            	}
            	
            	if(lines[i].toString().contains("Incident/Case#"))
            	{
            		modifiedList.add(lines[i].toString());
            	}
            	
            	if(lines[i].toString().contains("Date Occurred"))
            	{
                modifiedList.add(lines[i].toString());
            	}
            	
            	if(lines[i].toString().contains("Time Occurred"))
            	{
                modifiedList.add(lines[i].toString());
            	}
            	if(lines[i].toString().contains("Disposition"))
            	{
           		modifiedList.add(lines[i].toString().replace(":",""));
            	}
            	
            }
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
       
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        
        	    if(record.contains("IncidentType:"))
        		 { 
        			 IncidentType =record.replace("IncidentType:","").trim();
        		 }
        		 if(iterator.hasNext())
        		 {
        			 String LocationElement = iterator.next().toString();
        			
        			 if(LocationElement.contains("Location:"))
        			 {
        				 Location = LocationElement.replace("Location:","").trim();
        			 }
        		 }
        		 if(iterator.hasNext())
        		 {
        			 String DateReportedElement = iterator.next().toString();
        			
        			 if(DateReportedElement.contains("Date Reported"))
        			 { 
        				 DateReported = DateReportedElement.replace("Date Reported","").trim();
        			 }
        		 }
        		 
        		 if(iterator.hasNext())
        		 {
        			 String ReportNumElement = iterator.next().toString();
        			
        			 if(ReportNumElement.contains("Incident/Case#"))
        			 { 
        				 ReportNum = ReportNumElement.replace("Incident/Case#","").trim();
        			 }
        		 }
        		 
        		 if(iterator.hasNext())
        		 {
        			 String DateOccurredElement = iterator.next().toString();
        			
        			 if(DateOccurredElement.contains("Date Occurred"))
        			 { 
        				 DateOccurred = DateOccurredElement.replace("Date Occurred","").trim();
        			 }
        		 }
        		 
        		 if(iterator.hasNext())
        		 {
        			 String TimeOccurredElement = iterator.next().toString().trim();
        		
        			 if(TimeOccurredElement.contains("Time Occurred"))
        			 {
        				 TimeOccurred = TimeOccurredElement.replace("Time Occurred","");
        			 }
        		 }
        		 
        		
        		 if(iterator.hasNext())
        		 {
        			 String DispositionElement = iterator.next().toString();
        			
        			 if(DispositionElement.contains("Disposition"))
        			 { 
        				 Disposition = DispositionElement.replace("Disposition","");
        			 }
        		 }
        		 
        	
               
   //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
          UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime=  dateToConvert( DateReported,"M/dd/yyyy") ;
          recordData.OffenseDateTime=  dateToConvert( DateOccurred,"M/dd/yyyy") ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void JHU_AppliedPhysicsExtractionMethod()
    {
    	String UniversityName = "JohnsHopkinsUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\JHU_AppliedPhysicsCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	String content="";
            if(pageText.contains("Crime Log End Date:"))
            {
            	startIndex = pageText.indexOf("Crime Log End Date:");
            	content = pageText.substring(startIndex);
            }
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Date/Time Reported") || line.split("\t").length <= 3) )
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(!dataElements[0].toString().matches(".*\\d+.*"))
        	{
        		IncidentType = dataElements[0].toString();
        		
        	}
        	else
        	{
        		ReportNum = dataElements[0].toString();
        	}
        	if(!dataElements[1].toString().contains("/"))
    		{
    			ReportNum = dataElements[1].toString();
    		}
        	int dateReportedIndex=0;
        	for(int i=0;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains("/"))
            	{	
        			DateReported= dataElements[i].toString();
        			dateReportedIndex= i;
        			break;
            	}
        	}
           if(dataElements[dateReportedIndex+1].toString().contains("/"))
           {
           	   DateOccurred = dataElements[dateReportedIndex+1].toString();
           }
           if(dataElements[dataElements.length - 1].toString().contains("Open"))
           {
        	   Disposition = "Open";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else if(dataElements[dataElements.length - 1].toString().contains("Closed") ||  dataElements[dataElements.length - 1].toString().contains("Referred"))
           {
        	   Disposition = "Closed";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else
           {
        	   Location = dataElements[dataElements.length - 1].toString();
           }
        	   	
           
           if(dateReportedIndex+3 == dataElements.length-1)
           {
        		Location = dataElements[dataElements.length - 2].toString();
           }
           
      //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
       String dr = DateReported;
       String oc = DateOccurred+" - "+TimeOccurred;
           String[] dateValues1 = dr.split(" ");
       	if(dateValues1.length >= 1)
       	{
       		 dr = dateValues1[0];
       	}
        String[] dateValues2 = oc.split(" ");
       	if(dateValues2.length >= 1)
       	{
       		 oc = dateValues2[0];
       	}
           UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
          recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location= "AppliedPhysics-"+Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void	JHU_HarbourEastCampusExtractionMethod()
    {
    	String UniversityName = "JohnsHopkinsUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\JHU_HarbourEastCampusCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	String content="";
            if(pageText.contains("Crime Log End Date:"))
            {
            	startIndex = pageText.indexOf("Crime Log End Date:");
            	content = pageText.substring(startIndex);
            }
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Date/Time Reported") || line.split("\t").length <= 3) )
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(!dataElements[0].toString().matches(".*\\d+.*"))
        	{
        		IncidentType = dataElements[0].toString();
        		
        	}
        	else
        	{
        		ReportNum = dataElements[0].toString();
        	}
        	if(!dataElements[1].toString().contains("/"))
    		{
    			ReportNum = dataElements[1].toString();
    		}
        	int dateReportedIndex=0;
        	for(int i=0;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains("/"))
            	{	
        			DateReported= dataElements[i].toString();
        			dateReportedIndex= i;
        			break;
            	}
        	}
           if(dataElements[dateReportedIndex+1].toString().contains("/"))
           {
           	   DateOccurred = dataElements[dateReportedIndex+1].toString();
           }
           if(dataElements[dataElements.length - 1].toString().contains("Open"))
           {
        	   Disposition = "Open";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else if(dataElements[dataElements.length - 1].toString().contains("Closed") ||  dataElements[dataElements.length - 1].toString().contains("Referred"))
           {
        	   Disposition = "Closed";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else
           {
        	   Location = dataElements[dataElements.length - 1].toString();
           }
        	   	
           
           if(dateReportedIndex+3 == dataElements.length-1)
           {
        		Location = dataElements[dataElements.length - 2].toString();
           }
           
      //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
           String dr = DateReported;
           String oc = DateOccurred+" - "+TimeOccurred;
               String[] dateValues1 = dr.split(" ");
           	if(dateValues1.length >= 1)
           	{
           		 dr = dateValues1[0];
           	}
            String[] dateValues2 = oc.split(" ");
           	if(dateValues2.length >= 1)
           	{
           		 oc = dateValues2[0];
           	}
               UniversityData recordData = new UniversityData();
              recordData.IncidentNumber = ReportNum;
              recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
              recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location="HarbourEastCampus-"+ Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }  
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void	JHU_ColumbiaCampusExtractionMethod() 
    {
    	String UniversityName = "JohnsHopkinsUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\JHU_ColumbiaCampusCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	String content="";
            if(pageText.contains("Crime Log End Date:"))
            {
            	startIndex = pageText.indexOf("Crime Log End Date:");
            	content = pageText.substring(startIndex);
            }
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Date/Time Reported") || line.split("\t").length <= 3) )
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(!dataElements[0].toString().matches(".*\\d+.*"))
        	{
        		IncidentType = dataElements[0].toString();
        		
        	}
        	else
        	{
        		ReportNum = dataElements[0].toString();
        	}
        	if(!dataElements[1].toString().contains("/"))
    		{
    			ReportNum = dataElements[1].toString();
    		}
        	int dateReportedIndex=0;
        	for(int i=0;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains("/"))
            	{	
        			DateReported= dataElements[i].toString();
        			dateReportedIndex= i;
        			break;
            	}
        	}
           if(dataElements[dateReportedIndex+1].toString().contains("/"))
           {
           	   DateOccurred = dataElements[dateReportedIndex+1].toString();
           }
           if(dataElements[dataElements.length - 1].toString().contains("Open"))
           {
        	   Disposition = "Open";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else if(dataElements[dataElements.length - 1].toString().contains("Closed") ||  dataElements[dataElements.length - 1].toString().contains("Referred"))
           {
        	   Disposition = "Closed";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else
           {
        	   Location = dataElements[dataElements.length - 1].toString();
           }
        	   	
           
           if(dateReportedIndex+3 == dataElements.length-1)
           {
        		Location = dataElements[dataElements.length - 2].toString();
           }
           
      //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
           String dr = DateReported;
           String oc = DateOccurred+" - "+TimeOccurred;
               String[] dateValues1 = dr.split(" ");
           	if(dateValues1.length >= 1)
           	{
           		 dr = dateValues1[0];
           	}
            String[] dateValues2 = oc.split(" ");
           	if(dateValues2.length >= 1)
           	{
           		 oc = dateValues2[0];
           	}
               UniversityData recordData = new UniversityData();
              recordData.IncidentNumber = ReportNum;
              recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
              recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location="ColumbiaCampus-"+ Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }  
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void	JHU_WashDCCampusExtractionMethod() 
    {
    	String UniversityName = "JohnsHopkinsUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\JHU_WashDCCampusCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	String content="";
            if(pageText.contains("Crime Log End Date:"))
            {
            	startIndex = pageText.indexOf("Crime Log End Date:");
            	content = pageText.substring(startIndex);
            }
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Date/Time Reported") || line.split("\t").length <= 3) )
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(!dataElements[0].toString().matches(".*\\d+.*"))
        	{
        		IncidentType = dataElements[0].toString();
        		
        	}
        	else
        	{
        		ReportNum = dataElements[0].toString();
        	}
        	if(!dataElements[1].toString().contains("/"))
    		{
    			ReportNum = dataElements[1].toString();
    		}
        	int dateReportedIndex=0;
        	for(int i=0;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains("/"))
            	{	
        			DateReported= dataElements[i].toString();
        			dateReportedIndex= i;
        			break;
            	}
        	}
           if(dataElements[dateReportedIndex+1].toString().contains("/"))
           {
           	   DateOccurred = dataElements[dateReportedIndex+1].toString();
           }
           if(dataElements[dataElements.length - 1].toString().contains("Open"))
           {
        	   Disposition = "Open";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else if(dataElements[dataElements.length - 1].toString().contains("Closed") ||  dataElements[dataElements.length - 1].toString().contains("Referred"))
           {
        	   Disposition = "Closed";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else
           {
        	   Location = dataElements[dataElements.length - 1].toString();
           }
        	   	
           
           if(dateReportedIndex+3 == dataElements.length-1)
           {
        		Location = dataElements[dataElements.length - 2].toString();
           }
           
      //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
           String dr = DateReported;
           String oc = DateOccurred+" - "+TimeOccurred;
               String[] dateValues1 = dr.split(" ");
           	if(dateValues1.length >= 1)
           	{
           		 dr = dateValues1[0];
           	}
            String[] dateValues2 = oc.split(" ");
           	if(dateValues2.length >= 1)
           	{
           		 oc = dateValues2[0];
           	}
               UniversityData recordData = new UniversityData();
              recordData.IncidentNumber = ReportNum;
              recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
              recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location="WashDCCampus-"+ Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void	JHU_DorseyCampusExtractionMethod() 
    {
    	String UniversityName = "JohnsHopkinsUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\JHU_DorseyCampusCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	String content="";
            if(pageText.contains("Crime Log End Date:"))
            {
            	startIndex = pageText.indexOf("Crime Log End Date:");
            	content = pageText.substring(startIndex);
            }
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Date/Time Reported") || line.split("\t").length <= 3) )
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(!dataElements[0].toString().matches(".*\\d+.*"))
        	{
        		IncidentType = dataElements[0].toString();
        		
        	}
        	else
        	{
        		ReportNum = dataElements[0].toString();
        	}
        	if(!dataElements[1].toString().contains("/"))
    		{
    			ReportNum = dataElements[1].toString();
    		}
        	int dateReportedIndex=0;
        	for(int i=0;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains("/"))
            	{	
        			DateReported= dataElements[i].toString();
        			dateReportedIndex= i;
        			break;
            	}
        	}
           if(dataElements[dateReportedIndex+1].toString().contains("/"))
           {
           	   DateOccurred = dataElements[dateReportedIndex+1].toString();
           }
           if(dataElements[dataElements.length - 1].toString().contains("Open"))
           {
        	   Disposition = "Open";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else if(dataElements[dataElements.length - 1].toString().contains("Closed") ||  dataElements[dataElements.length - 1].toString().contains("Referred"))
           {
        	   Disposition = "Closed";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else
           {
        	   Location = dataElements[dataElements.length - 1].toString();
           }
        	   	
           
           if(dateReportedIndex+3 == dataElements.length-1)
           {
        		Location = dataElements[dataElements.length - 2].toString();
           }
           
      //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
           String dr = DateReported;
           String oc = DateOccurred+" - "+TimeOccurred;
               String[] dateValues1 = dr.split(" ");
           	if(dateValues1.length >= 1)
           	{
           		 dr = dateValues1[0];
           	}
            String[] dateValues2 = oc.split(" ");
           	if(dateValues2.length >= 1)
           	{
           		 oc = dateValues2[0];
           	}
               UniversityData recordData = new UniversityData();
              recordData.IncidentNumber = ReportNum;
              recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
              recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location= "DorseyCampus-"+Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         } 
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void	JHU_EBaltimoreCampusExtractionMethod() 
    {
    	String UniversityName = "JohnsHopkinsUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\JHU_EBaltimoreCampusCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	String content="";
            if(pageText.contains("Crime Log End Date:"))
            {
            	startIndex = pageText.indexOf("Crime Log End Date:");
            	content = pageText.substring(startIndex);
            }
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Date/Time Reported") || line.split("\t").length <= 3) )
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(!dataElements[0].toString().matches(".*\\d+.*"))
        	{
        		IncidentType = dataElements[0].toString();
        		
        	}
        	else
        	{
        		ReportNum = dataElements[0].toString();
        	}
        	if(!dataElements[1].toString().contains("/"))
    		{
    			ReportNum = dataElements[1].toString();
    		}
        	int dateReportedIndex=0;
        	for(int i=0;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains("/"))
            	{	
        			DateReported= dataElements[i].toString();
        			dateReportedIndex= i;
        			break;
            	}
        	}
           if(dataElements[dateReportedIndex+1].toString().contains("/"))
           {
           	   DateOccurred = dataElements[dateReportedIndex+1].toString();
           }
           if(dataElements[dataElements.length - 1].toString().contains("Open"))
           {
        	   Disposition = "Open";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else if(dataElements[dataElements.length - 1].toString().contains("Closed") ||  dataElements[dataElements.length - 1].toString().contains("Referred"))
           {
        	   Disposition = "Closed";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else
           {
        	   Location = dataElements[dataElements.length - 1].toString();
           }
        	   	
           
           if(dateReportedIndex+3 == dataElements.length-1)
           {
        		Location = dataElements[dataElements.length - 2].toString();
           }
           
      //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
           String dr = DateReported;
           String oc = DateOccurred+" - "+TimeOccurred;
               String[] dateValues1 = dr.split(" ");
           	if(dateValues1.length >= 1)
           	{
           		 dr = dateValues1[0];
           	}
            String[] dateValues2 = oc.split(" ");
           	if(dateValues2.length >= 1)
           	{
           		 oc = dateValues2[0];
           	}
               UniversityData recordData = new UniversityData();
              recordData.IncidentNumber = ReportNum;
              recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
              recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location= "EBaltimoreCampus-"+Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void	JHU_HomewoodCampusExtractionMethod()
    {
    	String UniversityName = "JohnsHopkinsUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\JHU_HomewoodCampusCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	String content="";
            if(pageText.contains("Crime Log End Date:"))
            {
            	startIndex = pageText.indexOf("Crime Log End Date:");
            	content = pageText.substring(startIndex);
            }
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Date/Time Reported") || line.split("\t").length <= 3) )
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(!dataElements[0].toString().matches(".*\\d+.*"))
        	{
        		IncidentType = dataElements[0].toString();
        		
        	}
        	else
        	{
        		ReportNum = dataElements[0].toString();
        	}
        	if(!dataElements[1].toString().contains("/"))
    		{
    			ReportNum = dataElements[1].toString();
    		}
        	int dateReportedIndex=0;
        	for(int i=0;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains("/"))
            	{	
        			DateReported= dataElements[i].toString();
        			dateReportedIndex= i;
        			break;
            	}
        	}
           if(dataElements[dateReportedIndex+1].toString().contains("/"))
           {
           	   DateOccurred = dataElements[dateReportedIndex+1].toString();
           }
           if(dataElements[dataElements.length - 1].toString().contains("Open"))
           {
        	   Disposition = "Open";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else if(dataElements[dataElements.length - 1].toString().contains("Closed") ||  dataElements[dataElements.length - 1].toString().contains("Referred"))
           {
        	   Disposition = "Closed";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else
           {
        	   Location = dataElements[dataElements.length - 1].toString();
           }
        	   	
           
           if(dateReportedIndex+3 == dataElements.length-1)
           {
        		Location = dataElements[dataElements.length - 2].toString();
           }
           
      //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
           String dr = DateReported;
           String oc = DateOccurred+" - "+TimeOccurred;
               String[] dateValues1 = dr.split(" ");
           	if(dateValues1.length >= 1)
           	{
           		 dr = dateValues1[0];
           	}
            String[] dateValues2 = oc.split(" ");
           	if(dateValues2.length >= 1)
           	{
           		 oc = dateValues2[0];
           	}
               UniversityData recordData = new UniversityData();
              recordData.IncidentNumber = ReportNum;
              recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
              recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location="HomewoodCampus-"+ Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void	JHU_MontogomeryCampusExtractionMethod() 
    {
    	String UniversityName = "JohnsHopkinsUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\JHU_MontogomeryCampusCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	String content="";
            if(pageText.contains("Crime Log End Date:"))
            {
            	startIndex = pageText.indexOf("Crime Log End Date:");
            	content = pageText.substring(startIndex);
            }
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Date/Time Reported") || line.split("\t").length <= 3) )
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(!dataElements[0].toString().matches(".*\\d+.*"))
        	{
        		IncidentType = dataElements[0].toString();
        		
        	}
        	else
        	{
        		ReportNum = dataElements[0].toString();
        	}
        	if(!dataElements[1].toString().contains("/"))
    		{
    			ReportNum = dataElements[1].toString();
    		}
        	int dateReportedIndex=0;
        	for(int i=0;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains("/"))
            	{	
        			DateReported= dataElements[i].toString();
        			dateReportedIndex= i;
        			break;
            	}
        	}
           if(dataElements[dateReportedIndex+1].toString().contains("/"))
           {
           	   DateOccurred = dataElements[dateReportedIndex+1].toString();
           }
           if(dataElements[dataElements.length - 1].toString().contains("Open"))
           {
        	   Disposition = "Open";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else if(dataElements[dataElements.length - 1].toString().contains("Closed") ||  dataElements[dataElements.length - 1].toString().contains("Referred"))
           {
        	   Disposition = "Closed";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else
           {
        	   Location = dataElements[dataElements.length - 1].toString();
           }
        	   	
           
           if(dateReportedIndex+3 == dataElements.length-1)
           {
        		Location = dataElements[dataElements.length - 2].toString();
           }
           
      //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
           String dr = DateReported;
           String oc = DateOccurred+" - "+TimeOccurred;
               String[] dateValues1 = dr.split(" ");
           	if(dateValues1.length >= 1)
           	{
           		 dr = dateValues1[0];
           	}
            String[] dateValues2 = oc.split(" ");
           	if(dateValues2.length >= 1)
           	{
           		 oc = dateValues2[0];
           	}
               UniversityData recordData = new UniversityData();
              recordData.IncidentNumber = ReportNum;
              recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
              recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location="MontogomeryCampus-"+ Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void	JHU_PeabodyCampusExtractionMethod() 
    {
    	String UniversityName = "JohnsHopkinsUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\JHU_PeabodyCampusCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	String content="";
            if(pageText.contains("Crime Log End Date:"))
            {
            	startIndex = pageText.indexOf("Crime Log End Date:");
            	content = pageText.substring(startIndex);
            }
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Date/Time Reported") || line.split("\t").length <= 3) )
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(!dataElements[0].toString().matches(".*\\d+.*"))
        	{
        		IncidentType = dataElements[0].toString();
        		
        	}
        	else
        	{
        		ReportNum = dataElements[0].toString();
        	}
        	if(!dataElements[1].toString().contains("/"))
    		{
    			ReportNum = dataElements[1].toString();
    		}
        	int dateReportedIndex=0;
        	for(int i=0;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains("/"))
            	{	
        			DateReported= dataElements[i].toString();
        			dateReportedIndex= i;
        			break;
            	}
        	}
           if(dataElements[dateReportedIndex+1].toString().contains("/"))
           {
           	   DateOccurred = dataElements[dateReportedIndex+1].toString();
           }
           if(dataElements[dataElements.length - 1].toString().contains("Open"))
           {
        	   Disposition = "Open";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else if(dataElements[dataElements.length - 1].toString().contains("Closed") ||  dataElements[dataElements.length - 1].toString().contains("Referred"))
           {
        	   Disposition = "Closed";
        	   if(!dataElements[dataElements.length - 2].contains("/"))
        	   { Location = dataElements[dataElements.length - 2].toString();}
           }
           else
           {
        	   Location = dataElements[dataElements.length - 1].toString();
           }
        	   	
           
           if(dateReportedIndex+3 == dataElements.length-1)
           {
        		Location = dataElements[dataElements.length - 2].toString();
           }
           
      //  // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
           String dr = DateReported;
           String oc = DateOccurred+" - "+TimeOccurred;
               String[] dateValues1 = dr.split(" ");
           	if(dateValues1.length >= 1)
           	{
           		 dr = dateValues1[0];
           	}
            String[] dateValues2 = oc.split(" ");
           	if(dateValues2.length >= 1)
           	{
           		 oc = dateValues2[0];
           	}
               UniversityData recordData = new UniversityData();
              recordData.IncidentNumber = ReportNum;
              recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy") ;
              recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy") ;
          recordData.Location= "PeabodyCampus-"+Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }  
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void UniversityofCaliforniaLAExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofCaliforniaLA";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }

    public void DukeUniversityExtractionMethod()
    {
    	String UniversityName = "DukeUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\DukeUniversityCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
          	int startIndex=0;
          	int endIndex=0;
          	if(pageText.contains("(Crimes are included when they are reported to the Duke University Police Department)"))
          	{
          		 startIndex = pageText.indexOf("(Crimes are included when they are reported to the Duke University Police Department)");
          		 
          	}
          	if(pageText.contains("DISPOSITION DEFINITIONS"))
          	{
          		endIndex = pageText.indexOf("DISPOSITION DEFINITIONS")-24;
          		 
          	}
          	String content = pageText.substring(startIndex, endIndex);
            String[] lines = content.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	String[] elements = line.split(" ");
            	if(elements[0].toString().matches("[0-9-]+") && !line.trim().isEmpty() && line.split(" ").length >3)
            	{
            		modifiedList.add(line.trim());
            		
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split(" ");
        	
        	ReportNum = dataElements[0].toString();
        	if(dataElements[1].toString().matches("[0-9:]+"))
        	{
        		DateReported = 	dataElements[1].toString();
        		if((dataElements[2].toString().contains("a.m.") ||dataElements[2].toString().contains("p.m.")) && !dataElements[2].toString().contains(":") )
        		{
        			DateReported = DateReported + dataElements[2].toString();
        		}
        	}
        	int dateOccurredIndex = 3;
        	for(int i=2;i<=3;i++)
        	{
        		if(dataElements[i].toString().contains(":") || dataElements[i].toString().contains("/")  )
        		{
        			dateOccurredIndex =i;
        			break;
        		}
        	}
        	int dateOccurredEndIndex = dateOccurredIndex;
        	for(int i=dateOccurredIndex;i<=dateOccurredIndex+3;i++ )
        	{
        		if(dataElements[i].toString().contains("a.m.") ||dataElements[i].toString().contains("p.m."))
        				{
        			dateOccurredEndIndex = i;
        			break;
        				}
        	}
        	for(int i=dateOccurredIndex;i<=dateOccurredEndIndex;i++ )
        	{
        		DateOccurred = DateOccurred+ dataElements[i].toString();
        	}
        	if(dataElements.length > dateOccurredEndIndex+3)
        	{
        		Location = dataElements[dateOccurredEndIndex+1].toString()+dataElements[dateOccurredEndIndex+2].toString();
        	}
        	if(record.contains("Arrest"))
        	{
        		Disposition = "Arrest";
        		record.replace("Arrest","");
        	}
        	if(record.contains("Referred"))
        	{
        		Disposition = "Referred to other Department";
        		record.replace("Referred","");
        	}
        	if(record.contains("Inactive"))
        	{
        		Disposition = "Inactive";
        		record.replace("Inactive","");
        	}
        	if(record.contains("Exceptionally Cleared ") || record.contains("Exceptionally"))
        	{
        		Disposition = "Exceptionally Cleared ";
        		record.replace("Exceptionally","");
        		if(record.contains("Cleared"))
        		{
        			record.replace("Cleared","");
        		}
        	}
        	if(record.contains("Pending"))
        	{
        		Disposition = "Pending";
        		record.replace("Pending","");
        	}
        	if(record.contains("Unfounded"))
        	{
        		Disposition = "Unfounded";
        		record.replace("Unfounded","");
        	}
         // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
       
        	UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime= null;
          recordData.OffenseDateTime=  dateToConvert( DateOccurred, "MM/dd/yy") ;
          recordData.Location= Location;
          recordData.IncidentType= "";
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}

    }
    public void MichiganStateUniversityExtractionMethod()
    {
    	String UniversityName = "MichiganStateUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\MichiganStateUniversityCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	
            	if(!(line.contains("Crime Classification") || line.split("\t").length < 7))
            	{
            		modifiedList.add(line.trim());
            	}
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	String nextRecord = "";
        	String[] dataElements = record.split("\t");
        	if(dataElements.length == 7)
        	{
        		IncidentType = dataElements[0].toString();
        		Disposition	= dataElements[2].toString();
        		Location = dataElements[3].toString();
        		DateOccurred = dataElements[4].toString();
        		DateReported = dataElements[5].toString();
        		ReportNum = dataElements[6].toString();
        	}
        	
        	
         // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
          UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime=  dateToConvert( DateReported, "MM/dd/yyyy h:mma") ;
          recordData.OffenseDateTime=  dateToConvert( DateOccurred, "MM/dd/yyyy h:mma") ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }  
         pd.close();
        DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void UniversityofMissouriExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofMissouri";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofConnecticutExtractionMethod()
   {
	   String UniversityName = "UniversityofConnecticut";
		File input = new File("D:\\CrimeReportPDFS\\UniversityofConnecticutCrimeReport.pdf"); 
		PDDocument pd;
		try{
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	      	String pageText = reader.getText(pd);
	        String[] lines = pageText.split("\n");
	        ArrayList<String> modifiedList = new ArrayList<String>();
	        for(String line : lines)
	        {
	        	if(!(line.contains("Disposition") || line.split("\t").length < 8))
	        	{
	        		modifiedList.add(line.trim());
	        	}
	        	
	        }
	        
	     Iterator iterator = modifiedList.iterator();
	     ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	        
	     while(iterator.hasNext())
	     {
	    	String ReportNum="";
	    	String DateReported="";
	    	String TimeReported="";
	    	String Location="";
	    	String DateOccurred = "";
	    	String TimeOccurred = "";
	    	String IncidentType = "";
	    	String Disposition = "";
	    	String record = iterator.next().toString();
	    	
	    	String[] dataElements = record.split("\t");
	    	if(dataElements.length == 8)
	    	{
	    		ReportNum = dataElements[0].toString();
	    		DateReported	= dataElements[1].toString();
	    		TimeReported	= dataElements[2].toString();
	    		DateOccurred = dataElements[3].toString();
	    		TimeOccurred = dataElements[4].toString();
	    		IncidentType = dataElements[5].toString();
	    		Location = dataElements[6].toString();
	    		Disposition = dataElements[7].toString();
	    	}
	    	
	    	
	     // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
	     String dr =DateReported+" - "+TimeReported;
	     String oc = DateOccurred+" - "+TimeOccurred;
	    		 
	    				 
	    	
	    	UniversityData recordData = new UniversityData();
	      recordData.IncidentNumber = ReportNum;
	      recordData.ReportedDateTime=  dateToConvert( dr, "MM/dd/yyyy - H:mm") ;
	      recordData.OffenseDateTime=  dateToConvert( oc, "MM/dd/yyyy - H:mm") ;
	      recordData.Location= Location;
	      recordData.IncidentType= IncidentType;
	      recordData.Disposition= Disposition;
	      finalRecordsList.add(recordData);
	     
	     }  
	     pd.close();
	    DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
		}catch (IOException e) {
	       e.printStackTrace();
		}
   }
    /*
    public void GeorgetownUniversityExtractionMethod()
    {
    	String UniversityName = "GeorgetownUniversity";
    	File input = new File("D:\\CrimeReportPDFS\\GeorgetownUniversityCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            // System.out.println("ext method");
            for(String line : lines)
            {
            	if(!(line.contains("Disposition") || line.split("\t").length < 6))
            	{
            		modifiedList.add(line.trim());
            	}
            	
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
            
         while(iterator.hasNext())
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = iterator.next().toString();
        	
        	String[] dataElements = record.split("\t");
        	if(dataElements.length == 6)
        	{
        		
        		DateReported	= dataElements[0].toString();
        		IncidentType	= dataElements[1].toString().substring(0, dataElements[1].toString().length()-10);
        		ReportNum = dataElements[1].toString().substring(dataElements[1].toString().length()-10 , dataElements[1].toString().length()-1 );
        		DateOccurred = dataElements[2].toString();
        		TimeOccurred = dataElements[3].toString();
        		Location = dataElements[4].toString();
        		Disposition = dataElements[5].toString();
        	}
        	
        	
         // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
          UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime= DateReported;
          recordData.OffenseDateTime= DateOccurred+" - "+TimeOccurred;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
       DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }*/
    public void VirginiaTechExtractionMethod()
    {
    	String UniversityName = "VirginiaTech";
    	File input = new File("D:\\CrimeReportPDFS\\VirginiaTechCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Police Department, 330 Sterrett Drive, Blacksburg, Virginia 24061") || line.contains("VIRGINIA TECH POLICE") || line.contains("A Nationally Accredited Police Department")
            		 || line.contains("Disposition") || line.contains("www.police.vt.edu") || line.contains("MONTHLY CRIME LOG:")
            		 || line.contains("Case#") || line.contains("Occurrence") ||
            		 line.contains("Do Not Delete") || line.contains("Delete") || line.contains("VTPD – 098(a)")))
            	{
            		String[] elements = line.split("\t");
            		if(elements[0].toString().matches("[0-9-]+"))
            		{
            		modifiedList.add(line.trim());
            		
            		}
            	}
            	
            }
            
        
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
       
         for(int i=0;i<= modifiedList.toArray().length-1;i++ )
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = modifiedList.get(i);
        	String nextRecord="";
        	if(i+1 <= modifiedList.toArray().length-1 && modifiedList.get(i+1).split("\t").length <4 )
        	{
        		nextRecord = modifiedList.get(++i);
        	}
        	String[] dataElements = record.split("\t");
        	String[] dataElements1 = nextRecord.split("\t");
        	if(dataElements[0].toString().matches("[0-9-]+") && dataElements1[0].toString().matches("[0-9-]+"))
        	{
        		ReportNum = dataElements[0].toString()+dataElements1[0].toString();
        	}
        	if(dataElements[1].toString().matches("[0-9/]+"))
        	{
        		DateReported = dataElements[1].toString();
        	}
        	else
        	{
        		IncidentType = dataElements[1].toString();
        	}
        	
        	if(!dataElements[2].toString().matches("[0-9]+"))
        	{
        		IncidentType = dataElements[2].toString();	
        	}
        	
        	
        	if(dataElements.length >= 5 && dataElements[4].toString().matches("[0-9/]+") )
        	{
        		DateOccurred = dataElements[4].toString();
        	}
        	if(dataElements.length >= 6 && dataElements[5].toString().matches("[0-9-]+") )
        	{
        		TimeOccurred = dataElements[5].toString();
        	}
        	if(dataElements.length >= 7 && dataElements[6].toString().matches("[a-zA-Z]+") )
        	{
        		Disposition = dataElements[6].toString();
        	}
        	if(dataElements.length >= 4)
        	{
        		Location = dataElements[3].toString();
        		if(Location.length() >= 11)
        		{
        	if(Location.substring(Location.length()-11).trim().matches("[0-9/]+"))
        	{
        		DateOccurred = Location.substring(Location.length()-11);
        		Location = Location.substring(0, Location.length()-11);
        		
        		Disposition = dataElements[dataElements.length - 1].toString();
        		TimeOccurred = dataElements[dataElements.length - 2].toString();
        	}
        		}
        	}
        	
         // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
        	String date = DateOccurred+"  "+TimeOccurred;
        	String[] values =date.split("-");
        	date = values[0].toString();
        	
        	UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime=  dateToConvert( DateReported, "MM/dd/yyyy") ;
          recordData.OffenseDateTime=  dateToConvert( date, "MM/dd/yyyy  Hmm") ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
       DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void UniversityofWashingtonExtractionMethod()
    {
    	String UniversityName = "UniversityofWashington";
    	File input = new File("D:\\CrimeReportPDFS\\UniversityofWashingtonCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("60 Day Log") || line.contains("UWPD") || line.contains("Incident Date")
            		 || line.contains("Associated Case") || line.contains("And Time") || line.contains("Disposition")
            		 || line.split("\t").length < 4))
            	{
            		modifiedList.add(line.trim());
            		
            	}
            	
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
        
         for(int i=0;i<= modifiedList.toArray().length-1;i++ )
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = modifiedList.get(i);
        	String nextRecord="";
        	String[] dataElements = record.split("\t");
        	
        	IncidentType = dataElements[0].toString();
        	if(dataElements[1].toString().matches("[0-9-]+"))
        	{
        	ReportNum = dataElements[1].toString();
        	}
        	
        	for(int j=0;j<= dataElements.length - 1;j++)
        	{
        		if(dataElements[j].toString().matches("[0-9/: ]+") && dataElements[j].toString().contains("/"))
        		{
        			DateOccurred = dataElements[j].toString();
        			break;
        		}
        	}
        	DateReported = DateOccurred;
        	if(record.contains("HANDLED BY"))
        	{
        		Disposition = "HANDLED BY OFFICER";
        	}
        	if(record.contains("OTHER") || record.contains("AGENCY"))
        	{
        		Disposition = "OTHER AGENCY";
        	}
        	if(record.contains("HANDLED BY") || record.contains("OFFICER"))
        	{
        		Disposition = "HANDLED BY OFFICER";
        	}
        	if(record.contains("REPORT"))
        	{
        		Disposition = "REPORT";
        	}
        	if(record.contains("UNABLE TO") || record.contains("LOCATE"))
        	{
        		Disposition = "UNABLE TO LOCATE";
        	}
        	if(record.contains("WARNING"))
        	{
        		Disposition = "WARNING";
        	}
        	if(record.contains("CANCELLED"))
        	{
        		Disposition = "CANCELLED";
        	}
        	if(record.contains("ARREST"))
        	{
        		Disposition = "ARREST";
        	}
        	if(record.contains("CITATION"))
        	{
        		Disposition = "CITATION";
        	}
        	if(record.contains("BACK UP"))
        	{
        		Disposition = "BACK UP";
        	}
        	if(dataElements.length >= 4)
        	{
        		Location = dataElements[3].toString();
        	}
        	if(!ReportNum.isEmpty())
        	{
         // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
          UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime= null;
          recordData.OffenseDateTime= null;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
        	}
         }   
         pd.close();
      DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void UniversityofMinnesotaExtractionMethod()
    {
    	String UniversityName = "UniversityofMinnesota";
    	File input = new File("D:\\CrimeReportPDFS\\UniversityofMinnesotaCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Disposition")))
            	{
            		modifiedList.add(line.trim());
            		}
            	
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
        
         for(int i=0;i<= modifiedList.toArray().length-1;i++ )
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = modifiedList.get(i);
        	String nextRecord="";
        	String[] dataElements = record.split("\t");
        	
        	if(dataElements.length == 6)
        	{
        	ReportNum = dataElements[0].toString();
        	IncidentType = dataElements[1].toString();
        	DateOccurred = dataElements[2].toString();
        	DateReported = dataElements[3].toString();
        	Location = dataElements[4].toString();
        	Disposition = dataElements[5].toString();
        	}
        
         // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
          UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/d/yyyy") ;
          recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yyyy HHmm") ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
         DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void MITExtractionMethod()
    {
    	String UniversityName = "MIT";
    	File input = new File("D:\\CrimeReportPDFS\\MITCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Disposition") || line.trim().isEmpty() || line.trim().split(" ").length <10))
            	{
            		String[] elements =  line.trim().split(" ");
            		if(elements[0].toString().matches("[0-9-]+"))
            		{
            		modifiedList.add(line.trim());
            		}
            		}
            	
            }
     
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
        
         for(int i=0;i<= modifiedList.toArray().length-1;i++ )
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = modifiedList.get(i);
        	String nextRecord="";
        	String[] dataElements = record.split(" ");
        	
        	if(record.contains("CLOSED"))
        	{
        		Disposition = "CLOSED";
        	}
        	if(record.contains("OPEN"))
        	{
        		Disposition = "OPEN";
        	}
        	DateReported = dataElements[0].toString();
        	int dateOccurredIndex = 2;
        	for(int k=1;k<=dataElements.length-1;k++)
        	{
        		if(dataElements[k].toString().matches("[0-9-]+"))
        		{
        			DateOccurred = dataElements[k].toString();
        			dateOccurredIndex = k;
        			break;
        		}
        	}
        	for(int j=1;j<=dateOccurredIndex-1;j++)
        	{
        	IncidentType = IncidentType+ dataElements[j].toString();
        	}
        	if(dateOccurredIndex+2 <= dataElements.length-1 )
        	{
        	Location = dataElements[dateOccurredIndex+1].toString() + dataElements[dateOccurredIndex+2].toString();
        	}
        	Random ran = new Random();
			int randomNum = ran.nextInt(1000000000) + 1;
        	
           //// System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
          UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = "RN_"+randomNum;;
          recordData.ReportedDateTime=  dateToConvert( DateReported, "yyyy-MM-dd") ;
          recordData.OffenseDateTime=  dateToConvert(DateOccurred, "yyyy-MM-dd") ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
        DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void UniversityofSouthCarolinaExtractionMethod()
    {
    	String UniversityName = "UniversityofSouthCarolina";
    	File input = new File("D:\\CrimeReportPDFS\\UniversityofSouthCarolinaCrimeReport.pdf"); 
    	PDDocument pd;
    	try{
    		pd= PDDocument.load(input);
    		PDFTextStripper reader = new PDFTextStripper();
            reader.setSortByPosition(true);
            reader.setWordSeparator("\t");
          	String pageText = reader.getText(pd);
            String[] lines = pageText.split("\n");
            ArrayList<String> modifiedList = new ArrayList<String>();
            for(String line : lines)
            {
            	if(!(line.contains("Disposition Desc.") || line.trim().isEmpty() || line.trim().split("\t").length <8))
            	{
            		
            		modifiedList.add(line.trim());
            		}
            	
            }
            
         Iterator iterator = modifiedList.iterator();
         ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
        
         for(int i=0;i<= modifiedList.toArray().length-1;i++ )
         {
        	String ReportNum="";
        	String DateReported="";
        	String TimeReported="";
        	String Location="";
        	String DateOccurred = "";
        	String TimeOccurred = "";
        	String IncidentType = "";
        	String Disposition = "";
        	String record = modifiedList.get(i);
        	String nextRecord="";
        	String[] dataElements = record.split("\t");

        	IncidentType = dataElements[0].toString();
        	ReportNum = dataElements[1].toString();
        	DateReported = dataElements[2].toString();
        	int dateOccurredIndex = 7;
        	for(int j=3;j<=7;j++)
        	{
        		if(dataElements[j].toString().matches("[0-9/:AMPM ]+"))
        		{
        			dateOccurredIndex = j;
        			break;
        		}
        		else
        		{
        			Location =Location+dataElements[j].toString();
        			
        		}
        	}
        	DateOccurred = dataElements[dateOccurredIndex].toString();
        	if(dataElements[dateOccurredIndex+1].toString().matches("[0-9/:AMPM ]+"))
        	{
        		Disposition = dataElements[dateOccurredIndex+2].toString();
        	}
        	String[] elements = IncidentType.split(" ");
        	if(elements[elements.length-1].matches("[0-9]+"))
        	{
        		ReportNum = elements[elements.length-1].toString();
        		DateReported = dataElements[1].toString();
        		IncidentType = IncidentType.replace(ReportNum,"").toString();
        		
        	}
        	
           //// System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
        	DateReported = DateReported.replaceAll(" ", "");
        	String inputPatterndr;
        	if(DateReported.length() > 10)
        	{
        	 inputPatterndr = "MM/dd/yyyyhh:mm:ssa";
        	}
        	else{
        		inputPatterndr = "MM/dd/yyyy";	}
        	
        	DateOccurred = DateOccurred.replaceAll(" ", "");
        	String inputPatternoc;
        	if(DateOccurred.length() > 10)
        	{
        	 inputPatternoc = "MM/dd/yyyyhh:mm:ssa";
        	}
        	else{
        		inputPatternoc = "MM/dd/yyyy";}
        	
        	UniversityData recordData = new UniversityData();
          recordData.IncidentNumber = ReportNum;
          recordData.ReportedDateTime=  dateToConvert( DateReported, inputPatterndr) ;
          recordData.OffenseDateTime=  dateToConvert( DateOccurred, inputPatternoc) ;
          recordData.Location= Location;
          recordData.IncidentType= IncidentType;
          recordData.Disposition= Disposition;
          finalRecordsList.add(recordData);
         
         }   
         pd.close();
      DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    	}catch (IOException e) {
           e.printStackTrace();
    	}
    }
    public void PurdueUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "PurdueUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void PennStateCollegeExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "PennStateCollege";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);	
    }
    public void UniversityofArkansasExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofArkansas";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);	
    }
    public void NorthWesternUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "NorthWesternUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofDelawareExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofDelaware";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofVirginiaExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofVirginia";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void WashingtonUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "WashingtonUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    
    public void EmoryUnivesityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "EmoryUnivesity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofCaliforniaRiversideExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofCaliforniaRiverside";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void BostonUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "BostonUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void IllinoisInstituteofTechnologyExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "IllinoisInstituteofTechnology";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void LehighUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "LehighUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofNewMexicoExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofNewMexico";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofNebraskaExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofNebraska";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofMichiganExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofMichigan";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofIllinois_UCExtractionMethod()
    {
    	 String UniversityName = "UniversityofIllinois_UC";
 		File input = new File("D:\\CrimeReportPDFS\\UniversityofIllinois_UCCrimeReport.pdf"); 
 		PDDocument pd;
 		try{
 			pd= PDDocument.load(input);
 			PDFTextStripper reader = new PDFTextStripper();
 	        reader.setSortByPosition(true);
 	        reader.setWordSeparator("\t");
 	      	String pageText = reader.getText(pd);
 	        String[] lines = pageText.split("\n");
 	        ArrayList<String> modifiedList = new ArrayList<String>();
 	        for(String line : lines)
 	        {
 	        	if(!(line.contains("General Location") || line.split("\t").length < 8))
 	        	{
 	        		modifiedList.add(line.trim());
 	        	}
 	        	
 	        }
 	        
 	     Iterator iterator = modifiedList.iterator();
 	     ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
 	        
 	     while(iterator.hasNext())
 	     {
 	    	String ReportNum="";
 	    	String DateReported="";
 	    	String TimeReported="";
 	    	String Location="";
 	    	String DateOccurred = "";
 	    	String TimeOccurred = "";
 	    	String IncidentType = "";
 	    	String Disposition = "";
 	    	String record = iterator.next().toString();
 	    	
 	    	String[] dataElements = record.split("\t");
 	    	if(dataElements.length == 8)
 	    	{
 	    		ReportNum = dataElements[0].toString();
 	    		DateReported	= dataElements[1].toString();
 	    		TimeReported	= dataElements[2].toString();
 	    		DateOccurred = dataElements[3].toString();
 	    		TimeOccurred = dataElements[4].toString();
 	    		Location = dataElements[5].toString();
 	    		IncidentType = dataElements[6].toString();
 	    		Disposition = dataElements[7].toString();
 	    	}
 	    	
 	    	
 	    // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
 	   String dr = DateReported+" - "+TimeReported;
 	   String oc = DateOccurred+" - "+TimeOccurred;
 	    	
 	    	UniversityData recordData = new UniversityData();
 	      recordData.IncidentNumber = ReportNum;
 	      recordData.ReportedDateTime=  dateToConvert( dr,"MM/dd/yyyy - H:mm") ;
 	      recordData.OffenseDateTime=  dateToConvert( oc,"MM/dd/yyyy - H:mm") ;
 	      recordData.Location= Location;
 	      recordData.IncidentType= IncidentType;
 	      recordData.Disposition= Disposition;
 	      finalRecordsList.add(recordData);
 	     
 	     }   
 	    pd.close();
 	    DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
 		}catch (IOException e) {
 	       e.printStackTrace();
 		}
    }
    public void YaleUniversityExtractionMethod()
    {
    	String UniversityName = "YaleUniversity";
		File input = new File("D:\\CrimeReportPDFS\\YaleUniversityCrimeReport.pdf"); 
		PDDocument pd;
		try{
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	      	String pageText = reader.getText(pd);
	        String[] lines = pageText.split("\n");
	        ArrayList<String> modifiedList = new ArrayList<String>();
	        
	        for(String line : lines)
	        {
	        	if(!(line.contains("Type of Incident") || line.split("\t").length < 6))
	        	{
	        		modifiedList.add(line.trim());
	        	}
	        	
	        }
	        
	     Iterator iterator = modifiedList.iterator();
	     ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	        
	     while(iterator.hasNext())
	     {
	    	String ReportNum="";
	    	String DateReported="";
	    	String TimeReported="";
	    	String Location="";
	    	String DateOccurred = "";
	    	String TimeOccurred = "";
	    	String IncidentType = "";
	    	String Disposition = "";
	    	String record = iterator.next().toString();
	    	
	    	String[] dataElements = record.split("\t");
	    
	    	if(dataElements.length == 6)
	    	{
	    		DateReported = dataElements[0].toString();
	    		IncidentType	= dataElements[1].toString();
	    		DateOccurred	= dataElements[2].toString();
	    		Location = dataElements[3].toString();
	    		Disposition = dataElements[4].toString();
	    		ReportNum = dataElements[5].toString();	
	    	}    	
	    // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
	      UniversityData recordData = new UniversityData();
	      recordData.IncidentNumber = ReportNum;
	      recordData.ReportedDateTime=  dateToConvert( DateReported.replaceAll(" ",""),"MM/dd/yyH:mm") ;
	      recordData.OffenseDateTime=  dateToConvert( DateOccurred.replaceAll(" ",""),"MM/dd/yyH:mm") ;
	      recordData.Location= Location;
	      recordData.IncidentType= IncidentType;
	      recordData.Disposition= Disposition;
	      finalRecordsList.add(recordData);
	     
	     }   
	     pd.close();
	   DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
		}catch (IOException e) {
	       e.printStackTrace();
		}
    }    
    public void OregonStateUniversityExtractionMethod()
    {
    	  String UniversityName = "OregonStateUniversity";
  		File input = new File("D:\\CrimeReportPDFS\\OregonStateUniversityCrimeReport.pdf"); 
  		PDDocument pd;
  		try{
  			pd= PDDocument.load(input);
  			PDFTextStripper reader = new PDFTextStripper();
  	        reader.setSortByPosition(true);
  	        reader.setWordSeparator("\t");
  	      	String pageText = reader.getText(pd);
  	        String[] lines = pageText.split("\n");
  	        ArrayList<String> modifiedList = new ArrayList<String>();
  	        for(String line : lines)
  	        {
  	        	if(!(line.contains("Case #") || line.split("\t").length < 6))
  	        	{
  	        		modifiedList.add(line.trim());
  	        	}
  	        	
  	        }
  	        
  	     Iterator iterator = modifiedList.iterator();
  	     ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
  	        
  	     while(iterator.hasNext())
  	     {
  	    	String ReportNum="";
  	    	String DateReported="";
  	    	String TimeReported="";
  	    	String Location="";
  	    	String DateOccurred = "";
  	    	String TimeOccurred = "";
  	    	String IncidentType = "";
  	    	String Disposition = "";
  	    	String record = iterator.next().toString();
  	    	
  	    	String[] dataElements = record.split("\t");
  	    	if(dataElements.length == 6)
  	    	{
  	    		IncidentType = dataElements[0].toString();
  	    		ReportNum	= dataElements[1].toString();
  	    		DateReported	= dataElements[2].toString();
  	    		DateOccurred = dataElements[3].toString();
  	    		Location = dataElements[4].toString();
  	    		Disposition = dataElements[5].toString();
  	    		
  	    	}
  	    	
  	    	
  	   // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
  	    	
  	    UniversityData recordData = new UniversityData();
  	      recordData.IncidentNumber = ReportNum;
  	      recordData.ReportedDateTime=  dateToConvert( DateReported,"MM/dd/yy") ;
  	      recordData.OffenseDateTime=  dateToConvert( DateOccurred,"MM/dd/yy Hmm") ; 
	    	
  	      recordData.Location= Location;
  	      recordData.IncidentType= IncidentType;
  	      recordData.Disposition= Disposition;
  	      finalRecordsList.add(recordData);
  	     
  	     }   
  	   pd.close();
  	   DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
  		}catch (IOException e) {
  	       e.printStackTrace();
  		}
    }
    public void UniversityofTennesseeExtractionMethod()
    {

	    String UniversityName = "UniversityofTennessee";
		File input = new File("D:\\CrimeReportPDFS\\UniversityofTennesseeCrimeReport.pdf"); 
		PDDocument pd;
		try{
			pd= PDDocument.load(input);
			PDFTextStripper reader = new PDFTextStripper();
	        reader.setSortByPosition(true);
	        reader.setWordSeparator("\t");
	      	String pageText = reader.getText(pd);
	        String[] lines = pageText.split("\n");
	        ArrayList<String> modifiedList = new ArrayList<String>();
	        for(String line : lines)
	        {
	        	if(!(line.contains("Incident #") || line.split("\t").length < 6 ))
	        	{
	        		modifiedList.add(line.trim());
	        	}
	        	
	        }
	        
	     Iterator iterator = modifiedList.iterator();
	     ArrayList<UniversityData> finalRecordsList = new ArrayList<UniversityData>();
	        
	     while(iterator.hasNext())
	     {
	    	String ReportNum="";
	    	String DateReported="";
	    	String TimeReported="";
	    	String Location="";
	    	String DateOccurred = "";
	    	String TimeOccurred = "";
	    	String IncidentType = "";
	    	String Disposition = "";
	    	String record = iterator.next().toString();
	    	
	    	String[] dataElements = record.split("\t");
	    	if(dataElements.length >= 6)
	    	{
	    		IncidentType = dataElements[0].toString();
	    		DateReported	= dataElements[1].toString();
	    		DateOccurred	= dataElements[2].toString();
	    		ReportNum = dataElements[3].toString();
	    		Location = dataElements[4].toString();
	    		Disposition = dataElements[5].toString();
	    		
	    	}
	    	
	    	
	   // // System.out.println(ReportNum+"   "+IncidentType+"     "+DateReported+"   "+Location+"   "+DateOccurred+"   "+Disposition);
	      UniversityData recordData = new UniversityData();
	      recordData.IncidentNumber = ReportNum;
	      if(DateReported.replaceAll(" ", "").length() >= 9)
	      recordData.ReportedDateTime=  dateToConvert( DateReported.replaceAll(" ", "").substring(0, 8),"MM/dd/yy") ;
	      if(DateOccurred.replaceAll(" ", "").length() >= 9)
	      recordData.OffenseDateTime=  dateToConvert( DateOccurred.replaceAll(" ", "").substring(0, 8),"MM/dd/yy") ;

	      recordData.Location= Location;
	      recordData.IncidentType= IncidentType;
	      recordData.Disposition= Disposition;
	      finalRecordsList.add(recordData);
	     
	     }   
	     pd.close();
	  DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
		}catch (IOException e) {
	       e.printStackTrace();
		}
    }
    public void CarnegieMellonUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "CarnegieMellonUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }

    public void StateUniversityofNewYorkatBuffaloExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "StateUniversityofNewYorkatBuffalo";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void TempleUnivesityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "TempleUnivesity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void NewYorkUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "NewYorkUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void CornellUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "CornellUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofChicagoExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofChicago";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofKentuckyExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofKentucky";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    
    public void UniversityofFloridaExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofFlorida";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofNorthCarolinaatChapelHillExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofNorthCarolinaatChapelHill";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofArizonaExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofArizona";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    
    public void UniversityofSouthFloridaExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofSouthFlorida";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void OhioStateUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "OhioStateUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofMarylandCollegeParkExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofMarylandCollegePark";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void UniversityofCincinnatiExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "UniversityofCincinnati";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void AuburnUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "AuburnUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    public void RiceUniversityExtractionMethod(ArrayList<UniversityData> finalRecordsList)
    {
    	String UniversityName = "RiceUniversity";
    	DBConn.InsertDatatoDB(UniversityName,finalRecordsList);
    }
    
    public Date dateToConvert(String date,String inputPattern) {
    	Date dateReturn= null;
        try {
          

            DateFormat sdfSource = new SimpleDateFormat(inputPattern);
            Date d =  sdfSource.parse(date);
           
            SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String strDate = sdfDestination.format(d);
         //   // System.out.println(strDate);
             dateReturn =  sdfDestination.parse(strDate);
          //  // System.out.println(dateReturn);

        } catch (ParseException pe) {
         //   // System.out.println("Parse Exception : " + pe);
            
        }
        return dateReturn;
        }  
    	
}
