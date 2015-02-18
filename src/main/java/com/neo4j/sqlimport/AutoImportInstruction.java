package com.neo4j.sqlimport;

import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;

import java.io.BufferedReader;
import java.io.FileReader;


public class AutoImportInstruction implements  Command {

	private final String fileName;

	public AutoImportInstruction(String fileName) {
		this.fileName = fileName;;
	}

	public void execute(BatchInserter neo, BatchInserterIndexProvider indexService) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			// replace ,NULL, with ,'',
			int i = 0;
			String line = SQLImporter.getNextInsertLine(br);
			while (line != null) {
				if (line.contains("NULL")) {
					line = line.replaceAll("NULL", "''");
					line = line.replaceAll("''''", "'");
				}
				String tableName = SQLImporter.getTableNameFromInsertStatement(line);
				long subrefId = SQLImporter.cerateOrFindSubrefNode(tableName, neo);
				Field[] fields = SQLImporter.getFieldsForLine(line);
				SQLImporter.createNode(fields, line, neo, subrefId);
				line = SQLImporter.getNextInsertLine(br);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
