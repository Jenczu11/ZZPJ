package pl.lodz.p.it.mercedes;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.io.IOException;

@TestConfiguration
public class TestMongoConfiguration extends AbstractMongoClientConfiguration {

    private static MongodExecutable mongodExecutable;

    @Override
    protected String getDatabaseName() {
        return "test";
    }

    static void setup() throws IOException {
        String ip = "localhost";
        int port = 27017;

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(ip, port, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
    }

    static void clean() {
        mongodExecutable.stop();
    }
}