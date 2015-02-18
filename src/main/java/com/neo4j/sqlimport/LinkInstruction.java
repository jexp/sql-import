package com.neo4j.sqlimport;

import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserter;

public abstract class LinkInstruction {


	public abstract void execute(BatchInserter neo,
	        BatchInserterIndexProvider indexService);
	
	
}
