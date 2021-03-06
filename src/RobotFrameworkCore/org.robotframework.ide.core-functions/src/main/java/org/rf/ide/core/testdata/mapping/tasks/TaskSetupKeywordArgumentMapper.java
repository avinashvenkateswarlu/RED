/*
 * Copyright 2018 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.mapping.tasks;

import java.util.List;
import java.util.Stack;

import org.rf.ide.core.testdata.mapping.table.IParsingMapper;
import org.rf.ide.core.testdata.mapping.table.ParsingStateHelper;
import org.rf.ide.core.testdata.model.FilePosition;
import org.rf.ide.core.testdata.model.RobotFileOutput;
import org.rf.ide.core.testdata.model.RobotVersion;
import org.rf.ide.core.testdata.model.table.LocalSetting;
import org.rf.ide.core.testdata.model.table.tasks.Task;
import org.rf.ide.core.testdata.text.read.ParsingState;
import org.rf.ide.core.testdata.text.read.RobotLine;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;

public class TaskSetupKeywordArgumentMapper implements IParsingMapper {

    private final ParsingStateHelper stateHelper = new ParsingStateHelper();

    @Override
    public final boolean isApplicableFor(final RobotVersion robotVersion) {
        return robotVersion.isNewerOrEqualTo(new RobotVersion(3, 1));
    }

    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput, final RobotLine currentLine,
            final RobotToken rt, final String text, final Stack<ParsingState> processingState) {

        final ParsingState state = stateHelper.getCurrentState(processingState);
        if (state == ParsingState.TASK_SETTING_SETUP) {
            final List<Task> tasks = robotFileOutput.getFileModel().getTasksTable().getTasks();
            final List<LocalSetting<Task>> setups = tasks.get(tasks.size() - 1).getSetups();
            return TaskSetupKeywordMapper.hasKeywordNameAlready(setups);
        }
        return state == ParsingState.TASK_SETTING_SETUP_KEYWORD
                || state == ParsingState.TASK_SETTING_SETUP_KEYWORD_ARGUMENT;
    }

    @Override
    public RobotToken map(final RobotLine currentLine, final Stack<ParsingState> processingState,
            final RobotFileOutput robotFileOutput, final RobotToken rt, final FilePosition fp, final String text) {

        rt.setText(text);

        final List<Task> tasks = robotFileOutput.getFileModel().getTasksTable().getTasks();
        final Task task = tasks.get(tasks.size() - 1);
        final List<LocalSetting<Task>> setups = task.getSetups();
        final LocalSetting<Task> tcs = setups.get(setups.size() - 1);
        tcs.addToken(rt);

        processingState.push(ParsingState.TASK_SETTING_SETUP_KEYWORD_ARGUMENT);
        return rt;
    }
}
