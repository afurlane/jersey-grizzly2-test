package org.example.infrastructure.transactional;

import com.arjuna.ats.jdbc.TransactionalDriver;
import com.arjuna.ats.jdbc.common.jdbcPropertyManager;

import javax.naming.Context;
import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XAConnectionBuilder;
import javax.sql.XADataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class XADataSourceWrapper implements XADataSource, DataSource {
    private final XADataSource theXADataSource;
    private final TransactionalDriver transactionalDriver = new TransactionalDriver();
    private final String name;

    /**
     * Create a wrapper around the provided XADataSource implementation,
     * which should be registered in tomcat's global JNDI with the specified name.
     * Note: the registration is not done here, it's someone elses problem.
     * See TransactionalResourceFactory for example usage.
     *
     * @param name          should be the fully qualifed JNDI name of the XADataSource, in
     *                      tomcat's global JNDI, not a webapp specific JNDI context.
     * @param theDataSource
     */
    public XADataSourceWrapper(String name, XADataSource theDataSource) {
        theXADataSource = theDataSource;
        this.name = name;
    }

    /**
     * Obtain a direct reference to the wrapped object. This is not
     * recommended but may be necessary to e.g. call vendor specific methods.
     *
     * @return
     */
    public XADataSource getUnwrappedXADataSource() {
        return theXADataSource;
    }

    ///////////////////////

    // Implementation of the DataSource API is done by reusing the arjuna
    // TransactionalDriver. Its already got all the smarts for checking tx
    // context, enlisting resources etc so we just delegate to it.
    // All we need is some fudging to make the JNDI name stuff behave.

    /**
     * Obtain a connection to the database.
     * Note: Pooling behaviour depends on the vendor's underlying XADataSource implementation.
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        String url = TransactionalDriver.arjunaDriver + name;
        // although we are not setting any properties, the driver will barf if we pass 'null'.
        Properties properties = new Properties();
        return getTransactionalConnection(url, properties);
    }

    /**
     * Obtain a connection to the database using the supplied authentication credentials.
     *
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public Connection getConnection(String username, String password) throws SQLException {
        String url = TransactionalDriver.arjunaDriver + name;
        Properties properties = new Properties();
        properties.setProperty(TransactionalDriver.userName, username);
        properties.setProperty(TransactionalDriver.password, password);
        return getTransactionalConnection(url, properties);
    }

    /*
     * This is where most of the tomcat specific weirdness resides. You probably
     * want to subclass and override this method for reuse in env other than tomcat.
     */
    protected Connection getTransactionalConnection(String url, Properties properties) throws SQLException {

        // For ref, the url the TransactionalDriver expects is the arjuna driver's
        // special prefix followed by a JNDI name.
        // via ConnectionImple the IndirectRecoverableConnection.createDataSource method
        // attempts to look it up in JNDI. There are two problems with this:

        //  First problem,
        // it always calls InitialContext(env), never InitalContext().
        // This we work around by copying into the arjuna config, the system
        // properties it needs to populate the env:

        // caution: ensure the tx lifecycle listener is configured in tomcat or there will be a
        // possible race here, as recovery needs these properties too and may start first
        jdbcPropertyManager.getJDBCEnvironmentBean().getJndiProperties().put("Context.INITIAL_CONTEXT_FACTORY", System.getProperty(Context.INITIAL_CONTEXT_FACTORY));
        jdbcPropertyManager.getJDBCEnvironmentBean().getJndiProperties().put("Context.URL_PKG_PREFIXES", System.getProperty(Context.URL_PKG_PREFIXES));

        // Second problem: this method has almost certainly been called by a webapp,
        // which has its own InitialContext. Whilst the datasource is in there, we
        // can't be certain it's under the same name as its global name. We also
        // don't want any hassle with the webapp classloader, which may go away
        // whilst recovery is still active. Hence we need to temporarily set things
        // such that we use the server's global InitialContext for the lookup
        // instead of the webapp one. Tomcat figures out the InitialContext based
        // on classloader, so we fool it by changing the Thread context from the
        // webapps classloader to its parent (the server's classloader):
        ClassLoader webappClassLoader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(webappClassLoader.getParent());
        Connection connection;
        try {
            connection = transactionalDriver.connect(url, properties);
        } finally {
            Thread.currentThread().setContextClassLoader(webappClassLoader);
        }

        return connection;
    }

    ///////////////////////

    // Implementation of XADataSource API is just a straightforward wrap/delegate.
    // Note that some of these methods also appear in the DataSource API.
    // We don't really care, it's the underlying implementations problem
    // to disambiguate them if required.

    public boolean isWrapperFor(Class<?> iface) {
        return iface.isAssignableFrom(XADataSource.class);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return (T)getUnwrappedXADataSource();
        } else {
            throw new SQLException("Not a wrapper for " + iface.getCanonicalName());
        }
    }

    public XAConnection getXAConnection() throws SQLException {
        return theXADataSource.getXAConnection();
    }

    public XAConnection getXAConnection(String user, String password) throws SQLException {
        return theXADataSource.getXAConnection(user, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return theXADataSource.getLogWriter();
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        theXADataSource.setLogWriter(out);
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        theXADataSource.setLoginTimeout(seconds);
    }

    public int getLoginTimeout() throws SQLException {
        return theXADataSource.getLoginTimeout();
    }

    @Override
    public XAConnectionBuilder createXAConnectionBuilder() throws SQLException {
        return XADataSource.super.createXAConnectionBuilder();
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return DataSource.super.createConnectionBuilder();
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }

    @Override
    public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return XADataSource.super.createShardingKeyBuilder();
    }
}