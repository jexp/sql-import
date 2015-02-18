package org.neo4j.sqlimport.file;

import org.neo4j.unsafe.batchinsert.BatchInserterIndexProvider;
import org.neo4j.unsafe.batchinsert.BatchInserter;

public interface FileImportCommand {

	void execute(BatchInserter neo, BatchInserterIndexProvider indexService, int stepSize);
}
