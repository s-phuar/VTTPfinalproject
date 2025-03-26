package VTTPproject.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.convert.CouchbaseCustomConversions;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import com.couchbase.client.java.env.ClusterEnvironment;

import java.nio.file.Paths;
import java.util.Collections;

@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfig extends AbstractCouchbaseConfiguration{
    @Value("${spring.couchbase.connection.string}")
    private String connectionString;

    @Value("${spring.couchbase.username}")
    private String username;

    @Value("${spring.couchbase.password}")
    private String password;

    @Value("${spring.data.couchbase.bucket-name}")
    private String bucketName;

    @Override
    public String getBucketName() {
        return bucketName;
    }

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    protected void configureEnvironment(ClusterEnvironment.Builder builder) {
        builder.securityConfig(security -> {
            security.enableTls(true)
                   .trustCertificate(Paths.get("/app/couchbase-cert.pem")); // Path to cert in container
        });
    }

    @Bean
    @Primary
    public CouchbaseCustomConversions customConversions() {
        return new CouchbaseCustomConversions(Collections.emptyList());
    }

}
