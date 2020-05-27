package pl.lodz.p.it.mercedes;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

public class TestSuiteExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws IOException {
        if (!started) {
            started = true;
            TestMongoConfiguration.setup();
            extensionContext.getRoot().getStore(GLOBAL).put("TestSuiteExtension", this);
        }
    }

    @Override
    public void close() {
        TestMongoConfiguration.clean();
    }
}
