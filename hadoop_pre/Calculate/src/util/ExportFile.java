package util;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import data.FireDepartment;
import data.IllegalConstruction;
import data.NarrowRoadway;
import data.SQLServer;
import data.Village;

public class ExportFile {
	
	SQLServer sqlServer = new SQLServer();
	
	String[] sections={"中正區","萬華區","大同區","中山區","松山區","大安區"
			,"信義區","內湖區","南港區","士林區","北投區","文山區"};
	
	
	FileWriter fWriter ;
	BufferedWriter bWriter;
	
	
	public ExportFile(String inputPath) throws IOException{
		fWriter = new FileWriter(inputPath,true);
		bWriter = new BufferedWriter(fWriter);
	}
	
	
	public void writeVillage(String path) throws IOException{
		FileWriter fWriter = new FileWriter(path,true);
		BufferedWriter bWriter = new BufferedWriter(fWriter);
		int i=0;
	
		for(String section:sections){
			LinkedList<Village> villages = sqlServer.select_Village(section);
			for(Village v:villages){
				bWriter.write("village"+String.format("%05d", i)
						+" "+section
						+" "+v.getlats().toString().replaceAll(" ", "")
						+" "+v.getlngs().toString().replaceAll(" ", ""));
				bWriter.newLine();
				i++;
			}
		}
		
		bWriter.flush();
		bWriter.close();
		fWriter.close();
		
	}
	
	
	public void writeIllegalConstruction() throws IOException{
		
		int i=0;
		for(String section:sections){
			LinkedList<IllegalConstruction> list = sqlServer.select_IllegalConstruction(section);
			for(IllegalConstruction ill:list){
				bWriter.write("illegal"+String.format("%05d", i)
						+" "+section
						+" "+ill.getLat()
						+" "+ill.getLng());
				bWriter.newLine();
				i++;
			}
		}
	}
	
	public void writeFireDepartment() throws IOException{
		
		int i=0;
		for(String section:sections){
			LinkedList<FireDepartment> list = sqlServer.select_FireDepartment(section);
			for(FireDepartment f:list){
				bWriter.write("firedepartment"+String.format("%05d", i)
						+" "+section
						+" "+f.getLat()
						+" "+f.getLng());
				bWriter.newLine();
				i++;
			}
		}
	}
	
	public void writeNarrowRoadway() throws IOException{
		
		int i=0;
		for(String section:sections){
			LinkedList<NarrowRoadway> list = sqlServer.select_NarrowRoadway(section);
			for(NarrowRoadway n:list){
				bWriter.write("narrow"+String.format("%05d", i)
						+" "+section
						+" "+n.getLat()
						+" "+n.getLng());
				bWriter.newLine();
				i++;
			}
		}
	}
	
	public void writeInput()throws IOException{
		
		
		writeIllegalConstruction();
		writeFireDepartment();
		writeNarrowRoadway();
		
		
		bWriter.flush();
		bWriter.close();
		fWriter.close();
		
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		ExportFile exportFile = new ExportFile("input.txt");
		//exportFile.writeVillage("village.txt");
		exportFile.writeInput();;
	}

}
