package com.uccu.textricator;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.lang3.StringUtils;

public final class CLIConfig {
    private final String root;
    private final String filter;
    private final boolean verbose;

    CLIConfig(final CommandLine commandLine) {
        assert !Objects.isNull(commandLine);

        this.root = Objects.isNull(commandLine.getOptionValue(CLIConfigurator.ROOT_PATH)) ? commandLine.getOptionValue(CLIConfigurator.ROOT_PATH)
                : commandLine.getOptionValue(CLIConfigurator.ROOT_PATH_LONG_OPTION);

        this.filter = commandLine.getOptionValue(CLIConfigurator.FILTER);
        this.verbose = commandLine.hasOption(CLIConfigurator.VERBOSE);
    }

    public Optional<String> getRoot() {
        return StringUtils.isEmpty(this.root) ? Optional.empty() : Optional.of(this.root);
    }

    public Optional<String> getFilter() {
        return StringUtils.isEmpty(this.filter) ? Optional.empty() : Optional.of(this.filter);
    }

    public boolean isVerbose() {
        return this.verbose;
    }
}
