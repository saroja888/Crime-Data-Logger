package Crawler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class DBConnection {
	public  void InsertDatatoDB(String UniversityName,ArrayList<UniversityData> finalRecordsList)
	{
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		   
		    Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/project?autoReconnect=true&useSSL=false","root","root");
		    Statement statement = conn.createStatement();
		    ResultSet rs = statement.executeQuery("select count(*) as count from project.universities");
		    rs.next();
		    
		   int preCount = rs.getInt("count");
		    String SQL = "INSERT IGNORE INTO project.universities  "+ 
		             "VALUES(?, ?, ?, ?, ?, ?, ?)";
		   
		    PreparedStatement prestat = conn.prepareStatement(SQL);
		    conn.setAutoCommit(false);
		    
		    Iterator iterator = finalRecordsList.iterator();
		    while(iterator.hasNext())
		    {
		    	for(int i=0;i<=100;i++)
		    	{
		    		if(iterator.hasNext())
		    		{
		    	
		    		UniversityData record = (UniversityData) iterator.next();
		    		
		    		
		    		prestat.setString( 1, record.IncidentNumber );
		    		
		    		
		    		if(record.ReportedDateTime != null)
		    		{
		    			java.sql.Date drDate = new java.sql.Date(record.ReportedDateTime.getTime());
		    			prestat.setDate( 2, drDate );
		    		}
		    		else
		    		prestat.setNull(2,java.sql.Types.DATE); 
		    		
		    		
		    		if(record.OffenseDateTime != null)
		    		{
		    			java.sql.Date ocDate = new java.sql.Date(record.OffenseDateTime.getTime());
		    			prestat.setDate( 3, ocDate );
		    		}
		    		else
		    		prestat.setNull(3,java.sql.Types.DATE); 
		    		
		    		
		    		prestat.setString( 4, record.Location );
		    		prestat.setString( 5, record.IncidentType );
		    		prestat.setString( 6, record.Disposition );
		    		prestat.setString( 7, UniversityName);
		    		prestat.addBatch();
		    		}
		    	}
		    	int[] count = prestat.executeBatch();
		    	
		    	conn.commit();
		    }
		    ResultSet rs1 = statement.executeQuery("select count(*) as count from project.universities;");
		    rs1.next();
		  int  postCount = rs1.getInt("count");
		    
		   int NumOfEntries = postCount-preCount;
		    System.out.println(UniversityName+"   "+"count-"+NumOfEntries);
             conn.close();
		    statement.close();
		   
		} catch (ClassNotFoundException | SQLException  | NullPointerException e) {
		//	System.out.println(e);
		}
	}
	 

}
