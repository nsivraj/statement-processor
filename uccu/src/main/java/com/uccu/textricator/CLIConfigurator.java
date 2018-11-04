package com.uccu.textricator;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.uccu.textricator.Constants;

final class CLIConfigurator {

    static final char ROOT_PATH = 'r';
    static final String ROOT_PATH_LONG_OPTION = "root-path";
    static final char FILTER = 'f';
    static final char VERBOSE = 'v';

    private static final Options CLI_OPTIONS = new Options();
    static {
        CLI_OPTIONS.addOption(String.valueOf(FILTER), true, "Filter to include files");
        CLI_OPTIONS.addRequiredOption(String.valueOf(ROOT_PATH), ROOT_PATH_LONG_OPTION, true, "Root path");
        CLI_OPTIONS.addOption(String.valueOf(VERBOSE), false, "Verbose output");
    }

    private CLIConfigurator() {
        throw new IllegalStateException(Constants.INSTANTIATION_NOT_ALLOWED);
    }

    static Optional<CLIConfig> configure(final String... args) {
        assert !Objects.isNull(args);

        Optional<CLIConfig> result = Optional.empty();

        try {
            final CommandLineParser parser = new DefaultParser();
            final CommandLine cmd = parser.parse(CLI_OPTIONS, args);

            if (cmd.hasOption(ROOT_PATH) || cmd.hasOption(ROOT_PATH_LONG_OPTION)) {
                result = Optional.of(new CLIConfig(cmd));
            } else {
                printUsage();
            }
        } catch (ParseException e) {
            printUsage();
        }

        return result;
    }

    private static void printUsage() {
        final HelpFormatter help = new HelpFormatter();
        help.printHelp("Running Iterate Directory", CLI_OPTIONS);
    }
}
