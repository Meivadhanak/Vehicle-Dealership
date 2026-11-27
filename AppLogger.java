import java.io.IOException;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Configures and provides loggers for the car dealership application.
 */
public final class AppLogger {
    private static final Handler FILE_HANDLER = createFileHandler();

    /**
     * Prevents construction of this logging utility.
     */
    private AppLogger() {
    }

    /**
     * Creates and registers the append-mode file handler without changing the
     * root logger's configured threshold.
     *
     * @return configured handler for {@code cardealerApp.log}
     */
    private static Handler createFileHandler() {
        Logger rootLogger = Logger.getLogger("");
        try {
            Handler handler = new FileHandler("cardealerApp.log", true);
            handler.setLevel(Level.INFO);
            handler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(handler);
            rootLogger.info("AppLogger.createFileHandler called");
            return handler;
        } catch (IOException | SecurityException exception) {
            rootLogger.log(Level.SEVERE, "Unable to initialize cardealerApp.log.", exception);
            throw new ExceptionInInitializerError(exception);
        }
    }

    /**
     * Returns an INFO-enabled logger for the specified class.
     *
     * @param owner class whose logger is requested
     * @return the class-specific logger
     * @throws NullPointerException if {@code owner} is {@code null}
     */
    public static Logger getLogger(Class<?> owner) {
        Objects.requireNonNull(owner, "owner");
        Logger logger = Logger.getLogger(owner.getName());
        logger.setLevel(Level.INFO);
        logger.info("AppLogger.getLogger called for " + owner.getSimpleName());
        return logger;
    }
}
