package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import data.SQLServer;

public class ExportDataForHadoop {
	
	private SQLServer sqlServer = new SQLServer();
	protected final static String WRITE_PATH="data.txt" ;
	FileWriter fw ;
	
	public ExportDataForHadoop(){
		try {
			fw = new FileWriter(WRITE_PATH);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// fw.flush();
		
		//fw.close();


	}
	

}
