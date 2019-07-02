import com.datastax.driver.core.Session;

/*
Repository to handle the Cassandra schema
*/

public class KeyspaceRepository {
    private Session session;

    public KeyspaceRepository(Session session){
        System.out.println("KeyspaceRepository - init");
        this.session = session;
        System.out.println("KeyspaceRepository - end");
    }

    /*
    Method used to create any keyspace - schema
    @param keyspaceName -> name of the schema
    @param replicationStrategy -> replication strategy
    @param numberOfReplicas -> number of replicas
     */

    public void createKeyspace(String keyspaceName, String replicationStrategy, int numberOfReplicas){
        System.out.println("createKeyspace - init");
        StringBuilder sb = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                .append(keyspaceName)
                .append(" WITH replication = {").append("'class':'")
                .append(replicationStrategy)
                .append("','replication_factor':")
                .append(numberOfReplicas).append("};");

        final String query = sb.toString();
        session.execute(query);
        System.out.println("createKeyspace - end");
    }

    public void useKeyspace(String keyspace){
        System.out.println("useKeyspace - init");
        session.execute("USE " + keyspace);
        System.out.println("useKeyspace - end");
    }

    /* Method used to delete the specified schema.
    It results in the immediatem irreversable removal of the keyspace,
    including allt ables and data contained in the keyspace

    @param keyspaceName -> name of the keyspace to delete
     */

    public void deleteKeyspace(String keyspaceName){
        System.out.println("deleteKeyspace - init");
        StringBuilder sb = new StringBuilder("DROP KEYSPACE ").append(keyspaceName);

        final String query = sb.toString();
        session.execute(query);
        System.out.println("deleteKeyspace - end");
    }
}
