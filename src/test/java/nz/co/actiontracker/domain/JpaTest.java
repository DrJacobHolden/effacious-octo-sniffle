package nz.co.actiontracker.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nz.co.actiontracker.EMFactorySingleton;

/**
 * Provided by Ian Warren, a useful class
 * to help set up JPA tests. The comments are all
 * gone because I typed it out manually to try and help
 * understand what it was doing.
 */
public abstract class JpaTest {
	
	private static Logger _logger = LoggerFactory.getLogger(JpaTest.class);
	
	private static Connection _jdbcConnection = null;
	
	private static EntityManagerFactory _factory = null;
	
	protected EntityManager _entityManager = null;
	
	@BeforeClass
	public static void initialiseDatabase() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		_jdbcConnection = DriverManager.getConnection(
				"jdbc:h2:~/test;mv_store=false","sa", "sa");
		clearDatabase(true);
		_factory = EMFactorySingleton.getInstance();
	}
	
	@AfterClass
	public static void releaseEntityManager() throws SQLException {
		_jdbcConnection.close();
	}
	
	@Before
	public void clearDatabase() throws SQLException {
		clearDatabase(false);
		_entityManager = _factory.createEntityManager();
	}
	
	@After
	public void closeEntityManager() {
		_entityManager.close();
	}
	
	protected static void clearDatabase(boolean dropTables) throws SQLException {
		Statement s = _jdbcConnection.createStatement();
		s.execute("SET REFERENTIAL_INTEGRITY FALSE");
		
		Set<String> tables = new HashSet<String>();
		ResultSet rs = s.executeQuery("select table_name "
				+ "from INFORMATION_SCHEMA.tables "
				+ "where table_type='TABLE' and table_schema='PUBLIC'");
		
		while (rs.next()) {
			tables.add(rs.getString(1));
		}
		rs.close();
		for(String table : tables) {
			_logger.debug("Deleting content from " + table);
			s.executeUpdate("DELETE FROM " + table);
			if (dropTables) {
				s.executeUpdate("DROP TABLE " + table);
			}
		}
		
		s.execute("SET REFERENTIAL_INTEGRITY TRUE");
		s.close();
	}
}
