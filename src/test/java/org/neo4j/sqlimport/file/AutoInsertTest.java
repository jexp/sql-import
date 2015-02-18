package org.neo4j.sqlimport.file;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.neo4j.sqlimport.Relationships;
import com.neo4j.sqlimport.SQLImporter;

public class AutoInsertTest {

	private static final String SQL_FILE = "example2.sql";
	private static final String DB_DIR = "target/db";
	private SQLImporter importer;

	@Before
	public void setUp() {
		importer = new SQLImporter(DB_DIR);
		importer.deleteDB();
		
	}

	@Test
	@Ignore
	public void importAll() {
		//import the data
		importer.autoImport(SQL_FILE);
		
		//make the links
		importer.autoLink("Comment","BOOK_ID", "Book", "BOOK_ID", "talks_about");
		importer.autoLink("Author_Book","Author","AUTHOR_ID", "Book", "BOOK_ID", "author_of");
		importer.startLinking();
		//verify the results
		GraphDatabaseService neo = new GraphDatabaseFactory().newEmbeddedDatabase(DB_DIR);
		try (Transaction tx = neo.beginTx()) {
			Node referenceNode = neo.getNodeById(0);
			Iterator<Relationship> subrefs = referenceNode.getRelationships().iterator();
			assertTrue(subrefs.hasNext());
			Node pippi = neo.getNodeById(2);
			assertTrue(pippi.hasProperty("BOOK_ID"));
			assertTrue(pippi.hasRelationship(DynamicRelationshipType.withName("talks_about")));
			assertTrue(pippi.hasRelationship(DynamicRelationshipType.withName("author_of")));
			assertTrue(pippi.hasRelationship(Relationships.IS_A));
			Assert.assertEquals("2009-05-13", pippi.getProperty("CREATED_DATE_TIME"));
			Node test = neo.getNodeById(10);
			Assert.assertEquals(" 2009-06-05", test.getProperty("DATE"));
			Assert.assertEquals("some_proc('2009-06-05','RRRR-MM-DD')", test.getProperty("PROC"));

			tx.success();
		}
		neo.shutdown();
	}

	@After
	public void shutdown() throws Exception {
	}
}
