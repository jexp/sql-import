package com.neo4j.sqlimport;

import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;

public interface Command {

	void execute(BatchInserter neo, BatchInserterIndexProvider indexService);
}
