/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.preferences;

import static java.util.stream.Collectors.joining;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.rf.ide.core.execution.server.AgentConnectionServer;
import org.rf.ide.core.executor.PythonInstallationDirectoryFinder;
import org.rf.ide.core.executor.PythonInstallationDirectoryFinder.PythonInstallationDirectory;
import org.robotframework.ide.eclipse.main.plugin.RedPlugin;
import org.robotframework.ide.eclipse.main.plugin.RedPreferences;
import org.robotframework.ide.eclipse.main.plugin.RedPreferences.CellCommitBehavior;
import org.robotframework.ide.eclipse.main.plugin.RedPreferences.CellWrappingStrategy;
import org.robotframework.ide.eclipse.main.plugin.RedPreferences.IssuesStrategy;
import org.robotframework.ide.eclipse.main.plugin.RedPreferences.SeparatorsMode;
import org.robotframework.ide.eclipse.main.plugin.model.RobotFileInternalElement.ElementOpenMode;
import org.robotframework.ide.eclipse.main.plugin.project.build.RobotTask.Priority;
import org.robotframework.ide.eclipse.main.plugin.project.build.causes.ProblemCategory;

import com.google.common.annotations.VisibleForTesting;

public class RedPreferencesInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        initializeDefaultPreferences(DefaultScope.INSTANCE.getNode(RedPlugin.PLUGIN_ID));
    }

    @VisibleForTesting
    void initializeDefaultPreferences(final IEclipsePreferences preferences) {
        initializeFrameworkPreferences(preferences);
        initializeEditorPreferences(preferences);
        initializeSourceFoldingPreferences(preferences);
        initializeSourceEditorAssistantPreferences(preferences);
        initializeSyntaxColoringPreferences(preferences);
        initializeAutodiscoveringPreferences(preferences);
        initializeProblemSeverityPreferences(preferences);
        initializeDefaultLaunchConfigurationPreferences(preferences);
        initializeRfLintPreferences(preferences);
        initializeValidationPreferences(preferences);
        initializeTasksPreferences(preferences);
    }

    private void initializeFrameworkPreferences(final IEclipsePreferences preferences) {
        final List<PythonInstallationDirectory> interpreterPaths = PythonInstallationDirectoryFinder
                .whereArePythonInterpreters();
        if (!interpreterPaths.isEmpty()) {
            final String activePath = interpreterPaths.get(0).getAbsolutePath();
            final String activeExec = interpreterPaths.get(0).getInterpreter().name();

            final String allPaths = interpreterPaths.stream()
                    .map(PythonInstallationDirectory::getAbsolutePath)
                    .collect(joining(";"));
            final String allExecs = interpreterPaths.stream()
                    .map(dir -> dir.getInterpreter().name())
                    .collect(joining(";"));

            preferences.put(RedPreferences.ACTIVE_RUNTIME, activePath);
            preferences.put(RedPreferences.ACTIVE_RUNTIME_EXEC, activeExec);
            preferences.put(RedPreferences.OTHER_RUNTIMES, allPaths);
            preferences.put(RedPreferences.OTHER_RUNTIMES_EXECS, allExecs);
        }
    }

    private void initializeEditorPreferences(final IEclipsePreferences preferences) {
        preferences.putBoolean(RedPreferences.PARENT_DIRECTORY_NAME_IN_TAB, false);
        preferences.put(RedPreferences.FILE_ELEMENTS_OPEN_MODE, ElementOpenMode.OPEN_IN_SOURCE.name());
        preferences.put(RedPreferences.SEPARATOR_MODE, SeparatorsMode.FILE_TYPE_DEPENDENT.name());
        preferences.put(RedPreferences.SEPARATOR_TO_USE, "ssss");
        preferences.putInt(RedPreferences.MINIMAL_NUMBER_OF_ARGUMENT_COLUMNS, 5);
        preferences.put(RedPreferences.BEHAVIOR_ON_CELL_COMMIT, CellCommitBehavior.MOVE_TO_ADJACENT_CELL.name());
        preferences.put(RedPreferences.CELL_WRAPPING, CellWrappingStrategy.SINGLE_LINE_CUT.name());
    }

    private void initializeSourceFoldingPreferences(final IEclipsePreferences preferences) {
        preferences.putInt(RedPreferences.FOLDING_LINE_LIMIT, 2);
        preferences.putBoolean(RedPreferences.FOLDABLE_SECTIONS, true);
        preferences.putBoolean(RedPreferences.FOLDABLE_CASES, true);
        preferences.putBoolean(RedPreferences.FOLDABLE_TASKS, true);
        preferences.putBoolean(RedPreferences.FOLDABLE_KEYWORDS, true);
        preferences.putBoolean(RedPreferences.FOLDABLE_DOCUMENTATION, true);
    }

    private void initializeSourceEditorAssistantPreferences(final IEclipsePreferences preferences) {
        preferences.putBoolean(RedPreferences.ASSISTANT_AUTO_ACTIVATION_ENABLED, true);
        preferences.putInt(RedPreferences.ASSISTANT_AUTO_ACTIVATION_DELAY, 100);
        preferences.put(RedPreferences.ASSISTANT_AUTO_ACTIVATION_CHARS, "");
        preferences.putBoolean(RedPreferences.ASSISTANT_KEYWORD_PREFIX_AUTO_ADDITION_ENABLED, false);
        preferences.putBoolean(RedPreferences.ASSISTANT_KEYWORD_FROM_NOT_IMPORTED_LIBRARY_ENABLED, false);
    }

    private void initializeSyntaxColoringPreferences(final IEclipsePreferences preferences) {
        for (final SyntaxHighlightingCategory category : EnumSet.allOf(SyntaxHighlightingCategory.class)) {
            preferences.put(category.getPreferenceId(), category.getDefault().toPreferenceString());
        }
    }

    private void initializeAutodiscoveringPreferences(final IEclipsePreferences preferences) {
        preferences.putBoolean(RedPreferences.PROJECT_MODULES_RECURSIVE_ADDITION_ON_VIRTUALENV_ENABLED, false);
    }

    private void initializeProblemSeverityPreferences(final IEclipsePreferences preferences) {
        for (final ProblemCategory category : EnumSet.allOf(ProblemCategory.class)) {
            preferences.put(category.getId(), category.getDefaultSeverity().name());
        }
    }

    private void initializeDefaultLaunchConfigurationPreferences(final IEclipsePreferences preferences) {
        preferences.putBoolean(RedPreferences.LAUNCH_USE_ARGUMENT_FILE, true);
        preferences.putBoolean(RedPreferences.LAUNCH_USE_SINGLE_COMMAND_LINE_ARGUMENT, false);
        preferences.putBoolean(RedPreferences.LAUNCH_USE_SINGLE_FILE_DATA_SOURCE, false);
        preferences.putBoolean(RedPreferences.LIMIT_MSG_LOG_OUTPUT, false);
        preferences.putInt(RedPreferences.LIMIT_MSG_LOG_LENGTH, 80_000);
        preferences.put(RedPreferences.LAUNCH_AGENT_CONNECTION_HOST, AgentConnectionServer.DEFAULT_CONNECTION_HOST);
        preferences.putInt(RedPreferences.LAUNCH_AGENT_CONNECTION_PORT, AgentConnectionServer.DEFAULT_CONNECTION_PORT);
        preferences.putInt(RedPreferences.LAUNCH_AGENT_CONNECTION_TIMEOUT,
                AgentConnectionServer.DEFAULT_CONNECTION_TIMEOUT);
        preferences.put(RedPreferences.DEBUGGER_SUSPEND_ON_ERROR, IssuesStrategy.PROMPT.name().toLowerCase());
        preferences.putBoolean(RedPreferences.DEBUGGER_OMIT_LIB_KEYWORDS, false);
    }

    private void initializeRfLintPreferences(final IEclipsePreferences preferences) {
        preferences.put(RedPreferences.RFLINT_RULES_FILES, "");
        preferences.put(RedPreferences.RFLINT_RULES_CONFIG_NAMES, "");
        preferences.put(RedPreferences.RFLINT_RULES_CONFIG_SEVERITIES, "");
        preferences.put(RedPreferences.RFLINT_RULES_CONFIG_ARGS, "");
    }

    private void initializeValidationPreferences(final IEclipsePreferences preferences) {
        preferences.putBoolean(RedPreferences.TURN_OFF_VALIDATION, false);
    }

    private void initializeTasksPreferences(final IEclipsePreferences preferences) {
        preferences.putBoolean(RedPreferences.TASKS_DETECTION_ENABLED, true);
        preferences.put(RedPreferences.TASKS_TAGS, "FIXME;TODO");
        preferences.put(RedPreferences.TASKS_PRIORITIES, Priority.HIGH.name() + ";" + Priority.NORMAL.name());
    }
}
